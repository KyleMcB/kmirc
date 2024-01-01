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
                api("org.jetbrains.kotlin:kotlin-test:1.9.20")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
                api("io.kotest:kotest-property:5.8.0") // Kotest property testing dependency
            }
        }
        val commonTest by getting {
            dependencies {
            }
        }
    }
}