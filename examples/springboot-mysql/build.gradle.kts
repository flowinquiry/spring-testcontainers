plugins {
    id("buildlogic.java-application-conventions")
    alias(libs.plugins.spring.dependency.management)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":spring-testcontainers"))
    implementation(project(":modules:mysql"))
    implementation(platform(libs.spring.bom))
    implementation(platform(libs.spring.boot.bom))
    implementation(libs.testcontainers.jdbc)
    implementation(libs.testcontainers.mysql)
    implementation(libs.slf4j.api)
    implementation(libs.logback.classic)
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.autoconfigure)
    runtimeOnly("mysql:mysql-connector-java:8.0.33")
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.platform.launcher)
    testImplementation(libs.spring.boot.starter.test)
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("io.flowinquiry.testcontainers.examples.mysql.MySqlDemoApp")
}
