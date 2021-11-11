/**
 * 🍣 sushi: simple github to youtrack proxy.
 * Copyright (c) 2021 Noelware <cutie@floofy.dev>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.util.concurrent.TimeUnit
import java.text.SimpleDateFormat
import java.util.Date
import java.io.File

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.0"
    kotlin("plugin.serialization") version "1.5.31"
    id("com.diffplug.spotless") version "5.16.0"
    kotlin("jvm") version "1.5.31"
    application
}

val current = Version(1, 0, 0)
group = "gay.floof"
version = current.string()

repositories {
    mavenCentral()
    mavenLocal()
    jcenter()
}

dependencies {
    // Kotlin libraries
    implementation(kotlin("stdlib"))

    // kotlinx libraries
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
    api("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.0")

    // ktor
    implementation("io.ktor:ktor-serialization:1.6.4")
    implementation("io.ktor:ktor-server-netty:1.6.4")

    // Logging (SLF4J + Logback)
    implementation("ch.qos.logback:logback-classic:1.2.6")
    implementation("ch.qos.logback:logback-core:1.2.6")
    api("org.slf4j:slf4j-api:1.7.32")

    // sha1 utilities
    implementation("org.bouncycastle:bcprov-jdk15on:1.69")
    implementation("org.bouncycastle:bcpkix-jdk15on:1.69")
}

spotless {
    kotlin {
        trimTrailingWhitespace()
        licenseHeaderFile("${rootProject.projectDir}/assets/HEADING")
        endWithNewline()

        // We can't use the .editorconfig file, so we'll have to specify it here
        // issue: https://github.com/diffplug/spotless/issues/142
        // ktlint 0.35.0 (default for Spotless) doesn't support trailing commas
        ktlint("0.43.0")
            .userData(mapOf(
                "no-consecutive-blank-lines" to "true",
                "no-unit-return" to "true",
                "disabled_rules" to "no-wildcard-imports,colon-spacing",
                "indent_size" to "4"
            ))
    }
}

application {
    mainClass.set("gay.floof.sushi.Bootstrap")
    java {
        sourceCompatibility = JavaVersion.VERSION_16
        targetCompatibility = JavaVersion.VERSION_16
    }
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
        kotlinOptions.javaParameters = true
        kotlinOptions.freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.RequiresOptIn"
        )
    }

    named<ShadowJar>("shadowJar") {
        archiveFileName.set("sushi.jar")
        mergeServiceFiles()

        manifest {
            attributes(mapOf(
                "Manifest-Version" to "1.0.0",
                "Main-Class" to "gay.floof.sushi.Bootstrap"
            ))
        }
    }

    build {
        dependsOn("generateMetadata")
        dependsOn(spotlessApply)
        dependsOn(shadowJar)
    }
}

tasks.register("generateMetadata") {
    val path = sourceSets["main"].resources.srcDirs.first()
    if (!file(path).exists()) path.mkdirs()

    val date = Date()
    val formatter = SimpleDateFormat("MMM dd, yyyy HH:mm:ss")
    file("$path/build-info.properties").writeText("""app.build.date = ${formatter.format(date)}
app.version = ${current.string()}
app.commit  = ${current.commit()}
    """.trimIndent())
}

class Version(
    private val major: Int,
    private val minor: Int,
    private val patch: Int
) {
    fun string(): String = "$major.$minor.$patch"
    fun commit(): String = execShell("git rev-parse HEAD")
}

fun execShell(command: String): String {
    val parts = command.split("\\s".toRegex())
    val process = ProcessBuilder(*parts.toTypedArray())
        .directory(File("."))
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()

    process.waitFor(1, TimeUnit.MINUTES)
    return process.inputStream.bufferedReader().readText()
        .trim()
        .slice(0..8)
}
