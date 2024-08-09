plugins {
    `kotlin-dsl`
    alias(libs.plugins.unimined)
    alias(libs.plugins.spotless)
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
