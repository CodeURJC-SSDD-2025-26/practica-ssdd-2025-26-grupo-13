#!/usr/bin/env bash

docker compose build
docker compose publish diegordgz/mqm-app:0.1.0 --with-env