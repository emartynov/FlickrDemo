package com.github.emartynov.flickrdemo.common.async

import android.os.Handler
import android.os.Looper

interface UiThreadDelivery {
    fun post(runnable: Runnable)
}

class UiThreadDeliveryImpl : UiThreadDelivery {
    private val handler = Handler(Looper.getMainLooper())

    override fun post(runnable: Runnable) {
        handler.post(runnable)
    }
}
