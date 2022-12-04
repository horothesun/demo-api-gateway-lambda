#!/bin/bash

[[ -z "${ECR_REGISTRY}" ]] && echo "Error: ECR_REGISTRY must be defined" && exit 10
[[ -z "${ECR_REPO_NAME}" ]] && echo "Error: ECR_REPO_NAME must be defined" && exit 20
[[ -z "${IMAGE_LATEST_TAG}" ]] && echo "Error: IMAGE_LATEST_TAG must be defined" && exit 30
[[ -z "${SBT_VERSION}" ]] && echo "Error: SBT_VERSION must be defined" && exit 40

ECR_REPO_NAME_AND_LATEST_TAG="${ECR_REPO_NAME}:${IMAGE_LATEST_TAG}"

docker build --tag "${ECR_REPO_NAME_AND_LATEST_TAG}" \
  --build-arg "SBT_VERSION=${SBT_VERSION}" \
  --file "./Dockerfile" "."

IMAGE_ID=$(
  docker images --format '{{json .}}' "${ECR_REPO_NAME_AND_LATEST_TAG}" | \
    jq --raw-output '.ID'
)
docker tag "${IMAGE_ID}" "${ECR_REGISTRY}/${ECR_REPO_NAME_AND_LATEST_TAG}"
