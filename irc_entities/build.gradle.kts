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
                api(project(":Utility_Functions"))
            }

        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(project(":TestUtils"))
                implementation(project(":TestArbs"))
            }
        }
    }
}

koverReport {
    verify {
        rule {
            isEnabled = true
            minBound(98)
//            minBound(98, metric = MetricType.BRANCH) //TODO be more strict
            filters {
                includes {
                    packages("com.xingpeds.kmirc.entities.events")
                }
            }
        }
    }
}