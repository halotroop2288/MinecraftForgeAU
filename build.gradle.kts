import xyz.wagyourtail.unimined.api.minecraft.task.RemapJarTask

plugins {
	alias(libs.plugins.unimined)
	alias(libs.plugins.spotless)
	`maven-publish`
}

tasks.jar {
	enabled = false
}

subprojects {
	apply("plugin" to "xyz.wagyourtail.unimined")
	apply("plugin" to "com.diffplug.spotless")
	apply("plugin" to "maven-publish")

	group = "risugami"
	base.archivesName = "ModLoader"
	version = "${rootProject.properties["version"]}+${project.name}"

	unimined.useGlobalCache = false

	repositories {
		unimined.wagYourMaven("releases")
		unimined.modrinthMaven()
		unimined.jitpack()
	}

	java {
		toolchain.languageVersion = JavaLanguageVersion.of(21)
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8

		withJavadocJar()
		withSourcesJar()
	}

	spotless {
		java {
			importOrder("modloader|risugami.modloader", "", "\\#modloader|risugami.modloader", "", "java|javax", "\\#")
			licenseHeaderFile(File(rootProject.rootDir, "JAVA_HEADER"))
		}
	}

	tasks.withType(JavaCompile::class.java).configureEach {
		sourceCompatibility = "8"
		targetCompatibility = "8"
	}

	tasks.withType(Javadoc::class.java).configureEach {
		(options as StandardJavadocDocletOptions)
			.linkSource()
			.docTitle("${rootProject.name} ${rootProject.properties["version"]} API")
			.addFileOption("-add-stylesheet", File(rootProject.rootDir, "javadoc.css"))
		isFailOnError = false
		exclude("risugami/modloader/mixin/**.java")
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

	publishing {
		publications {
			create<MavenPublication>("maven") {
				from(components["java"])
				groupId = "${project.group}"
				artifactId = project.base.archivesName.get()
				version = "${project.version}"

				tasks.withType(RemapJarTask::class.java).forEach {
					artifact(it) {
						builtBy(it)
					}
				}
			}
		}
	}
}
