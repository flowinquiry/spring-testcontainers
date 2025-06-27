plugins {
    id("buildlogic.java-library-conventions")
}

dependencies {
    api(platform(libs.junit.bom))
    api(platform(libs.spring.bom))
    api(libs.junit.jupiter.api)
    api(libs.junit.jupiter.engine)
    api(libs.junit.platform.launcher)
    api(libs.spring.test)
    api(libs.spring.context)
    api(libs.slf4j.api)
    api(libs.testcontainers)
}
