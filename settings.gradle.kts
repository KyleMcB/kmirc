/*
 * Copyright 2024 Kyle McBurnett
 */

rootProject.name = "MyApplication"
val excludedDirs = setOf(".gradle", "gradle", ".idea", ".run", ".git", "build", "src")

// Recursive function to enter all the directories that has a build.gradle.kts file and include them
fun includeModules(set: Set<String>, prefix: String, dir: File): Set<String> {
    val gradlePath = "$prefix:${dir.name}".removePrefix(":kmirc")
    val intermediateSet = set + if (dir.listFiles()?.firstOrNull { it.name == "build.gradle.kts" } != null &&
        gradlePath != "") {
        println("including $gradlePath")
        setOf(gradlePath)
    } else {
        emptySet()
    }
    return dir.listFiles()?.filter { !excludedDirs.contains(it.name) && it.isDirectory }
        ?.fold(intermediateSet) { acc: Set<String>, file: File? ->
            acc + includeModules(
                intermediateSet,
                gradlePath,
                file ?: File(".")
            )
        } ?: emptySet()
}

val output = includeModules(emptySet(), "", rootDir).toList().sorted()
println("set $output")
val list = mutableListOf<String>()
list.add(":androidApp")
list.add(":shared")
list.add(":desktopApp")
list.add(":client-network:header")
list.add(":client-network:ktorImpl")
list.add(":irc_entities")
list.add(":irc_parser:header")
list.add(":irc_parser:header_test")
list.add(":irc_parser:implementation")
list.add(":TestUtils")
list.add(":client_state:header")
list.add(":client_irc_engine:header")
list.add(":client_irc_engine:implementation")
list.add(":Utility_Functions")
list.add(":bot_prototype:core")
list.add(":bot_prototype:dep_inject")
list.sort()
println("manual $list")
include(output)
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        val kotlinVersion = extra["kotlin.version"] as String
        val agpVersion = extra["agp.version"] as String
        val composeVersion = extra["compose.version"] as String

        kotlin("jvm").version(kotlinVersion)
        kotlin("multiplatform").version(kotlinVersion)
        kotlin("android").version(kotlinVersion)

        id("com.android.application").version(agpVersion)
        id("com.android.library").version(agpVersion)

        id("org.jetbrains.compose").version(composeVersion)
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version ("0.4.0")
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
