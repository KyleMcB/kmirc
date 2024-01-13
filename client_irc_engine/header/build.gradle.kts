/*
 * Copyright 2024 Kyle McBurnett
 */

plugins {
    kotlin("multiplatform")
}


kotlin {

    jvm()

    sourceSets {
        val commonMain by getting {
            // Dependencies common across all platforms
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                api(project(":irc_entities"))
                api(project(":client_state:header"))
                api(project(":eventlist:header"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(project(":TestUtils"))
                implementation("org.jetbrains.kotlin:kotlin-test:1.9.20")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
            }
        }
    }
}