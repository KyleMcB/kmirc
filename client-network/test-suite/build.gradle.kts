plugins {
    kotlin("multiplatform")
}

kotlin {

    jvm()


    sourceSets {
        val commonMain by getting {
            // Dependencies common across all platforms
            dependencies {
                implementation(project(":client-network:contracts"))
                implementation("io.ktor:ktor-network:2.3.6")
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
                implementation("io.kotest:kotest-property:5.8.0")
            }
        }
        val jvmMain by getting {
            // JVM-specific dependencies
        }
    }
}