/*
 * Copyright (c) Kyle McBurnett 2024.
 */

plugins {
    kotlin("jvm")

}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    api(project(":irc_entities"))
    api(project(":client-network:header"))
    implementation(project(":client-network:ktorImpl"))
    api(project(":client_state:header"))
    api(project(":client_state:DI"))
    implementation(project(":irc_parser:implementation"))
    api(project(":irc_parser:header"))
    api(project(":eventlist:header"))
    api(project(":eventlist:DI"))





    api(project(":client_irc_engine:header"))
    implementation(project(":client_irc_engine:implementation"))
    testImplementation(project(":TestUtils"))
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.20")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}