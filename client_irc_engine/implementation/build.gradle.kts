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
                implementation(project(":irc_entities"))
                implementation(project(":client_state:header"))
                implementation(project(":client_state:mutable_impl"))
                implementation(project(":client_irc_engine:header"))
                implementation(project(":eventlist:header"))
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