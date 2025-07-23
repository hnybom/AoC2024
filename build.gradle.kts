plugins {
    kotlin("jvm") version "2.0.20"
    application
}

group = "fi.solita.hnybom"
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
    jvmToolchain(11)
}

// Create tasks to run each day
for (day in 1..6) {
    tasks.register<JavaExec>("runDay$day") {
        group = "application"
        description = "Run Day $day solution"
        classpath = sourceSets.main.get().runtimeClasspath
        mainClass.set("fi.solita.hnybom.aoc2024.Day${day}Kt")
        args = listOf()
    }
}

// Configure default application main class
tasks.named<JavaExec>("run") {
    mainClass.set("fi.solita.hnybom.aoc2024.Day1Kt")
}
