#!/usr/bin/env bash
set -euo pipefail

DEPLOY_DIR="/opt/nok-nok-deploy"
LOG_DIR="${DEPLOY_DIR}/logs"

mkdir -p "${DEPLOY_DIR}" "${LOG_DIR}"
chmod -R 755 "${DEPLOY_DIR}"

# docker 설치/서비스 상태 확인
if ! command -v docker >/dev/null 2>&1; then
  echo "[ERROR] docker not found"
  exit 1
fi

systemctl enable docker >/dev/null 2>&1 || true
systemctl start docker >/dev/null 2>&1 || true

# aws CLI 확인 (없으면 실패 처리)
if ! command -v aws >/dev/null 2>&1; then
  echo "[ERROR] aws CLI not found. Install awscli in AMI or user_data."
  exit 1
fi

# jq 설치 (없으면 자동 설치 시도)
if ! command -v jq >/dev/null 2>&1; then
  echo "[INFO] jq not found. Installing jq..."
  if command -v yum >/dev/null 2>&1; then
    yum -y install jq >/dev/null 2>&1
  elif command -v apt-get >/dev/null 2>&1; then
    apt-get update >/dev/null 2>&1
    apt-get install -y jq >/dev/null 2>&1
  else
    echo "[ERROR] No supported package manager found to install jq."
    exit 1
  fi
fi

echo "[INFO] 준비 완료: docker/aws/jq OK"
