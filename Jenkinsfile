pipeline {
  agent any

  options {
    timestamps()
    ansiColor('xterm')
  }

  environment {
    AWS_REGION   = 'ap-northeast-2'
    ECR_REPO     = 'bfree-kiosk-backend'
    APP_NAME     = 'nok-nok-dev-codedeploy-app'
    DG_NAME      = 'nok-nok-dev-dg'
    // CodeDeploy S3 버킷명은 Jenkins Credential(Secret text)로 넣는 것을 권장하지만
    // 우선 고정 값으로 예시
    CODEDEPLOY_BUCKET = 'bfree-kiosk-codedeploy-313984758699-dev'
  }

  stages {

    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build JAR') {
      steps {
        sh '''
          set -euo pipefail
          chmod +x ./gradlew
          ./gradlew clean build -x test
        '''
      }
    }

    stage('Build & Push Docker Image to ECR') {
      steps {
        sh '''
          set -euo pipefail

          ACCOUNT_ID="$(aws sts get-caller-identity --query Account --output text)"
          ECR_URI="${ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO}"
          IMAGE_TAG="${GIT_COMMIT}"

          aws ecr get-login-password --region "${AWS_REGION}" | docker login --username AWS --password-stdin "${ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"

          docker build -t "${ECR_URI}:${IMAGE_TAG}" .
          docker push "${ECR_URI}:${IMAGE_TAG}"

          echo "${ECR_URI}:${IMAGE_TAG}" > image_uri.txt
        '''
      }
    }

    stage('Package AppSpec & Scripts (for CodeDeploy)') {
      steps {
        sh '''
          set -euo pipefail

          # appspec.yml + scripts/ 가 리포지토리에 있어야 합니다.
          test -f appspec.yml
          test -d scripts

          mkdir -p bundle
          cp -r appspec.yml scripts bundle/

          # 배포 대상에서 ECR pull 할 수 있도록 image uri 전달
          cp image_uri.txt bundle/image_uri.txt

          (cd bundle && zip -r ../codedeploy_bundle.zip .)
        '''
      }
    }

    stage('Upload Bundle to S3') {
      steps {
        sh '''
          set -euo pipefail
          KEY="codedeploy/${JOB_NAME}/${BUILD_NUMBER}/codedeploy_bundle.zip"
          aws s3 cp codedeploy_bundle.zip "s3://${CODEDEPLOY_BUCKET}/${KEY}"
          echo "${KEY}" > s3_key.txt
        '''
      }
    }

    stage('Trigger CodeDeploy') {
      steps {
        sh '''
          set -euo pipefail
          KEY="$(cat s3_key.txt)"

          aws deploy create-deployment \
            --application-name "${APP_NAME}" \
            --deployment-group-name "${DG_NAME}" \
            --s3-location bucket="${CODEDEPLOY_BUCKET}",key="${KEY}",bundleType=zip \
            --file-exists-behavior OVERWRITE \
            --region "${AWS_REGION}"
        '''
      }
    }
  }

  post {
    always {
      archiveArtifacts artifacts: '*.zip,*.txt', onlyIfSuccessful: false
    }
  }
}
