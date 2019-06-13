package com.github.emartynov.flickrdemo.common.async

import java.util.concurrent.*

interface Async {
    fun <T> queue(job: Callable<T>, callback: (T) -> Unit, tag: Any = Any())
    fun cancelAll()
    fun cancel(tag: Any)
}

private val NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors()

/**
 * Class that will run job on thread executor and allow to cancel it by tag or all
 */
internal class AsyncImpl(
    private val queue: BlockingQueue<Runnable> = LinkedBlockingQueue<Runnable>(),
    private val executor: ExecutorService =
        ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES, 10, TimeUnit.SECONDS, queue),
    private val uiThreadDelivery: UiThreadDelivery = UiThreadDeliveryImpl()
) : Async {
    private val jobs = HashMap<Any, Future<*>>()

    override fun <T> queue(job: Callable<T>, callback: (T) -> Unit, tag: Any) {
        val future = executor.submit {
            val value = job.call()

            // check if cancelled
            if (jobs[tag]?.isCancelled == false)
                uiThreadDelivery.post(Runnable { callback(value) })
        }

        saveFuture(tag, future)
    }

    private fun saveFuture(tag: Any, future: Future<*>) {
        jobs[tag] = future
    }

    override fun cancelAll() {
        jobs.values.forEach { it.cancel(true) }
    }

    override fun cancel(tag: Any) {
        jobs[tag]?.cancel(true)
    }
}
