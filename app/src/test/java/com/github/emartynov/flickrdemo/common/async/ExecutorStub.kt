package com.github.emartynov.flickrdemo.common.async

import java.util.concurrent.*

class ExecutorStub : AbstractExecutorService() {
    private val scheduledJobs = mutableListOf<Runnable>()
    private val futureTasks = mutableListOf<FutureTask<Unit>>()

    override fun isTerminated() = false

    override fun execute(command: Runnable) {
        throw IllegalArgumentException("We don't execute since we want to cancel it in future")
    }

    override fun shutdown() {}

    override fun shutdownNow() = scheduledJobs

    override fun isShutdown() = false

    override fun awaitTermination(timeout: Long, unit: TimeUnit) = false

    override fun submit(command: Runnable): Future<*> {
        scheduledJobs.add(command)

        val futureTask = FutureTask(Callable { })
        futureTasks.add(futureTask)

        return futureTask
    }

    fun runJob() {
        scheduledJobs.forEach { it.run() }
        scheduledJobs.clear()
        futureTasks.clear()
    }

    fun areAllCancelled() = futureTasks.all { it.isCancelled }
}
