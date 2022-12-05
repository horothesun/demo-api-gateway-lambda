#!/bin/bash

[[ -z "${AWS_REGION}" ]] && echo "Error: AWS_REGION must be defined" && exit 10
[[ -z "${LAMBDA_NAME}" ]] && echo "Error: LAMBDA_NAME must be defined" && exit 20
[[ -z "${ECR_REGISTRY}" ]] && echo "Error: ECR_REGISTRY must be defined" && exit 30
[[ -z "${ECR_REPO_NAME}" ]] && echo "Error: ECR_REPO_NAME must be defined" && exit 40
[[ -z "${IMAGE_LATEST_TAG}" ]] && echo "Error: IMAGE_LATEST_TAG must be defined" && exit 50

IMAGE_SHA=$(
  aws ecr list-images --repository-name "${LAMBDA_NAME}" | \
    jq --arg imageLatestTag "${IMAGE_LATEST_TAG}" \
      --raw-output '.imageIds | map(select(.imageTag == $imageLatestTag))[0].imageDigest'
)
[[ -z "${IMAGE_SHA}" ]] && echo "Error: couldn't retrieve latest image SHA!" && exit 123

aws lambda update-function-code \
  --region "${AWS_REGION}" \
  --function-name "${LAMBDA_NAME}" \
  --image-uri "${ECR_REGISTRY}/${ECR_REPO_NAME}@${IMAGE_SHA}"

echo "Waiting for the update to complete..."
aws lambda wait function-updated-v2 --function-name "${LAMBDA_NAME}"
