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

IMAGE_URI="$(cat "${IMAGE_URI_FILE}")"
echo "[INFO] Pull image: ${IMAGE_URI}"
docker pull "${IMAGE_URI}"

echo "[INFO] Run new container: ${CONTAINER_NAME}"
docker run -d \
  --name "${CONTAINER_NAME}" \
  --restart unless-stopped \
  --env-file "${ENV_FILE}" \
  -p "${HOST_PORT}:${APP_PORT}" \
  "${IMAGE_URI}"