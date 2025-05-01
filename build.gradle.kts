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
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("com.google.truth:truth:1.4.2")

    // Koin for Dependency Injection
    implementation("io.insert-koin:koin-core:4.0.2")

    // JUnit for Testing
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")

    testImplementation("io.mockk:mockk:1.14.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")

    // kotest, assertion
    testImplementation("io.kotest:kotest-runner-junit5:5.7.2")
    testImplementation("io.kotest:kotest-assertions-core:5.7.2")
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
                    exclude("**/entity/**", "**/di/**", "**/exceptions/**", "**/presentation/**")

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
                minimum = "0.75".toBigDecimal() // 100% coverage required
            }
        }
    }

    classDirectories.setFrom(
        files(
            fileTree("build/classes/kotlin/main") {
                exclude("**/entity/**", "**/di/**", "**/exceptions/**")
            }
        )
    )
}

tasks.check {
    dependsOn(tasks.jacocoTestReport)
    dependsOn(tasks.jacocoTestCoverageVerification)
}