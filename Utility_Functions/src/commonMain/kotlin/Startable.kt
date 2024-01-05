/*
 * Copyright 2024 Kyle McBurnett
 */

import kotlinx.coroutines.Job

/**
 * This interface is for cases where a constructor can not be utilized
 * and the instance needs an initial construction/startup call
 */
interface Startable {
    fun start()
}

/**
 * This interface is for cases where a constructor can not be utilized
 * and the instance needs an initial construction/startup call
 * This version has a suspendable init function
 */
interface StartableJob {
    fun start(): Job
}