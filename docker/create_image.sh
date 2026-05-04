#!/usr/bin/bash
docker login

docker build -f docker/app-service.Dockerfile -t lucmp/app-service:latest .
docker build -f docker/utility-service.Dockerfile -t lucmp/utility-service:latest .
