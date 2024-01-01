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
            dependencies {
                api(project(":TestUtils"))
                implementation(project(":irc_entities"))
                api("io.kotest.extensions:kotest-property-arbs:+")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

