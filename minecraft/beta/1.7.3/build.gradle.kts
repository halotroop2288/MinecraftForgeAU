@file:Suppress("UnstableApiUsage")

import xyz.wagyourtail.unimined.api.minecraft.MinecraftConfig
import xyz.wagyourtail.unimined.api.minecraft.patch.fabric.LegacyFabricPatcher

val main: SourceSet by sourceSets.named("main")
val client: SourceSet by sourceSets.creating
val server: SourceSet by sourceSets.creating

val commonAW = File(projectDir, "src/main/resources/forge.common.accesswidener")
val clientAW = File(projectDir, "src/client/resources/forge.client.accesswidener")
val serverAW = File(projectDir, "src/server/resources/forge.server.accesswidener")

val fabricConfig: LegacyFabricPatcher.() -> Unit = {
	loader(libs.versions.fabric.get())
	customIntermediaries = true
	prodNamespace("babricIntermediary")
}

unimined.minecraft {
    version("b1.7.3")
    side("server")
    mappings {
		babricIntermediary()
        retroMCP("b1.7")
    }
    legacyFabric {
        loader("0.16.0")
        prodNamespace("official")
        customIntermediaries = true

        accessWidener(File(projectDir, "src/client/resources/forge.client.accesswidener"))
    }
    runs.off = true
}

val sidedMinecraftConfig: MinecraftConfig.(Boolean) -> Unit = { client ->
	combineWith(main)
	side(if (client) "client" else "server")
	legacyFabric {
		fabricConfig.invoke(this@legacyFabric)
		accessWidener(
			mergeAws(File(sourceSet.output.resourcesDir, "forge.accesswidener"),
				listOf(
					commonAW, if (client) clientAW else serverAW
				)
			)
		)
	}
	runs.off = false
	defaultRemapJar = true
}

unimined.minecraft(client) {
	sidedMinecraftConfig.invoke(this@minecraft, true)
}

unimined.minecraft(server) {
	sidedMinecraftConfig.invoke(this@minecraft, false)
}
