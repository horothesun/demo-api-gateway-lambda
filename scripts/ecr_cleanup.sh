#!/bin/bash

[[ -z "${ECR_REPO_NAME}" ]] && echo "Error: ECR_REPO_NAME must be defined" && exit 10

IMAGE_DIGESTS_TO_DELETE=$(
  aws ecr describe-images \
    --repository-name "${ECR_REPO_NAME}" \
    --filter "tagStatus=UNTAGGED" | \
    jq --raw-output '
        .imageDetails
      | sort_by(.imagePushedAt)
      | reverse
      | del(.[0])
      | map(.imageDigest)
    '
)

if [[ "${IMAGE_DIGESTS_TO_DELETE}" == "[]" ]]; then
  echo "No image to delete was found."
else
  IMAGE_IDS=$(
    echo "${IMAGE_DIGESTS_TO_DELETE}" | \
      jq --raw-output 'map("imageDigest=\(.)") | join(" ")'
  )
  # shellcheck disable=SC2086
  aws ecr batch-delete-image \
    --repository-name "${ECR_REPO_NAME}" \
    --image-ids ${IMAGE_IDS}
fi
