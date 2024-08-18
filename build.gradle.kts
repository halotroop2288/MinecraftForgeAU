plugins {
	`kotlin-dsl`
	alias(libs.plugins.unimined)
	alias(libs.plugins.spotless)
}

subprojects {
	apply("plugin" to "xyz.wagyourtail.unimined")
	apply("plugin" to "com.diffplug.spotless")

	group = "net.minecraftforge"
	base.archivesName = "forge"
	version = "${rootProject.properties["version"]}+${project.name}"

	unimined.useGlobalCache = false

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

	tasks.withType(Javadoc::class.java).configureEach {
		isFailOnError = false
		exclude("net/minecraftforge/mixin/**.java")
		configurations.all {
			if (isCanBeResolved) classpath += this@all
		}
		sourceSets.all {
			source += allJava
		}
	}

	tasks.withType(ProcessResources::class.java).configureEach {
		inputs.property("version", project.version)

		filesMatching("*.mod.json") {
			expand("version" to project.version)
		}
	}
}
