package com.github.emartynov.flickrdemo.common.async

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
