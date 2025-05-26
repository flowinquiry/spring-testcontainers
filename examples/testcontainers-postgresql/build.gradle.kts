plugins {
    id("java-library")
    id("application")
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.4"
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":spring-testcontainers"))
    implementation(project(":modules:postgresql"))
    implementation(libs.testcontainers.jdbc)
    implementation(libs.testcontainers.postgresql)
    implementation(libs.slf4j.api)
    implementation(libs.logback.classic)
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.autoconfigure)
    runtimeOnly("org.postgresql:postgresql")
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.platform.launcher)
    testImplementation(libs.spring.boot.starter.test)
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("org.hng.examples.testcontainers.postgresql.PostgresqlDemoApp")
}
