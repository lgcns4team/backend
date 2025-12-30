#!/bin/bash
set -e

APP_NAME="bfree-kiosk-backend"

if docker ps -a --format '{{.Names}}' | grep -q "^${APP_NAME}$"; then
  docker stop "${APP_NAME}" || true
  docker rm "${APP_NAME}" || true
fi