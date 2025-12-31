#!/bin/bash
set -euo pipefail

AWS_REGION="ap-northeast-2"
ECR_URI="313984758699.dkr.ecr.ap-northeast-2.amazonaws.com/bfree-kiosk-backend"
IMAGE_TAG="IMAGE_TAG"            # Jenkins가 치환
APP_NAME="nok-nok-backend"
SECRET_ID="nok-nok-dev-app-env"  # Secrets Manager name

# 0) 필수 도구 설치(없으면)
if ! command -v jq >/dev/null 2>&1; then
  yum -y install jq >/dev/null 2>&1 || true
fi

# 1) ECR 로그인 & 이미지 Pull
aws ecr get-login-password --region "${AWS_REGION}" \
  | docker login --username AWS --password-stdin "${ECR_URI}"

docker pull "${ECR_URI}:${IMAGE_TAG}"

# 2) Secrets Manager에서 JSON 조회
SECRET_JSON="$(aws secretsmanager get-secret-value \
  --region "${AWS_REGION}" \
  --secret-id "${SECRET_ID}" \
  --query SecretString --output text)"

DB_URL="$(echo "${SECRET_JSON}" | jq -r '.DB_URL')"
DB_USERNAME="$(echo "${SECRET_JSON}" | jq -r '.DB_USERNAME')"
DB_PASSWORD="$(echo "${SECRET_JSON}" | jq -r '.DB_PASSWORD')"
ENV_NAME="$(echo "${SECRET_JSON}" | jq -r '.ENV // "dev"')"

# 3) 기존 컨테이너 정리
if docker ps -a --format '{{.Names}}' | grep -q "^${APP_NAME}$"; then
  docker stop "${APP_NAME}" || true
  docker rm "${APP_NAME}" || true
fi

# 4) docker run 시 환경변수 주입 (Spring 표준 키로 매핑)
docker run -d --name "${APP_NAME}" \
  -p 8080:8080 \
  --restart always \
  -e SPRING_PROFILES_ACTIVE="${ENV_NAME}" \
  -e SPRING_DATASOURCE_URL="${DB_URL}" \
  -e SPRING_DATASOURCE_USERNAME="${DB_USERNAME}" \
  -e SPRING_DATASOURCE_PASSWORD="${DB_PASSWORD}" \
  "${ECR_URI}:${IMAGE_TAG}"