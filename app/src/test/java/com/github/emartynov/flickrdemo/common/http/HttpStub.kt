package com.github.emartynov.flickrdemo.common.http

class HttpStub : Http {
    private val stubs = HashMap<String, ByteArray>()
    private var throwable: Throwable? = null

    val invokedUrls = mutableListOf<String>()

    override fun get(url: String): ByteArray {
        invokedUrls.add(url)

        val t = throwable

        return if (t == null) {
            stubs[url] as ByteArray
        } else {
            throw t
        }
    }

    fun stub(url: String, response: ByteArray) {
        stubs[url] = response
    }

    fun error(throwable: Throwable) {
        this.throwable = throwable
    }
}
