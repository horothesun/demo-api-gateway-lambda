ARG JAVA_VERSION=25


# BUILDER

FROM eclipse-temurin:${JAVA_VERSION}-jdk-jammy AS builder

ARG SBT_VERSION=2.0.0

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
RUN sbt ";clean;assembly"


# RUNNER

FROM public.ecr.aws/lambda/java:${JAVA_VERSION}

COPY --from=builder /work/target/out/jvm/scala-2.13.18/demo-api-gateway-lambda/demo-api-gateway-lambda.jar

CMD ["com.horothesun.demo.lambda.Handler::handleRequest"]
