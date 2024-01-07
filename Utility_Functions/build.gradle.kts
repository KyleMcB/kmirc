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
            dependencies {
                implementation("co.touchlab:kermit:2.0.2")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            }

        }
    }
}