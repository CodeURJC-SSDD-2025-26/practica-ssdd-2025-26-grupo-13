#!/usr/bin/env bash

set -euo pipefail

USERNAME="${1:-${DOCKER_USERNAME:-}}"

if [[ -z "$USERNAME" ]]; then
  echo "Usage: $0 <docker-username>"
  echo "Or set DOCKER_USERNAME in the environment."
  exit 1
fi

docker login

docker compose build
docker compose publish "${USERNAME}/mqm-app:0.1.0" --with-env
