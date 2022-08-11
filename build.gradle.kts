import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties
import java.io.FileOutputStream

plugins {
    kotlin("jvm") version "1.7.10"

    id("com.palantir.git-version") version "0.15.0"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.jetbrains.dokka") version "1.7.10"
    id("java")
    id("distribution")
    application
}

val versionDetails: groovy.lang.Closure<com.palantir.gradle.gitversion.VersionDetails> by extra


java {
    sourceCompatibility = JavaVersion.VERSION_13
    targetCompatibility = JavaVersion.VERSION_13
    withSourcesJar()
}

group = "com.shinkson47"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    implementation("org.junit:junit4-runner:5.0.0-ALPHA")

    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-test")
    implementation("org.controlsfx:controlsfx:11.1.1")
    implementation("org.jetbrains.kotlin:kotlin-serialization:1.7.10")
    implementation("org.jfxtras:jmetro:11.6.15")
    implementation("org.jsoup:jsoup:1.15.2")
    implementation("net.sourceforge.cssparser:cssparser:0.9.29")
    implementation("org.reflections:reflections:0.10.2")
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

tasks.build {
    dependsOn("generateProperties")
}

tasks.withType<JavaExec>{
    dependsOn("generateProperties")
}

application {
    mainClass.set("HTMLDesignerKt")
    applicationDefaultJvmArgs = listOf("-ea")
    applicationDefaultJvmArgs
}

/*
 *   Properties file for the application to read
 */

tasks.register("generateProperties") {
    doLast {
        val propertiesFile = file("$buildDir/resources/main/com/jdngray77/htmldesigner/meta.properties")
        propertiesFile.parentFile.mkdirs()

        val properties = Properties()



        val details = versionDetails()
        //        properties.setProperty("lastHash", gitVersion())
        //        properties.setProperty("commitDistance", details.commitDistance.toString())
        //        properties.setProperty("isCleanTag", details.isCleanTag.toString())
        properties.setProperty("gitHash", details.lastTag)
        properties.setProperty("gitHashFull", details.gitHashFull)
        properties.setProperty("branchName", details.branchName)



        properties.store(FileOutputStream(propertiesFile), null)
    }
}


