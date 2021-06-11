import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.32"
}

group = "me.qaz"
version = "0.1.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("org.tomlj:tomlj:1.0.0")
    implementation(files(
        "/home/qaz/Projects/Programming/Neatlin/build/libs/Neatlin-jvm-0.2.jar",
        "/home/qaz/Projects/Programming/SemVer-KMP/build/libs/SemVer-KMP-jvm-0.2.1.jar"
    ))
    testImplementation(kotlin("test"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}