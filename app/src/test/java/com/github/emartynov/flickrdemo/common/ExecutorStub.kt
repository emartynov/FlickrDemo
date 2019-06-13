package com.github.emartynov.flickrdemo.common

import java.util.concurrent.Executor

class ExecutorStub : Executor {
    private var scheduledJobs = mutableListOf<Runnable>()

    override fun execute(command: Runnable) {
        scheduledJobs.add(command)
    }

    fun runJob() {
        scheduledJobs.forEach { it.run() }

        scheduledJobs.clear()
    }
}
