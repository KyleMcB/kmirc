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
                implementation("io.ktor:ktor-network:2.3.6")
                implementation(project(":Utility_Functions"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
                implementation("io.ktor:ktor-server-test-host:+")
                implementation("io.ktor:ktor-server-tests:+")
            }
        }
        val jvmMain by getting {
            // JVM-specific dependencies
        }
    }
}