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

# jq 필요 (AMI에 설치되어 있어야 함)
if ! command -v jq >/dev/null 2>&1; then
  echo "[ERROR] jq not found. Install jq in AMI or user_data."
  exit 1
fi

echo "${RAW_JSON}" | jq -r 'to_entries|map("\(.key)=\(.value|tostring)")|.[]' > "${ENV_FILE}"
chmod 600 "${ENV_FILE}"

echo "[INFO] Wrote env file: ${ENV_FILE}"