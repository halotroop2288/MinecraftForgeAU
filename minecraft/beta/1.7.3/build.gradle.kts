@file:Suppress("UnstableApiUsage")

import xyz.wagyourtail.unimined.api.mapping.MappingsConfig
import xyz.wagyourtail.unimined.util.sourceSets

val main: SourceSet by sourceSets.main

val mappingsConfigAction: MappingsConfig<*>.() -> Unit = {
	ornitheGenVersion = 2
	calamus()
	babricIntermediary()
	feather("${project.properties["mappings_version"]}")
	stubs(namespaces = arrayOf("official", "calamus")) {
		c("BaseMod", "risugami/modloader/BaseMod")
		c("EntityRendererProxy", "risugami/modloader/EntityRendererProxy")
		c("ModLoader", "risugami/modloader/ModLoader")
		c("MLProp", "risugami/modloader/MLProp")
		c("ModTextureAnimation", "risugami/modloader/ModTextureAnimation")
		c("ModTextureStatic", "risugami/modloader/ModTextureStatic")
	}
}

unimined.minecraft {
	version("${project.properties["minecraft_version"]}")
	mappings(mappingsConfigAction)
	ornitheFabric {
		loader(libs.versions.fabric.get())
		customIntermediaries = true
		prodNamespace("calamus")
		accessWidener(main.resources.first { it.name.equals("modloader.accesswidener") })
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
			}
		)
	}

	dependencies {
		implementation("com.github.thecatcore.CursedMixinExtensions:fabric:1.0.0") {
			exclude(module = "fabric-loader")
		}
		implementation(annotationProcessor("com.github.bawnorton.mixinsquared:mixinsquared-fabric:0.2.0")!!)
	}

	defaultRemapJar = false
}

unimined.minecraft(sourceSets.test.get()) {
	version("b1.7.3")
	side("client")
	mappings(mappingsConfigAction)
	jarMod()

	defaultRemapJar = false

	dependencies {
		"testJarMod"("risugami:modloader:b1.7.3")
	}
}

configurations.all {
	resolutionStrategy.eachDependency {
		if (requested.group.startsWith("org.lwjgl")) useVersion("2.9.4+legacyfabric.8")
	}
}
