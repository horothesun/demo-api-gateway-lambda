#!/bin/bash

[[ -z "${LOGIN_AWS_REGION}" ]] && echo "Error: LOGIN_AWS_REGION must be defined" && exit 10
[[ -z "${LAMBDA_NAME}" ]] && echo "Error: LAMBDA_NAME must be defined" && exit 20
[[ -z "${ECR_REGISTRY}" ]] && echo "Error: ECR_REGISTRY must be defined" && exit 30
[[ -z "${ECR_REPO_NAME_AND_LATEST_TAG}" ]] && echo "Error: ECR_REPO_NAME_AND_LATEST_TAG must be defined" && exit 40

aws lambda update-function-code \
  --region "${LOGIN_AWS_REGION}" \
  --function-name "${LAMBDA_NAME}" \
  --image-uri "${ECR_REGISTRY}/${ECR_REPO_NAME_AND_LATEST_TAG}"

aws lambda wait function-updated-v2 --function-name "${LAMBDA_NAME}"
