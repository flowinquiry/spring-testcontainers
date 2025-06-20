plugins {
    id("buildlogic.java-library-conventions")
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":spring-testcontainers"))
    implementation(platform(libs.spring.bom))
    implementation(libs.testcontainers.ollama)
    implementation(libs.spring.context)
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()
}
