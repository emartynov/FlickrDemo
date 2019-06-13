package com.github.emartynov.flickrdemo.common.async

import android.os.Looper
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UiThreadDeliveryImplShould {
    private val idlingResource = CountingIdlingResource("UiThreadDeliveryImplShould")

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    @Test
    fun deliverResultOnMainThread() {
        val delivery = UiThreadDeliveryImpl()
        var wasRunOnUI = false

        idlingResource.increment()
        delivery.post(
            Runnable {
                wasRunOnUI = Looper.getMainLooper().thread == Thread.currentThread()
                idlingResource.decrement()
            }
        )

        InstrumentationRegistry.getInstrumentation().waitForIdleSync()

        assertThat(wasRunOnUI).isTrue()
    }
}
