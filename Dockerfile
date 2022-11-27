ARG JAVA_VERSION=11


# BUILDER

FROM openjdk:${JAVA_VERSION}-jre-slim-buster as builder

ARG SBT_VERSION=1.8.0

# install dependencies
RUN apt-get update && apt-get install -y curl bash tar ca-certificates make git procps \
       && rm -rf /var/lib/apt/lists/*

# install sbt
RUN curl -fsL "https://github.com/sbt/sbt/releases/download/v${SBT_VERSION}/sbt-${SBT_VERSION}.tgz" | tar xfz - -C /opt \
  && ln -s /opt/sbt/bin/sbt /usr/bin/sbt \
  && chmod +x /usr/bin/sbt

# run from non-root folder
RUN mkdir -p /work
WORKDIR /work

# compile
COPY . /work
RUN sbt clean assembly


# RUNNER

FROM public.ecr.aws/lambda/java:${JAVA_VERSION}

# TODO: still possible/necessary for AWS Lambda Java runtime? 🔥🔥🔥
# create and run as a non-root user:
#RUN useradd -m appuser
#WORKDIR /home/appuser
#USER appuser

#COPY --from=builder --chown=appuser /work/target/scala-3.2.1/demo-api-gateway-lambda.jar app.jar

COPY --from=builder /work/target/scala-3.2.1/demo-api-gateway-lambda.jar ${LAMBDA_TASK_ROOT}/lib/

CMD ["com.horothesun.demo.LambdaHandler::handleRequest"]
