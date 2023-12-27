# TODO make this Dockerfile customizable (ARGS, ENV, ETC)
FROM ghcr.io/graalvm/jdk-community:21 as PART1

WORKDIR /sample-graalvm

ADD build.gradle.kts gradlew settings.gradle.kts ./
ADD src src/
ADD gradle gradle/

RUN ./gradlew build test shadowJar
RUN native-image -jar build/libs/sample-graalvm-all.jar -o build/sample-graalvm

FROM eclipse-temurin:21-jre-jammy as PART2

COPY --from=PART1 /sample-graalvm/build/sample-graalvm /sample-graalvm

ENTRYPOINT /sample-graalvm
