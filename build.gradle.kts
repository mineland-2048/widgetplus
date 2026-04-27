plugins {
    id("net.fabricmc.fabric-loom")
    `maven-publish`
}

version = providers.gradleProperty("mod_version").get()
group = providers.gradleProperty("maven_group").get()

loom {
    runs {
        runConfigs.remove(runConfigs["server"])
    }

    accessWidenerPath = file("src/main/resources/widgetplus.accesswidener")
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1") // DevAuth
    maven("https://maven.terraformersmc.com") // Mod Menu
    maven("https://maven.isxander.dev/releases") // YACL
}

dependencies {
    minecraft("com.mojang:minecraft:${providers.gradleProperty("minecraft_version").get()}")
    implementation("net.fabricmc:fabric-loader:${providers.gradleProperty("loader_version").get()}")

    implementation("dev.isxander:yet-another-config-lib:${providers.gradleProperty("yacl_version").get()}-fabric")
    implementation("com.terraformersmc:modmenu:${providers.gradleProperty("modmenu_version").get()}")

    localRuntime("me.djtheredstoner:DevAuth-fabric:${providers.gradleProperty("devauth_version").get()}")
}

tasks.processResources {
    inputs.property("version", version)
    filesMatching("fabric.mod.json") {
        expand("version" to version)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

tasks.jar {
    inputs.property("projectName", project.name)
    from("LICENSE") {
        rename { "${it}_${project.name}" }
    }
}

// configure the maven publication
publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    // See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
    repositories {
        // Add repositories to publish to here.
        // Notice: This block does NOT have the same function as the block in the top level.
        // The repositories here will be used for publishing your artifact, not for
        // retrieving dependencies.
    }
}
