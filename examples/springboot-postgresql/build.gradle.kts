plugins {
    id("buildlogic.java-application-conventions")
    alias(libs.plugins.spring.dependency.management)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":spring-testcontainers"))
    implementation(project(":modules:postgresql"))
    implementation(platform(libs.spring.bom))
    implementation(platform(libs.spring.boot.bom))
    implementation(libs.slf4j.api)
    implementation(libs.logback.classic)
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.autoconfigure)
    runtimeOnly(libs.postgresql)
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
