@file:Suppress("UNUSED_VARIABLE")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun isNonStable(version: String): Boolean {
	return listOf("alpha", "dev", "rc", "m1").any { version.toLowerCase().contains(it) }
}

plugins {
	kotlin("jvm") version "1.5.31"
	kotlin("plugin.spring") version "1.5.31"
	kotlin("plugin.jpa") version "1.5.31"
	id("org.springframework.boot") version "2.5.6"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("com.bmuschko.docker-spring-boot-application") version "7.1.0"
	id("com.avast.gradle.docker-compose") version "0.14.9"
	id("se.patrikerdes.use-latest-versions") version "0.2.18"
	id("com.github.ben-manes.versions") version "0.39.0"
	id("com.adarshr.test-logger") version "3.1.0"
}

group = "de.stefanbissell.dbmig"
version = "1.0"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.flywaydb:flyway-core:8.0.3")
	implementation("io.github.microutils:kotlin-logging:2.0.11")

	runtimeOnly("org.postgresql:postgresql:42.3.1")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

docker {
	springBootApplication {
		images.set(listOf("db-migration-demo:1.0"))
		baseImage.set("openjdk:11")
	}
}

tasks {
	withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "11"
		}
	}

	withType<Test> {
		useJUnitPlatform()
	}

	val waitServer by creating {
		dependsOn(dockerBuildImage)
		dependsOn(composeUp)
	}

	dependencyUpdates {
		rejectVersionIf {
			isNonStable(candidate.version)
		}
	}
}
