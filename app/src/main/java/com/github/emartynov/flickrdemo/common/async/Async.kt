package com.github.emartynov.flickrdemo.common.async

import com.github.emartynov.flickrdemo.common.http.AsyncResult
import java.util.concurrent.*

interface Async {
    fun <T : Any> queue(job: Callable<T>, callback: (AsyncResult<T>) -> Unit, tag: Any = Any())
    fun cancelAll()
    fun cancel(tag: Any)
}

private val NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors()

/**
 * Class that will run job on thread executor and allow to cancel job by tag or all
 */
internal class AsyncImpl(
    private val queue: BlockingQueue<Runnable> = LinkedBlockingQueue<Runnable>(),
    private val executor: ExecutorService =
        ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES, 10, TimeUnit.SECONDS, queue),
    private val uiThreadDelivery: UiThreadDelivery = UiThreadDeliveryImpl()
) : Async {
    private val jobs = HashMap<Any, Future<*>>()

    override fun <T : Any> queue(job: Callable<T>, callback: (AsyncResult<T>) -> Unit, tag: Any) {
        val future = executor.submit {
            val value = try {
                AsyncResult.success(job.call())
            } catch (e: Exception) {
                AsyncResult.failure<T>(e)
            }

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
