/*
 * Copyright 2024 Kyle McBurnett
 */

plugins {
    kotlin("jvm")

}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    api(project(":irc_entities"))
    api(project(":client-network:header"))
    api(project(":client_state:header"))
    api(project(":irc_parser:header"))
    api(project(":eventlist:header"))





    api(project(":client_irc_engine:header"))
    implementation(project(":client_irc_engine:implementation"))
    testImplementation(project(":TestUtils"))
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.20")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}