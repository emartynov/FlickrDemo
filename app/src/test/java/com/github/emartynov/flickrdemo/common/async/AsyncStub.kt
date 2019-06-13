package com.github.emartynov.flickrdemo.common.async

import java.util.concurrent.Callable

class AsyncStub : Async {
    override fun <T> queue(job: Callable<T>, callback: (T) -> Unit) {
        callback(job.call())
    }
}
