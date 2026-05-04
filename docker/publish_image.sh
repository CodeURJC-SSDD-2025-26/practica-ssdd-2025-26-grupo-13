#!/usr/bin/env bash

docker login

docker push "lucmp/app-service:latest"
docker push "lucmp/utility-service:latest"
