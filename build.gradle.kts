import org.jreleaser.model.Active
import org.jreleaser.model.Signing

plugins {
    id ("base")
    alias(libs.plugins.spotless) apply false
    id("org.jreleaser") version "1.18.0"
}

group = "io.flowinquiry"

subprojects {
    apply(plugin = "com.diffplug.spotless")


    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        java {
            target(fileTree(projectDir) {
                include("**/*.java")
                exclude("**/build/**")
            })
            googleJavaFormat()
            removeUnusedImports()
        }

        kotlinGradle {
            target(fileTree(projectDir) {
                include("**/*.gradle.kts")
                exclude("**/build/**")
            })
            ktlint()
        }
    }

    if (!project.path.startsWith(":examples")) {
        plugins.withId("buildlogic.java-library-conventions") {
            apply(plugin = "maven-publish")
            group = "io.flowinquiry.testcontainers"

            // Create a sources JAR
            val sourcesJar by tasks.registering(Jar::class) {
                archiveClassifier.set("sources")
                from(project.the<SourceSetContainer>()["main"].allSource)
            }

            extensions.configure<PublishingExtension>("publishing") {
                publications {
                    create<MavenPublication>("mavenJava") {
                        from(components["java"])
                        artifact(sourcesJar.get())

                        pom {
                            name.set("Spring Test Containers")
                            description.set("Designed to make writing integration tests with Testcontainers in Java effortless and extensible")
                            url.set("https://github.com/flowinquiry/spring-testcontainers")
                            inceptionYear.set("2025")

                            licenses {
                                license {
                                    name.set("MIT License")
                                    url.set("https://raw.githubusercontent.com/flowinquiry/spring-testcontainers/refs/heads/main/LICENSE")
                                }
                            }

                            developers {
                                developer {
                                    id.set("flowinquiry")
                                    name.set("FlowInquiry")
                                }
                            }

                            scm {
                                connection.set("scm:git@github.com:flowinquiry/spring-testcontainers.git")
                                developerConnection.set("scm:git:ssh://github.com:flowinquiry/spring-testcontainers.git")
                                url.set("https://github.com/flowinquiry/spring-testcontainers")
                            }
                        }
                    }
                }

                repositories {
                    mavenLocal()
                    maven {
                        url = layout.buildDirectory.dir("staging-deploy").get().asFile.toURI()
                    }
                }
            }
        }
    }
}

jreleaser {
    signing {
        active.set(Active.ALWAYS)
        armored.set(true)
    }
    deploy {
        maven {
            mavenCentral {
                register("central") {
                    active.set(Active.ALWAYS)
                    url.set("https://central.sonatype.com/api/v1/publisher")
                    stagingRepositories.set(listOf("build/staging-deploy"))
                }
            }
        }
    }
}
