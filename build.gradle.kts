plugins {
    `kotlin-dsl`
    id("xyz.wagyourtail.unimined") // version 1.3.5
    id("com.diffplug.spotless") version "7.0.0.BETA1"
}

subprojects {
    apply("plugin" to "xyz.wagyourtail.unimined")
    apply("plugin" to "com.diffplug.spotless")

    tasks.withType(JavaCompile::class.java).configureEach {
        sourceCompatibility = "8"
        targetCompatibility = "8"
    }

    spotless {
        java {
            licenseHeaderFile(File(rootProject.rootDir, "JAVA_HEADER"))
        }
    }
}
