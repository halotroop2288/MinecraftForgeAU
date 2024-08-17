plugins {
	`kotlin-dsl`
	alias(libs.plugins.unimined)
	alias(libs.plugins.spotless)
}

subprojects {
	apply("plugin" to "xyz.wagyourtail.unimined")
	apply("plugin" to "com.diffplug.spotless")

	java {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8

		withJavadocJar()
		withSourcesJar()
	}

	spotless {
		java {
			licenseHeaderFile(File(rootProject.rootDir, "JAVA_HEADER"))
		}
	}

	tasks.withType(JavaCompile::class.java).configureEach {
		sourceCompatibility = "8"
		targetCompatibility = "8"
	}

	tasks.withType(ProcessResources::class.java).configureEach {
		inputs.property ("version", project.version)

		filesMatching("*.mod.json") {
			expand ("version" to project.version)
		}
	}

	unimined.useGlobalCache = false
}
