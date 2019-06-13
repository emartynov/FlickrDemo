package com.github.emartynov.flickrdemo.common

import com.github.emartynov.flickrdemo.common.async.AsyncImpl
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.util.concurrent.Callable

class AsyncImplShould {
    private val executor = ExecutorStub()
    private val uiThreadDelivery = UiThreadDeliveryStub()

    private val async = AsyncImpl(
        executor = executor,
        uiThreadDelivery = uiThreadDelivery
    )

    @Test
    fun `Run callable on executor thread`() {
        val value = 2
        val callable = Callable<Int> { value }
        var returnedValue = 0

        async.queue(job = callable, callback = { v -> returnedValue = v })
        executor.runJob()
        uiThreadDelivery.runAll()

        assertThat(returnedValue).isEqualTo(value)
    }
}
