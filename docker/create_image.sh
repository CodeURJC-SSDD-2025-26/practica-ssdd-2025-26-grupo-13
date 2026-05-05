#!/usr/bin/bash
docker login

docker build -f docker/app-service.Dockerfile -t diegordgz/app-service:latest .
docker build -f docker/utility-service.Dockerfile -t diegordgz/utility-service:latest .
