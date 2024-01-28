/*
 * Copyright (c) Kyle McBurnett 2024.
 */

import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    jvm()
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(compose.runtime)
                implementation(compose.foundation)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation(compose.desktop.common)
                implementation(compose.material3)
                val voyagerVersion = "1.0.0"

                // Multiplatform

                // Navigator
                implementation("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
                implementation("androidx.window:window:1.2.0")

                // Screen Model
                implementation("cafe.adriel.voyager:voyager-screenmodel:$voyagerVersion")
                implementation("com.russhwolf:multiplatform-settings:1.1.1")
                implementation(project(":client_irc_engine"))
                implementation(project(":client-network:DI"))
                implementation(project(":client-network:header"))
                implementation(project(":ResizableRow"))
                implementation(project(":ui_elements"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "KotlinMultiplatformComposeDesktopApplication"
            packageVersion = "1.0.0"
        }
    }
}
