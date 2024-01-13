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
                implementation(project(":client-network:header"))
                implementation(project(":client-network:ktorImpl"))
                implementation(project(":Utility_Functions"))
            }
        }
        val commonTest by getting {
            dependencies {
            }
        }
        val jvmMain by getting {
            // JVM-specific dependencies
        }
    }
}