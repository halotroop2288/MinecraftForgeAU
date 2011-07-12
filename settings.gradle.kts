pluginManagement {
    repositories {
        mavenCentral {
            name = "Maven Central"
        }
        gradlePluginPortal {
            name = "Gradle Plugin Portal"
        }
        maven("https://maven.wagyourtail.xyz/releases") {
            name = "WagYourTail Releases"
        }
        maven("https://maven.wagyourtail.xyz/snapshots") {
            name = "WagYourTail Snapshots"
        }
    }
}

includeBuild("../unimined")

rootProject.name = "Minecraft Forge"

fun createProject(name: String, directory: String) {
    // Replace illegal characters
    val id = name
        .replace(" ", "-")
        .replace(".", "_")
    include(id)
    val project: ProjectDescriptor = project(":$id")
    project.projectDir = File(rootDir, directory)
    project.name = name
}

createProject("Beta1.7.3", "minecraft/beta/1.7.3")
