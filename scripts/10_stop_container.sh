#!/usr/bin/env bash
set -euo pipefail

CONTAINER_NAME="nok-nok-backend"

if docker ps -a --format '{{.Names}}' | grep -q "^${CONTAINER_NAME}$"; then
  echo "[INFO] Stopping existing container: ${CONTAINER_NAME}"
  docker stop "${CONTAINER_NAME}" || true
  docker rm "${CONTAINER_NAME}" || true
else
  echo "[INFO] No existing container to stop."
fi
