/*
 * Copyright 2024 Kyle McBurnett
 */

rootProject.name = "MyApplication"

//include(":androidApp")
//include(":shared")
//include(":desktopApp")
include(":client-network:header")
include(":client-network:ktorImpl")
include(":irc_entities")
include(":irc_parser:header")
include(":irc_parser:header_test")
include(":irc_parser:implementation")
include(":TestUtils")
include(":client_state:header")
include(":client_irc_engine:header")
include(":client_irc_engine:implementation")
include(":Utility_Functions")
include(":bot_prototype:core")
include(":bot_prototype:dep_inject")
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
