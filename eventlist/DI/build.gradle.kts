/*
 * Copyright 2024 Kyle McBurnett
 */

plugins {
    kotlin("multiplatform")
}


kotlin {

    jvm() {
        withJava()
    }

    sourceSets {
        val commonMain by getting {
            // Dependencies common across all platforms
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                implementation(project(":irc_entities"))
                implementation(project(":eventlist:impl"))
                implementation(project(":eventlist:header"))
            }
        }

    }
}
