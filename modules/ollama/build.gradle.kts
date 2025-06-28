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
}

tasks.test {
    useJUnitPlatform()
}
