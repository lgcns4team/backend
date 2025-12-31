#!/usr/bin/env bash
set -euo pipefail

echo "[INFO] Prepare dirs"
mkdir -p /opt/nok-nok-deploy /opt/nok-nok-deploy/logs
chmod -R 755 /opt/nok-nok-deploy

# docker 설치/서비스 상태 확인
if ! command -v docker >/dev/null 2>&1; then
  echo "[ERROR] docker not found"
  exit 1
fi

systemctl enable docker >/dev/null 2>&1 || true
systemctl start docker >/dev/null 2>&1 || true

# -------------------------
# Install awscli/jq if missing
# -------------------------
if ! command -v aws >/dev/null 2>&1; then
  echo "[WARN] aws CLI not found. Installing..."

  if command -v apt-get >/dev/null 2>&1; then
    # Ubuntu/Debian
    apt-get update -y
    apt-get install -y --no-install-recommends curl unzip ca-certificates jq
    curl -fsSL "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o /tmp/awscliv2.zip
    unzip -q /tmp/awscliv2.zip -d /tmp
    /tmp/aws/install
    rm -rf /tmp/aws /tmp/awscliv2.zip

  elif command -v yum >/dev/null 2>&1; then
    # Amazon Linux 2 / RHEL 계열
    yum -y install curl unzip ca-certificates jq || true
    curl -fsSL "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o /tmp/awscliv2.zip
    unzip -q /tmp/awscliv2.zip -d /tmp
    /tmp/aws/install
    rm -rf /tmp/aws /tmp/awscliv2.zip

  else
    echo "[ERROR] No supported package manager found (apt-get/yum)."
    exit 1
  fi
fi

# jq가 없는 경우 설치 (apt/yum에서 위에서 설치되지만 방어적으로 체크)
if ! command -v jq >/dev/null 2>&1; then
  echo "[WARN] jq not found. Installing..."
  if command -v apt-get >/dev/null 2>&1; then
    apt-get update -y
    apt-get install -y --no-install-recommends jq
  elif command -v yum >/dev/null 2>&1; then
    yum -y install jq
  else
    echo "[ERROR] Cannot install jq (no apt-get/yum)"
    exit 1
  fi
fi

echo "[INFO] aws version: $(aws --version 2>&1 || true)"
echo "[INFO] jq version: $(jq --version 2>&1 || true)"
echo "[INFO] docker version: $(docker --version 2>&1 || true)"
