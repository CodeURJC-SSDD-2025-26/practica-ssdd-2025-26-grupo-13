#!/usr/bin/env bash

set -euo pipefail

USERNAME="${1:-${DOCKER_USERNAME:-}}"

if [[ -z "$USERNAME" ]]; then
  echo "Usage: $0 <docker-username>"
  echo "Or set DOCKER_USERNAME in the environment."
  exit 1
fi

docker login

docker push "${USERNAME}/app-service:latest"
docker push "${USERNAME}/utility-service:latest"
