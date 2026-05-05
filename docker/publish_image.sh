#!/usr/bin/env bash

docker login

docker push "diegordgz/app-service:latest"
docker push "diegordgz/utility-service:latest"
