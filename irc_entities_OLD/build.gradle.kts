plugins {
    kotlin("multiplatform")
}

kotlin {

    jvm()

    sourceSets {
        val commonMain by getting {
            // Dependencies common across all platforms
//            dependencies {
//                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
//
//            }
        }
    }
}