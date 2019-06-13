package com.github.emartynov.flickrdemo.common.async

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

    override fun <T> queue(job: Callable<T>, callback: (T) -> Unit, tag: Any) {
        if (execute) callback(job.call())
    }
}
