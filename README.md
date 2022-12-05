# Demo API Gateway triggered Lambda

[![CI](https://github.com/horothesun/demo-api-gateway-lambda/workflows/CI/badge.svg)](https://github.com/horothesun/demo-api-gateway-lambda/actions/workflows/ci.yml)
[![Manual API call](https://github.com/horothesun/demo-api-gateway-lambda/actions/workflows/manual_api_call.yml/badge.svg)](https://github.com/horothesun/demo-api-gateway-lambda/actions/workflows/manual_api_call.yml)

Based on API Gateway's

- HTTP API (not REST API) and
- payload format version `2.0`.

## CI

Secrets

- `AWS_ACCOUNT_ID`
- `ECR_REPO_NAME`
- `LAMBDA_NAME`
- `LOGIN_AWS_REGION`
- `PROD_CI_ROLE_ARN`

## API call

Secrets

- `PROD_BASE_URL`

## Docker

Build an image with

```bash
docker build --tag <REPOSITORY:TAG> \
  --build-arg JAVA_VERSION=11 \
  --build-arg SBT_VERSION=1.8.0 \
  --file ./Dockerfile .
```

(default `JAVA_VERSION` and `SBT_VERSION` values in `Dockerfile`).

Run the image with

```bash
docker run --rm --publish 9000:8080 <REPOSITORY:TAG>
```

and test it with

```bash
curl --silent \
  --request "POST" "http://localhost:9000/2015-03-31/functions/function/invocations" \
  --data '{"body":"hello world!","isBase64Encoded":false}' | \
  jq --raw-output '.' | jq '.'
```
