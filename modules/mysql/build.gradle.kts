plugins {
    id("buildlogic.java-library-conventions")
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":spring-testcontainers"))
    implementation(platform(libs.spring.bom))
    implementation(libs.testcontainers.jdbc)
    implementation(libs.testcontainers.mysql)
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()
}
