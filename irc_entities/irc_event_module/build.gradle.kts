/*
 * Copyright 2024 Kyle McBurnett
 */

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.kotlinx.kover") version "0.7.5"

}

kotlin {

    jvm()

    sourceSets {
        val commonMain by getting {

        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(project(":TestUtils"))
            }
        }
    }
}
koverReport {
    verify {
        rule {
            isEnabled = true
            minBound(98)
        }
    }
}