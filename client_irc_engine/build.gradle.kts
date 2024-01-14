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
    implementation(project(":client-network:ktorImpl"))
    implementation(project(":client-network:DI"))
    api(project(":client_state:header"))
    implementation(project(":client_state:DI"))
    implementation(project(":irc_parser:implementation"))
    api(project(":irc_parser:header"))
    implementation(project(":irc_parser:DI"))
    api(project(":eventlist:header"))
    implementation(project(":eventlist:DI"))
    implementation(project(":container"))






    api(project(":client_irc_engine:header"))
    implementation(project(":client_irc_engine:implementation"))
    testImplementation(project(":TestUtils"))
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.20")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}