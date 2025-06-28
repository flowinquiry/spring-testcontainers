plugins {
    id("buildlogic.java-library-conventions")
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":modules:jdbc"))
    implementation(platform(libs.spring.bom))
    implementation(libs.testcontainers.jdbc)
    implementation(libs.testcontainers.mysql)
}

tasks.test {
    useJUnitPlatform()
}
