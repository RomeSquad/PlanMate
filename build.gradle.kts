plugins {
    kotlin("jvm") version "2.1.0"
    jacoco
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

jacoco {
    toolVersion = "0.8.10"
}


tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        html.required.set(true)
    }

    classDirectories.setFrom(
        files(
            classDirectories.files.map {
                fileTree(it) {
                    exclude("**/entity/**", "**/di/**","**/utils/**")

                }
            }
        )
    )
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.test)

    violationRules {
        rule {
            limit {
                minimum = "0.90".toBigDecimal() // 100% coverage required
            }
        }
    }

    classDirectories.setFrom(
        files(
            fileTree("build/classes/kotlin/main") {
                exclude("**/entity/**", "**/di/**","**/utils/**")
            }
        )
    )
}

tasks.check {
    dependsOn(tasks.jacocoTestReport)
    dependsOn(tasks.jacocoTestCoverageVerification)
}