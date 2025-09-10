@file:Suppress("UnstableApiUsage")

import xyz.wagyourtail.unimined.util.sourceSets
import xyz.wagyourtail.unimined.util.withSourceSet

val main: SourceSet by sourceSets.main
val client: SourceSet by sourceSets.creating
val server: SourceSet by sourceSets.creating

val commonAW = main.resources.first { it.name.equals("forge.common.accesswidener") }

unimined.minecraft {
	version("${project.properties["minecraft_version"]}")
	mappings {
		ornitheGenVersion = 2
		calamus()
		babricIntermediary()
		feather("${project.properties["mappings_version"]}")
	}
	ornitheFabric {
		loader(libs.versions.fabric.get())
		customIntermediaries = true
		prodNamespace("calamus")
		accessWidener(commonAW)
	}

	project.afterEvaluate {
		val defaultJarTask = tasks.jar.get()
		defaultJarTask.archiveClassifier = "dev"
		val buildTask by tasks.build
		buildTask.dependsOn(
			remap(defaultJarTask, "calamusJar") {
				asJar.archiveClassifier = "calamusG2"
				prodNamespace("calamus")
			},
			remap(defaultJarTask, "babricJar") {
			asJar.archiveClassifier = "babric"
			prodNamespace("babricIntermediary")
		})
	}

	defaultRemapJar = false
}

unimined.minecraft(client, server) {
	version("${project.properties["minecraft_version"]}")
	side(sourceSet.name)

	mappings {
		ornitheGenVersion = 2
		calamus()
		feather(4)
	}

	jarMod()

	dependencies {
		val jarModConfiguration = configurations.named("jarMod".withSourceSet(sourceSet))
		if (sourceSet == client) jarModConfiguration("risugami:modloader:b1.7.3")
		jarModConfiguration("modloadermp:modloadermp:b1.7.3:${sourceSet.name}")
		jarModConfiguration("net.minecraftforge:forge:b1.7.3-1.0.7:${sourceSet.name}@zip")
	}

	defaultRemapJar = false
	defaultRemapSourcesJar = false
}

configurations.all {
	resolutionStrategy.eachDependency {
		if (requested.group.startsWith("org.lwjgl")) useVersion("2.9.4+legacyfabric.8")
	}
}
