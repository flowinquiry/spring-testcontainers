plugins {
    id("buildlogic.java-application-conventions")
    alias(libs.plugins.spring.dependency.management)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":spring-testcontainers"))
    implementation(project(":modules:postgresql"))
    implementation(platform(libs.spring.bom))
    implementation(libs.testcontainers.jdbc)
    implementation(libs.testcontainers.postgresql)
    implementation(libs.slf4j.api)
    implementation(libs.logback.classic)

    // Spring Framework dependencies (not Spring Boot)
    implementation("org.springframework:spring-context:6.1.5")
    implementation("org.springframework:spring-orm:6.1.5")
    implementation("org.springframework:spring-jdbc:6.1.5")
    implementation("org.springframework:spring-tx:6.1.5")
    implementation("org.springframework.data:spring-data-jpa:3.2.3")

    // Hibernate and JPA
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

    // Database
    runtimeOnly(libs.postgresql)

    // Test dependencies
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.platform.launcher)
    testImplementation("org.springframework:spring-test")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("io.flowinquiry.testcontainers.examples.postgresql.PostgresqlDemoApp")
}
