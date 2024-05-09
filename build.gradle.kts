plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.23"
    id("org.jetbrains.kotlin.kapt") version "1.9.23"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.9.23"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.application") version "4.3.8"
}

version = "0.1"
group = "cli.app.two"

val kotlinVersion = project.properties.get("kotlinVersion")
repositories {
    mavenCentral()
}

dependencies {
    kapt("info.picocli:picocli-codegen")
    kapt("io.micronaut.serde:micronaut-serde-processor")
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.9.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("info.picocli:picocli")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.picocli:micronaut-picocli")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

    testImplementation("io.kotest:kotest-assertions-core-jvm:5.8.1")
    testImplementation("io.kotest:kotest-assertions-json:5.8.1")
}

application {
    mainClass = "cli.app.two.CliApp"
}

tasks.withType(JavaExec::class) {
    // Run with these args by default
    args = listOf("-v")
}

java {
    sourceCompatibility = JavaVersion.toVersion("17")
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

micronaut {
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("cli.app.two.*")
    }
}
