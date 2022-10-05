import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties
import java.io.FileOutputStream

plugins {
    kotlin("jvm") version "1.7.10"
    id("java")
    id("distribution")
    id("org.openjfx.javafxplugin") version "0.0.13"

    `maven-publish`

    // gets git version at build time for use at runtime.
    id("com.palantir.git-version") version "0.15.0"

    // Generates documentation from docstrings
    id("org.jetbrains.dokka") version "1.7.10"

    // Generates a report of code coverage of tests.
    id("jacoco")


    // Plugin used to generate a report of every report used by the IDE.
    // For some reason, it interferes with the spdx library, so it's commented out.
    // Just uncomment it if you want to use it.

    // NOTE : REMEMBER TO UNCOMMENT THE DNDTABPANE LIBRARY TO INCLUDE IT IN THE REPORT.
    // id("io.cloudflight.license-gradle-plugin") version "1.0.3"
    application
}




val versionDetails: groovy.lang.Closure<com.palantir.gradle.gitversion.VersionDetails> by extra


java {
    sourceCompatibility = JavaVersion.VERSION_13
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
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
    implementation("org.reflections:reflections:0.10.2")

    // Make the ide look not shit
    implementation("org.jfxtras:jmetro:11.6.16")

    // HMTL dom management
    implementation("org.jsoup:jsoup:1.15.3")

    // CSS validation
    implementation("net.sourceforge.cssparser:cssparser:0.9.29")

    // Fetches licences for dependencies
    implementation("org.spdx:spdx-tools:2.2.8")

    // Spotify integration - for fun.
    implementation("se.michaelthelin.spotify:spotify-web-api-java:7.2.0")

// Included only for license. This library is imported as source for custom modification
//    implementation("com.sibvisions.external.jvxfx:dndtabpane:0.1")

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

tasks.build { dependsOn("generateProperties") }

tasks.withType<JavaExec>{
    dependsOn("generateProperties")
}

application {
    mainClass.set("HTMLDesignerKt")
    applicationDefaultJvmArgs = listOf("--add-opens", "javafx.controls/javafx.scene.control.skin=ALL-UNNAMED")
}

/*
 *   Properties file for the application to read
 */

tasks.register("generateProperties") {
    doLast {
        val propertiesFile = file("$buildDir/resources/main/com/jdngray77/htmldesigner/frontend/meta.properties")
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


tasks.jar {
    dependsOn("generateProperties")
    manifest.attributes["Main-Class"] = "HTMLDesignerKt"
    manifest.attributes["Class-Path"] = configurations
//        .runtimeClasspath
//        .get()
//        .joinToString(separator = " ") { file ->
//            "libs/${file.name}"
//        }
}

tasks.register("testAndReport") {
    dependsOn("test", "jacocoTestReport")
}
