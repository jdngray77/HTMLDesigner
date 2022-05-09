import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    application
}


group = "me.gordie"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jcodec:jcodec:0.2.3")
}


tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "14"
}

application {
    mainClass.set("MainKt")
}