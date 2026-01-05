pipeline {
  agent any

  options {
    timestamps()
    disableConcurrentBuilds()
    skipDefaultCheckout(true)
  }

  environment {
    AWS_REGION         = 'ap-northeast-2'
    ECR_REPO_NAME      = 'bfree-kiosk-backend'
    CODEDEPLOY_APP     = 'nok-nok-dev-codedeploy-app'
    CODEDEPLOY_DG      = 'nok-nok-dev-dg'
    CODEDEPLOY_BUCKET  = 'bfree-kiosk-codedeploy-313984758699-dev'
    DEPLOY_S3_PREFIX   = 'codedeploy/nok-nok'
  }

  stages {

    stage('Checkout') {
      steps {
        deleteDir()
        checkout scm
      }
    }

    stage('Build (Gradle JDK17)') {
      agent {
        docker {
          image 'gradle:9.2.1-jdk17'
          args '-u root:root'
          reuseNode true
        }
      }
      steps {
    sh '''#!/bin/bash
      set -euo pipefail
      java -version
      chmod +x ./gradlew
      ./gradlew clean build -x test

      echo "[INFO] build/libs output:"
      ls -al build/libs || true

      #XX 이전 버전 XX
      # 실행 가능한 jar를 app.jar로 고정 (plain.jar 제외)
      #JAR_PATH="$(ls -1 build/libs/*.jar | grep -v -- '-plain\\.jar$' | head -n 1 || true)"
      #if [[ -z "${JAR_PATH}" ]]; then
        #echo "[ERROR] No runnable jar found in build/libs"
        #exit 1
      #fi

      #cp -f "${JAR_PATH}" build/libs/app.jar
      #echo "[INFO] Selected jar: ${JAR_PATH} -> build/libs/app.jar"

      #ls -al build/libs/app.jar
      #XX 이전 버전 XX


       # app.jar만 사용
            if [[ ! -f build/libs/app.jar ]]; then
              echo "[ERROR] build/libs/app.jar not found"
              exit 1
            fi

            echo "[INFO] Using build/libs/app.jar"
            ls -al build/libs/app.jar

    '''
      }
    }

    stage('ECR Login') {
      steps {
        sh '''#!/bin/bash
          set -euo pipefail
          aws --version

          ACCOUNT_ID="$(aws sts get-caller-identity --query Account --output text)"
          ECR_REGISTRY="${ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"

          aws ecr get-login-password --region "${AWS_REGION}" \
            | docker login --username AWS --password-stdin "${ECR_REGISTRY}"

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

      # ✅ GIT_COMMIT이 없을 수 있으므로 안전하게 대체
      if [[ -n "${GIT_COMMIT:-}" ]]; then
        IMAGE_TAG="${GIT_COMMIT}"
      else
        IMAGE_TAG="$(git rev-parse --short=12 HEAD)"
      fi

      IMAGE_LATEST="latest"

      echo "[INFO] ECR_URI=${ECR_URI}"
      echo "[INFO] IMAGE_TAG=${IMAGE_TAG}"

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
          test -f image_uri.txt

          rm -f deployment_bundle.zip
          zip -r deployment_bundle.zip appspec.yml scripts image_uri.txt
        '''
      }
    }

    stage('Upload Bundle to S3') {
      steps {
        sh '''#!/bin/bash
          set -euo pipefail
          aws --version

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
          aws --version

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
