/*
 * Copyright (c) Kyle McBurnett 2024.
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

                implementation(project(":client_state:header"))
                implementation(project(":client_state:mutable_impl"))

            }
        }
        val commonTest by getting {

        }
    }
}