plugins {
    id("buildlogic.java-application-conventions")
    alias(libs.plugins.spring.dependency.management)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":spring-testcontainers"))
    implementation(project(":modules:kafka"))
    implementation(platform(libs.spring.bom))
    implementation(platform(libs.spring.boot.bom))
    implementation(libs.slf4j.api)
    implementation(libs.logback.classic)
    implementation(libs.spring.boot.starter)
    implementation("org.springframework.kafka:spring-kafka")
    implementation(libs.spring.boot.autoconfigure)
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.platform.launcher)
    testImplementation(libs.spring.boot.starter.test)
    testImplementation("org.springframework.kafka:spring-kafka-test")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("io.flowinquiry.testcontainers.examples.kafka.KafkaDemoApp")
}
