import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"

    id("org.openjfx.javafxplugin") version "0.0.13"
    id("java")
    id("distribution")
    application
}

java {
    sourceCompatibility = JavaVersion.VERSION_13
    targetCompatibility = JavaVersion.VERSION_13
    withSourcesJar()
}

group = "com.shinkson47"
version = "BETA-1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")

    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-test")
    implementation("org.controlsfx:controlsfx:11.1.1")
    implementation("org.jetbrains.kotlin:kotlin-serialization:1.7.0")
    implementation("org.jfxtras:jmetro:11.6.15")
    implementation("org.jsoup:jsoup:1.14.3")
    implementation("net.sourceforge.cssparser:cssparser:0.9.29")
    implementation("com.vladsch.javafx-webview-debugger:javafx-webview-debugger:0.7.6")
}

javafx {
    modules("javafx.controls", "javafx.fxml", "javafx.web", "javafx.swing")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
}

application {
    mainClass.set("HTMLDesignerKt")
}