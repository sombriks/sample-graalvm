plugins {
    application
    kotlin("jvm") version "2.0.0-Beta2"
    id ("com.github.johnrengelman.shadow") version "8.1.1"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin-bundle:6.0.0-beta.4")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation("org.hamcrest:hamcrest:2.2")

}

application {
    mainClass.set("sample.graalvm.AppKt")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
