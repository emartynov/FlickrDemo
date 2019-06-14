package com.github.emartynov.flickrdemo.common.async

import com.github.emartynov.flickrdemo.common.http.AsyncResult
import java.util.concurrent.Callable

class AsyncStub : Async {
    var isCancelled = false
        private set

    var execute = true

    override fun cancelAll() {
        isCancelled = true
    }

    override fun cancel(tag: Any) {
        isCancelled = true
    }

    override fun <T : Any> queue(job: Callable<T>, callback: (AsyncResult<T>) -> Unit, tag: Any) {
        if (execute) callback(processData(job))
    }

    private fun <T : Any> processData(job: Callable<T>): AsyncResult<T> = try {
        val data = job.call()
        AsyncResult.success(data)
    } catch (e: Exception) {
        AsyncResult.failure(e)
    }
}
