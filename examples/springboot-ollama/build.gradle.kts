plugins {
    id("buildlogic.java-application-conventions")
    alias(libs.plugins.spring.dependency.management)
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    implementation(project(":spring-testcontainers"))
    implementation(project(":modules:ollama"))
    implementation(platform(libs.spring.bom))
    implementation(platform(libs.spring.boot.bom))
    implementation(libs.testcontainers.ollama)
    implementation(libs.slf4j.api)
    implementation(libs.logback.classic)
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.autoconfigure)
    implementation(platform(libs.spring.ai.bom))
    implementation(libs.bundles.spring.ai)
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.platform.launcher)
    testImplementation(libs.spring.boot.starter.test)
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("io.flowinquiry.testcontainers.examples.ollama.OllamaDemoApp")
}
