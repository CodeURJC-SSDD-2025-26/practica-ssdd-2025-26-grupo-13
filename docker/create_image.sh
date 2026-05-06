#!/usr/bin/env bash

set -euo pipefail

USERNAME="${1:-${DOCKER_USERNAME:-}}"

if [[ -z "$USERNAME" ]]; then
  echo "Usage: $0 <docker-username>"
  echo "Or set DOCKER_USERNAME in the environment."
  exit 1
fi

docker login

docker build -f docker/app-service.Dockerfile -t "${USERNAME}/app-service:latest" .
docker build -f docker/utility-service.Dockerfile -t "${USERNAME}/utility-service:latest" .
