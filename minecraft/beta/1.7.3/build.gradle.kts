@file:Suppress("UnstableApiUsage")

import xyz.wagyourtail.unimined.api.minecraft.patch.fabric.LegacyFabricPatcher
import xyz.wagyourtail.unimined.api.minecraft.task.RemapJarTask
import xyz.wagyourtail.unimined.util.capitalized
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
	prodNamespace("official")
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
		tasks.named("remap" + jarTaskName.capitalized(), RemapJarTask::class.java).configure {
			archiveClassifier = "${sourceSet.name}-official"
		}
		val baseName = "remapJarTo".withSourceSet(sourceSet)
		remap(defaultJarTask, "${baseName}Babric") {
			archiveClassifier = "${sourceSet.name}-babric"
			prodNamespace("babricIntermediary")
		}
		remap(defaultJarTask, "${baseName}Calamus") {
			archiveClassifier = "${sourceSet.name}-calamus"
			prodNamespace("calamus")
		}
	}
}

tasks.build.configure {
	for (set in arrayOf(client, server)) {
		dependsOn(
			"remapJarToBabric".withSourceSet(set),
			"remapJarToCalamus".withSourceSet(set)
		)
	}
}
