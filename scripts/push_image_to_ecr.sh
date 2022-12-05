#!/bin/bash

[[ -z "${AWS_REGION}" ]] && echo "Error: AWS_REGION must be defined" && exit 10
[[ -z "${ECR_REGISTRY}" ]] && echo "Error: ECR_REGISTRY must be defined" && exit 20
[[ -z "${ECR_REPO_NAME}" ]] && echo "Error: ECR_REPO_NAME must be defined" && exit 30
[[ -z "${IMAGE_LATEST_TAG}" ]] && echo "Error: IMAGE_LATEST_TAG must be defined" && exit 40

aws ecr get-login-password --region "${AWS_REGION}" | \
  docker login --username "AWS" --password-stdin "${ECR_REGISTRY}"
docker push "${ECR_REGISTRY}/${ECR_REPO_NAME}:${IMAGE_LATEST_TAG}"
