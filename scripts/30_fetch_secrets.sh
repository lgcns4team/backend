#!/usr/bin/env bash
set -euo pipefail

AWS_REGION="ap-northeast-2"
SECRET_ID="nok-nok-dev-app-env"
ENV_FILE="/opt/nok-nok-deploy/app.env"

echo "[INFO] Fetch secret: ${SECRET_ID}"
RAW_JSON="$(aws secretsmanager get-secret-value \
  --secret-id "${SECRET_ID}" \
  --query SecretString \
  --output text \
  --region "${AWS_REGION}")"

# jq는 00_prepare_dirs.sh에서 설치 보장
DB_URL="$(echo "${RAW_JSON}" | jq -r '.DB_URL')"
DB_USERNAME="$(echo "${RAW_JSON}" | jq -r '.DB_USERNAME')"
DB_PASSWORD="$(echo "${RAW_JSON}" | jq -r '.DB_PASSWORD')"
ENV_NAME="$(echo "${RAW_JSON}" | jq -r '.ENV // "dev"')"

# 필수 값 검증
if [[ -z "${DB_URL}" || "${DB_URL}" == "null" ]]; then
  echo "[ERROR] Secret missing DB_URL"
  exit 1
fi
if [[ -z "${DB_USERNAME}" || "${DB_USERNAME}" == "null" ]]; then
  echo "[ERROR] Secret missing DB_USERNAME"
  exit 1
fi
if [[ -z "${DB_PASSWORD}" || "${DB_PASSWORD}" == "null" ]]; then
  echo "[ERROR] Secret missing DB_PASSWORD"
  exit 1
fi

# Spring 표준 환경변수로 저장 (로그에 비밀번호 출력 금지)
cat > "${ENV_FILE}" <<EOF
SPRING_PROFILES_ACTIVE=${ENV_NAME}
SPRING_DATASOURCE_URL=${DB_URL}
SPRING_DATASOURCE_USERNAME=${DB_USERNAME}
SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}
EOF

chmod 600 "${ENV_FILE}"
echo "[INFO] Wrote env file: ${ENV_FILE} (Spring datasource vars)"
