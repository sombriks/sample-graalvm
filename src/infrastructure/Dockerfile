# TODO make this Dockerfile customizable (ARGS, ENV, PORT, etc)
# TODO again, a gradle build as part of the image creation is a horrible idea
#FROM gradle:jdk21 as PART0
#
#WORKDIR /sample-graalvm
#
#ADD build.gradle.kts settings.gradle.kts ./
#ADD src src/
#
#RUN gradle build test shadowJar --no-daemon

FROM ghcr.io/graalvm/native-image:22 as PART1

WORKDIR /build

#COPY --from=PART0 /sample-graalvm/build/libs/sample-graalvm-all.jar /build/sample-graalvm-all.jar

ADD build/libs/sample-graalvm-all.jar ./

RUN native-image -jar /build/sample-graalvm-all.jar -o /build/sample-graalvm

# FROM ghcr.io/graalvm/jdk:22 as PART2
FROM eclipse-temurin:21-jre-jammy as PART2

# fallback images still relies on original jar and the installed jvm
# so we still need a jvm base image and the original jar.
COPY --from=PART1 /build/sample-graalvm /sample-graalvm
COPY --from=PART1 /build/sample-graalvm-all.jar /sample-graalvm-all.jar

EXPOSE 7070

ENTRYPOINT /sample-graalvm
