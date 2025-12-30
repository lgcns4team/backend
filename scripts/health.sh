#!/bin/bash
set -e

URL="http://127.0.0.1:8080/nok-nok/actuator/health/readiness"

for i in {1..30}; do
  if curl -fsS "${URL}" | grep -q '"status":"UP"'; then
    exit 0
  fi
  sleep 2
done

echo "Readiness health check failed: ${URL}"
exit 1