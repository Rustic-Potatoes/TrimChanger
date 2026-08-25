plugins {
    java
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.21"
    id("xyz.jpenilla.run-paper") version "3.0.2"
}

group = project.group
version = project.version

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("26.1.2.build.+")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

tasks.processResources {
    inputs.property("version", version)

    filesMatching("plugin.yml") {
        expand(mapOf("version" to version))
    }
}

tasks {
    compileJava {
        options.release = 25
    }

    jar {
        archiveBaseName.set("TrimChanger")
        from(rootDir) {
            include("LICENSE")
        }
    }

    runServer {
        minecraftVersion("26.1.2")
    }

    register<xyz.jpenilla.runpaper.task.RunServer>("runServer26.1.2") {
        minecraftVersion("26.1.2")
        pluginJars(jar)
        runDirectory.set(layout.projectDirectory.dir("run/26.1.2"))
    }

    register<xyz.jpenilla.runpaper.task.RunServer>("runServer26.2") {
        minecraftVersion("26.2")
        //pluginJars(jar)
        runDirectory.set(layout.projectDirectory.dir("run/26.2"))
    }
}
