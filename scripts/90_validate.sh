#!/usr/bin/env bash
set -euo pipefail

URL="http://127.0.0.1:8080/nok-nok/actuator/health/readiness"

echo "[INFO] Validate: ${URL}"
for i in {1..30}; do
  CODE="$(curl -s -o /dev/null -w "%{http_code}" "${URL}" || true)"
  if [[ "${CODE}" == "200" ]]; then
    echo "[OK] Service is healthy"
    exit 0
  fi
  echo "[WAIT] health=${CODE} (try ${i}/30)"
  sleep 3
done

echo "[ERROR] Service did not become healthy"
docker logs nok-nok-backend --tail 200 || true
exit 1