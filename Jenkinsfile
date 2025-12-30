#!/usr/bin/env bash
set -euo pipefail

REGION="ap-northeast-2"

# Jenkins가 번들에 포함시킨 파일(아래 Jenkinsfile 참고)
IMAGE_URI=$(cat /opt/codedeploy-agent/deployment-root/*/*/deployment-archive/image-uri.txt)

docker rm -f nok-nok-app >/dev/null 2>&1 || true

docker run -d \
  --name nok-nok-app \
  --restart always \
  --env-file /opt/nok-nok/app.env \
  -p 8080:8080 \
  "$IMAGE_URI"
