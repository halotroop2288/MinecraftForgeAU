@file:Suppress("UnstableApiUsage")

import xyz.wagyourtail.unimined.util.sourceSets
import xyz.wagyourtail.unimined.util.withSourceSet

val main: SourceSet by sourceSets.main
val client: SourceSet by sourceSets.creating
val server: SourceSet by sourceSets.creating

val commonAW = main.resources.first { it.name.equals("modloader.accesswidener") }

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

	dependencies {
		implementation("com.github.thecatcore.CursedMixinExtensions:fabric:1.0.0") {
			exclude(module = "fabric-loader")
		}
		implementation(annotationProcessor("com.github.bawnorton.mixinsquared:mixinsquared-fabric:0.2.0")!!)
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
		stubs("official", "calamus") {
			c("BaseMod", "modloader/BaseMod")
			c("EntityRendererProxy", "modloader/EntityRendererProxy")
			c("MLProp", "modloader/MLProp")
			c("ModLoader", "modloader/ModLoader")
			c("ModTextureAnimation", "modloader/ModTextureAnimation")
			c("ModTextureStatic", "modloader/ModTextureStatic")
		}
	}

	jarMod()

	dependencies {
		val jarModConfiguration = configurations.named("jarMod".withSourceSet(sourceSet))
		if (sourceSet == client) jarModConfiguration("risugami:modloader:b1.7.3")
//		jarModConfiguration("modloadermp:modloadermp:b1.7.3:${sourceSet.name}")
	}

	defaultRemapJar = false
	defaultRemapSourcesJar = false
}

configurations.all {
	resolutionStrategy.eachDependency {
		if (requested.group.startsWith("org.lwjgl")) useVersion("2.9.4+legacyfabric.8")
	}
}
