plugins {
    id("org.springframework.boot") version "2.5.6"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.5.31"
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.spring") version "1.5.31"
    id("org.sonarqube" ) version "3.0"
    id( "io.gitlab.arturbosch.detekt") version "1.18.1"
}
group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:2.5.6")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:2.5.6")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    source = files("$projectDir")
    config = files("$projectDir/src/main/resources/detekt-config.yml")
}
