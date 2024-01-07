/*
 * Copyright 2024 Kyle McBurnett
 */

rootProject.name = "MyApplication"
/**
 * Directories that should not be traversed while looking for modules to include in the gradle build
 */
val excludedDirs: Set<String> = setOf(".gradle", "gradle", ".idea", ".run", ".git", "build", "src")

/**
 * Recursively includes all the directories that have a `build.gradle.kts` file and adds them to the given set.
 *
 * @param set The set of modules to include from recursive call one step up in the stack
 * @param prefix The prefix to be added to the module path.
 * @param dir The current directory to be checked for inclusion.
 * @return The set of included module paths.
 */
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

include(includeModules(emptySet(), "", rootDir))
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
