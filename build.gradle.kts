plugins {
    id ("base")
    alias(libs.plugins.spotless) apply false
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
        plugins.withId("java-library") {
            apply(plugin = "maven-publish")
            group = "io.flowinquiry.testcontainers"

            extensions.configure<PublishingExtension>("publishing") {
                publications {
                    create<MavenPublication>("mavenJava") {
                        from(components["java"])
                    }
                }

                repositories {
                    mavenLocal()
                }
            }
        }
    }
}
