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

                implementation(project(":client_state:header"))
                implementation(project(":client_state:mutable_impl"))
                implementation(project(":container"))

            }
        }
        val commonTest by getting {

        }
    }
}