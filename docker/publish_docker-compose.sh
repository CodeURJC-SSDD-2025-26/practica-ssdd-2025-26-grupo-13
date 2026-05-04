#!/usr/bin/env bash

docker compose build
docker compose publish lucmp/mqm-app:0.1.0 --with-env