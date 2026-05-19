plugins {
    java
    `maven-publish`
}

repositories {
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        url = uri("https://nexus.telesphoreo.me/repository/plex/")
    }

    maven {
        url = uri("https://repo.md-5.net/content/groups/public/")
    }

    maven {
        url = uri("https://repo.codemc.io/repository/maven-releases/")
    }

    maven {
        url = uri("https://mvn.lib.co.nz/public")
    }

    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.46")
    annotationProcessor("org.projectlombok:lombok:1.18.46")
    compileOnly("io.papermc.paper:paper-api:26.1.2.build.+")
    compileOnly("dev.plex:api:2.0-SNAPSHOT")
    implementation("me.libraryaddict.disguises:libsdisguises:11.0.18")
    compileOnly("com.github.retrooper:packetevents-spigot:2.12.1")
}

group = "dev.plex"
version = "2.0"
description = "Module-LibsDisguises"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

tasks.getByName<Jar>("jar") {
    archiveBaseName.set("Module-LibsDisguises")
    archiveVersion.set("")
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}