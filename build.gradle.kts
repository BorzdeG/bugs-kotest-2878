import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	// see https://youtrack.jetbrains.com/issue/KTIJ-19370
	alias(libs.plugins.kotlin.jvm)
}

repositories {
	mavenCentral()
}

configure<JavaPluginExtension> {
	sourceCompatibility = JavaVersion.toVersion(libs.versions.jvm.get())
}

configurations.all {
	resolutionStrategy {
		failOnVersionConflict()
		failOnDynamicVersions()
		failOnNonReproducibleResolution()

		eachDependency {
			when (requested.group) {
				"org.jetbrains.kotlin" -> useVersion(libs.versions.kotlin.get())
				"io.kotest" -> useVersion(libs.versions.kotest.get())
			}
		}
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf(
			"-Xjsr305=strict",
			"-progressive",
			"-Xskip-prerelease-check",
			"-opt-in=io.kotest.common.ExperimentalKotest"
		)
		jvmTarget = libs.versions.jvm.get()
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
	failFast = true
	filter {
		isFailOnNoMatchingTests = false
	}

	testLogging {
		showStandardStreams = true
		showExceptions = true
		exceptionFormat = FULL
		events(FAILED, PASSED, SKIPPED)
	}
}

dependencies {
	api(libs.bundles.test.base)
}
