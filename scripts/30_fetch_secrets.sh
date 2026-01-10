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

if ! command -v jq >/dev/null 2>&1; then
  echo "[ERROR] jq not found. Install jq in AMI or user_data."
  exit 1
fi

DRIVER_NAME="$(echo "${RAW_JSON}" | jq -r '.DRIVER_NAME // empty')"
DRIVER_URL="$(echo "${RAW_JSON}" | jq -r '.DRIVER_URL // empty')"
DRIVER_USER_NAME="$(echo "${RAW_JSON}" | jq -r '.DRIVER_USER_NAME // empty')"
DRIVER_PASSWORD="$(echo "${RAW_JSON}" | jq -r '.DRIVER_PASSWORD // empty')"

missing=0
if [[ -z "${DRIVER_NAME}" ]]; then
  echo "[ERROR] Secret missing DRIVER_NAME"
  missing=1
fi
if [[ -z "${DRIVER_URL}" ]]; then
  echo "[ERROR] Secret missing DRIVER_URL"
  missing=1
fi
if [[ -z "${DRIVER_USER_NAME}" ]]; then
  echo "[ERROR] Secret missing DRIVER_USER_NAME"
  missing=1
fi
if [[ -z "${DRIVER_PASSWORD}" ]]; then
  echo "[ERROR] Secret missing DRIVER_PASSWORD"
  missing=1
fi
if [[ "${missing}" -ne 0 ]]; then
  exit 1
fi

cat > "${ENV_FILE}" <<EOF
DRIVER_NAME=${DRIVER_NAME}
DRIVER_URL=${DRIVER_URL}
DRIVER_USER_NAME=${DRIVER_USER_NAME}
DRIVER_PASSWORD=${DRIVER_PASSWORD}
EOF

chmod 600 "${ENV_FILE}"
echo "[INFO] Wrote env file: ${ENV_FILE}"
