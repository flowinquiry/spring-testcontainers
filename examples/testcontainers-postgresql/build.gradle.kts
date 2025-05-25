plugins {
    id("java-library")
    id("application")
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
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("org.hng.examples.testcontainers.postgresql.PostgresqlDemoApp")
}
