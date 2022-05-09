import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.30"
    kotlin("plugin.serialization") version "1.5.30"
    application
}





dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0-RC")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.2.1") // Infant library, but supposedly compatable with serialization. Fuck using java.util.date with kotlinx.
    implementation("aat:ascii-art-table-0.0.1:0.0.1")
    implementation("org.apache.commons:commons-lang3:3.0")
}

group = "me.gordie"
version = "1.0-SNAPSHOT"



repositories {
    mavenCentral()
    flatDir {
        dirs("libs")
    }
}



tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClassName = "BJ"
}