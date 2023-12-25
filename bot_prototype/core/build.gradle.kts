/*
 * Copyright 2024 Kyle McBurnett
 */

plugins {
    kotlin("jvm")

}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation(project(":irc_entities"))
    implementation(project(":client-network:header"))
    implementation(project(":bot_prototype:dep_inject"))
    implementation(project(":irc_parser:header"))
    implementation(project(":client_state:header"))


    implementation(project(":client_irc_engine:header"))
    testImplementation(project(":TestUtils"))
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.20")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}