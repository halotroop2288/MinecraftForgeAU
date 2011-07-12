plugins {
    `kotlin-dsl`
    id("xyz.wagyourtail.unimined") // version 1.3.5
}

subprojects {
    apply("plugin" to "xyz.wagyourtail.unimined")
    apply("plugin" to "java-base")

    tasks.withType(JavaCompile::class.java).configureEach {
        sourceCompatibility = "8"
        targetCompatibility = "8"
    }
}
