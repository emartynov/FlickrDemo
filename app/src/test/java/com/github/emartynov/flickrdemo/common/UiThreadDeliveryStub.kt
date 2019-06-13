package com.github.emartynov.flickrdemo.common

import com.github.emartynov.flickrdemo.common.async.UiThreadDelivery

class UiThreadDeliveryStub : UiThreadDelivery {
    private val runnableList = mutableListOf<Runnable>()

    override fun post(runnable: Runnable) {
        runnableList.add(runnable)
    }

    fun runAll() {
        runnableList.forEach { it.run() }

        runnableList.clear()
    }
}
