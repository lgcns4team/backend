#!/usr/bin/env bash
set -euo pipefail

REGION="ap-northeast-2"
SECRET_NAME="nok-nok-dev-app-env"

mkdir -p /opt/nok-nok
SECRET_JSON=$(aws secretsmanager get-secret-value \
  --region "$REGION" \
  --secret-id "$SECRET_NAME" \
  --query SecretString \
  --output text)

echo "$SECRET_JSON" | jq -r 'to_entries|map("\(.key)=\(.value|tostring)")|.[]' > /opt/nok-nok/app.env
chmod 600 /opt/nok-nok/app.env
