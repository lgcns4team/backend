set -euo pipefail

AWS_REGION="ap-northeast-2"
ECR_URI="313984758699.dkr.ecr.ap-northeast-2.amazonaws.com/bfree-kiosk-backend"
IMAGE_TAG="IMAGE_TAG"   # Jenkins에서 치환
APP_NAME="bfree-kiosk-backend"

SECRET_ARN_OR_NAME="nok-nok/dev/rds"  # 예: Secrets Manager secret name 또는 ARN

# 필수: jq 설치(없으면 설치)
if ! command -v jq >/dev/null 2>&1; then
  yum -y install jq >/dev/null 2>&1 || true
fi

# ECR 로그인
aws ecr get-login-password --region "${AWS_REGION}" \
  | docker login --username AWS --password-stdin "${ECR_URI}"

# 이미지 Pull
docker pull "${ECR_URI}:${IMAGE_TAG}"

# Secrets Manager에서 secret(JSON) 조회
SECRET_JSON="$(aws secretsmanager get-secret-value \
  --region "${AWS_REGION}" \
  --secret-id "${SECRET_ARN_OR_NAME}" \
  --query SecretString --output text)"

# 예시: secret JSON 키를 환경변수로 매핑 (당신 secret 키에 맞게 수정)
DB_URL="$(echo "${SECRET_JSON}" | jq -r '.DB_URL')"
DB_USERNAME="$(echo "${SECRET_JSON}" | jq -r '.DB_USERNAME')"
DB_PASSWORD="$(echo "${SECRET_JSON}" | jq -r '.DB_PASSWORD')"

# 기존 컨테이너 정리(방어적으로 한 번 더)
if docker ps -a --format '{{.Names}}' | grep -q "^${APP_NAME}$"; then
  docker stop "${APP_NAME}" || true
  docker rm "${APP_NAME}" || true
fi

# 컨테이너 실행
docker run -d --name "${APP_NAME}" \
  -p 8080:8080 \
  --restart always \
  -e SPRING_PROFILES_ACTIVE=dev \
  -e DB_URL="${DB_URL}" \
  -e DB_USERNAME="${DB_USERNAME}" \
  -e DB_PASSWORD="${DB_PASSWORD}" \
  "${ECR_URI}:${IMAGE_TAG}"