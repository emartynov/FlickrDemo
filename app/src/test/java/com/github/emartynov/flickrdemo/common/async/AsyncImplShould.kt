package com.github.emartynov.flickrdemo.common.async

import com.github.emartynov.flickrdemo.common.http.Failure
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
    fun `Run callable on executor thread and deliver on UI`() {
        val value = 2
        val callable = Callable<Int> { value }
        var returnedValue = 0

        async.queue(job = callable, callback = { v -> returnedValue = v.requireSuccessData() })
        executor.runJob()
        uiThreadDelivery.runAll()

        assertThat(returnedValue).isEqualTo(value)
    }

    @Test
    fun `Cancel all futures when asked`() {
        async.queue(job = Callable { }, callback = {})

        async.cancelAll()

        assertThat(executor.areAllCancelled()).isTrue()
    }

    @Test
    fun `Cancel job by tag`() {
        val tag = Any()
        async.queue(job = Callable { }, callback = {}, tag = tag)

        async.cancel(tag)

        assertThat(executor.areAllCancelled()).isTrue()
    }

    @Test
    fun `Cancel only specific job by tag`() {
        val tag1 = Any()
        async.queue(job = Callable { }, callback = {}, tag = tag1)
        val tag2 = Any()
        async.queue(job = Callable { }, callback = {}, tag = tag2)

        async.cancel(tag1)

        assertThat(executor.areAllCancelled()).isFalse()
    }

    @Test
    fun `Do not deliver value if cancelled`() {
        val tag = Any()
        val value = 2
        val callable = Callable<Int> { value }
        var returnedValue = 0

        async.queue(job = callable, callback = { v -> returnedValue = v.requireSuccessData() }, tag = tag)
        async.cancel(tag)
        executor.runJob()
        uiThreadDelivery.runAll()

        assertThat(returnedValue).isNotEqualTo(value)
    }

    @Test
    fun `Wrap exception in AsyncResult Failure`() {
        val exception = NullPointerException("Test")
        var recordedException: Throwable? = null

        async.queue(job = Callable { throw exception }, callback = { recordedException = (it as? Failure)?.error })
        executor.runJob()
        uiThreadDelivery.runAll()

        assertThat(recordedException).isEqualTo(exception)
    }
}
