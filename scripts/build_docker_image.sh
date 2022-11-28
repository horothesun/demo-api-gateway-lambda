#!/bin/bash

[[ -z "${ECR_REPO_NAME_AND_LATEST_TAG}" ]] && echo "Error: ECR_REPO_NAME_AND_LATEST_TAG must be defined" && exit 10
[[ -z "${SBT_VERSION}" ]] && echo "Error: SBT_VERSION must be defined" && exit 20
[[ -z "${ECR_REGISTRY}" ]] && echo "Error: ECR_REGISTRY must be defined" && exit 30

docker build --tag "${ECR_REPO_NAME_AND_LATEST_TAG}" \
  --build-arg "SBT_VERSION=${SBT_VERSION}" \
  --file "./Dockerfile" "."

IMAGE_ID=$(
  docker images --format '{{json .}}' "${ECR_REPO_NAME_AND_LATEST_TAG}" | \
    jq --raw-output '.ID'
)
docker tag "${IMAGE_ID}" "${ECR_REGISTRY}/${ECR_REPO_NAME_AND_LATEST_TAG}"
