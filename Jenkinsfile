pipeline {
  agent any

  options {
    timestamps()
    disableConcurrentBuilds()
  }

  environment {
    AWS_REGION         = 'ap-northeast-2'
    ECR_REPO_NAME      = 'bfree-kiosk-backend'
    CODEDEPLOY_APP     = 'nok-nok-dev-codedeploy-app'
    CODEDEPLOY_DG      = 'nok-nok-dev-dg'
    CODEDEPLOY_BUCKET  = 'bfree-kiosk-codedeploy-313984758699-dev'
    DEPLOY_S3_PREFIX   = 'codedeploy/nok-nok'
    CONTAINER_NAME     = 'nok-nok-backend'
    CONTAINER_PORT     = '8080'
  }

  stages {

    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    // ✅ 여기만 "gradle:9.2.1-jdk17" 컨테이너에서 실행 (JDK 17 문제 해결)
    stage('Test & Build (Gradle JDK17)') {
      agent {
        docker {
          image 'gradle:9.2.1-jdk17'
          // Jenkins가 호스트 docker.sock를 쓰는 구조라면 보통 아래 args 없어도 됩니다.
          // 권한 문제가 나오면 -u root:root 또는 docker.sock 마운트를 추가하세요.
          args '-u root:root'
          reuseNode true
        }
      }
      steps {
        sh '''#!/bin/bash
          set -euo pipefail
          java -version
          chmod +x ./gradlew
          ./gradlew clean test
          ./gradlew build -x test
        '''
      }
    }

    stage('ECR Login') {
      steps {
        sh '''#!/bin/bash
          set -euo pipefail
          ACCOUNT_ID="$(aws sts get-caller-identity --query Account --output text)"
          ECR_REGISTRY="${ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
          aws ecr get-login-password --region "${AWS_REGION}" | docker login --username AWS --password-stdin "${ECR_REGISTRY}"
          echo "${ACCOUNT_ID}" > .account_id
        '''
      }
    }

    stage('Build & Push Docker Image') {
      steps {
        sh '''#!/bin/bash
          set -euo pipefail
          ACCOUNT_ID="$(cat .account_id)"
          ECR_URI="${ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO_NAME}"

          IMAGE_TAG="${GIT_COMMIT}"
          IMAGE_LATEST="latest"

          docker build -t "${ECR_URI}:${IMAGE_TAG}" -t "${ECR_URI}:${IMAGE_LATEST}" .
          docker push "${ECR_URI}:${IMAGE_TAG}"
          docker push "${ECR_URI}:${IMAGE_LATEST}"

          echo "${ECR_URI}:${IMAGE_TAG}" > image_uri.txt
        '''
      }
    }

    stage('Create CodeDeploy Bundle (Zip)') {
      steps {
        sh '''#!/bin/bash
          set -euo pipefail

          test -f appspec.yml
          test -d scripts

          rm -f deployment_bundle.zip
          zip -r deployment_bundle.zip appspec.yml scripts image_uri.txt
        '''
      }
    }

    stage('Upload Bundle to S3') {
      steps {
        sh '''#!/bin/bash
          set -euo pipefail

          KEY="${DEPLOY_S3_PREFIX}/${JOB_NAME}/${BUILD_NUMBER}/deployment_bundle.zip"
          aws s3 cp deployment_bundle.zip "s3://${CODEDEPLOY_BUCKET}/${KEY}"
          echo "${KEY}" > s3_key.txt
        '''
      }
    }

    stage('Trigger CodeDeploy') {
      steps {
        sh '''#!/bin/bash
          set -euo pipefail

          KEY="$(cat s3_key.txt)"

          aws deploy create-deployment \
            --application-name "${CODEDEPLOY_APP}" \
            --deployment-group-name "${CODEDEPLOY_DG}" \
            --deployment-config-name "CodeDeployDefault.OneAtATime" \
            --file-exists-behavior OVERWRITE \
            --s3-location bucket="${CODEDEPLOY_BUCKET}",key="${KEY}",bundleType=zip \
            --region "${AWS_REGION}"
        '''
      }
    }
  }

  post {
    always {
      archiveArtifacts artifacts: 'deployment_bundle.zip,image_uri.txt,s3_key.txt', onlyIfSuccessful: false
    }
  }
}
