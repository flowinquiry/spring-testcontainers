plugins {
    id("java-library")
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":spring-testcontainers"))
    implementation(libs.testcontainers.jdbc)
    implementation(libs.testcontainers.postgresql)
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()
}
