@file:Suppress("UnstableApiUsage")

import xyz.wagyourtail.unimined.api.minecraft.patch.fabric.LegacyFabricPatcher
import xyz.wagyourtail.unimined.util.sourceSets
import xyz.wagyourtail.unimined.util.withSourceSet

val main: SourceSet by sourceSets.named("main")
val client: SourceSet by sourceSets.creating
val server: SourceSet by sourceSets.creating

val commonAW = main.resources.find {
	it.name.equals("forge.common.accesswidener")
}!!

val fabricConfig: LegacyFabricPatcher.() -> Unit = {
	loader(libs.versions.fabric.get())
	customIntermediaries = true
	prodNamespace("babricIntermediary")
}

unimined.minecraft {
	version("b1.7.3")
	side("server")
	mappings {
		calamus()
		babricIntermediary()
		retroMCP("b1.7")
	}
	ornitheFabric {
		fabricConfig.invoke(this)
		accessWidener(commonAW)
	}
	runs.off = true
	defaultRemapJar = false
}

unimined.minecraft(client, server) {
	combineWith(main)
	side(sourceSet.name)
	ornitheFabric {
		fabricConfig.invoke(this@ornitheFabric)
		accessWidener(
			mergeAws(
				File(sourceSet.output.resourcesDir, "forge.accesswidener"),
				listOf(
					commonAW, sourceSet.resources.find {
						it.name.equals("forge.${sourceSet.name}.accesswidener")
					}!!
				)
			)
		)
	}
	runs.off = false
	defaultRemapJar = true
	project.afterEvaluate {
		val jarTaskName = "jar".withSourceSet(sourceSet)
		val defaultJarTask = tasks.named(jarTaskName).get()
		val baseName = "remapJarTo".withSourceSet(sourceSet)
		remap(defaultJarTask, "${baseName}Official") {
			asJar.archiveClassifier = "${sourceSet.name}-official"
			prodNamespace("official")
		}
		remap(defaultJarTask, "${baseName}Babric") {
			asJar.archiveClassifier = "${sourceSet.name}-babric"
			prodNamespace("babricIntermediary")
		}
		remap(defaultJarTask, "${baseName}Calamus") {
			asJar.archiveClassifier = "${sourceSet.name}-calamus"
			prodNamespace("calamus")
		}
	}
}

tasks.build.configure {
	for (set in arrayOf(client, server)) {
		val baseName = "remapJarTo".withSourceSet(set)
		dependsOn(
			"${baseName}Babric",
			"${baseName}Calamus"
		)
	}
}
