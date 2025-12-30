#!/usr/bin/env bash
set -euo pipefail

mkdir -p /opt/nok-nok-deploy
mkdir -p /opt/nok-nok-deploy/logs
chmod -R 755 /opt/nok-nok-deploy

# docker 설치/서비스 상태 확인 (AMI에 이미 설치돼 있다면 harmless)
if ! command -v docker >/dev/null 2>&1; then
  echo "[ERROR] docker not found"
  exit 1
fi

systemctl enable docker >/dev/null 2>&1 || true
systemctl start docker >/dev/null 2>&1 || true
