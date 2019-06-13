package com.github.emartynov.flickrdemo.common.async

import java.util.concurrent.*

interface Async {
    fun <T> queue(job: Callable<T>, callback: (T) -> Unit)
}

private val NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors()

internal class AsyncImpl(
    private val queue: BlockingQueue<Runnable> = LinkedBlockingQueue<Runnable>(),
    private val executor: Executor =
        ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES, 10, TimeUnit.SECONDS, queue),
    private val uiThreadDelivery: UiThreadDelivery = UiThreadDeliveryImpl()
) : Async {

    override fun <T> queue(job: Callable<T>, callback: (T) -> Unit) {
        executor.execute {
            val value = job.call()
            uiThreadDelivery.post(Runnable { callback(value) })
        }
    }
}
