#!/usr/bin/env bash
set -euo pipefail

CONTAINER_NAME="nok-nok-backend"
IMAGE_URI_FILE="/opt/nok-nok-deploy/image_uri.txt"
ENV_FILE="/opt/nok-nok-deploy/app.env"

APP_PORT="8080"
HOST_PORT="8080"

if [[ ! -f "${IMAGE_URI_FILE}" ]]; then
  echo "[ERROR] image_uri.txt not found at ${IMAGE_URI_FILE}"
  exit 1
fi

if [[ ! -f "${ENV_FILE}" ]]; then
  echo "[ERROR] env file not found at ${ENV_FILE}"
  exit 1
fi

IMAGE_URI="$(cat "${IMAGE_URI_FILE}")"
echo "[INFO] Pull image: ${IMAGE_URI}"
docker pull "${IMAGE_URI}"

# 기존 컨테이너가 있으면 정리 (중복 방지)
if docker ps -a --format '{{.Names}}' | grep -q "^${CONTAINER_NAME}$"; then
  echo "[INFO] Removing existing container: ${CONTAINER_NAME}"
  docker stop "${CONTAINER_NAME}" || true
  docker rm "${CONTAINER_NAME}" || true
fi

echo "[INFO] Run new container: ${CONTAINER_NAME}"
docker run -d \
  --name "${CONTAINER_NAME}" \
  --restart unless-stopped \
  --env-file "${ENV_FILE}" \
  -e SPRING_PROFILES_ACTIVE=prod \
  -p "${HOST_PORT}:${APP_PORT}" \
  "${IMAGE_URI}"
