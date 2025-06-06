/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    id("buildlogic.java-library-conventions")
}

dependencies {
    implementation(platform(libs.junit.bom))
    implementation(platform(libs.spring.bom))
    implementation(libs.junit.jupiter.api)
    implementation(libs.junit.jupiter.engine)
    implementation(libs.junit.platform.launcher)
    implementation(libs.testcontainers.jdbc)
    implementation(libs.spring.test)
    implementation(libs.spring.context)
}
