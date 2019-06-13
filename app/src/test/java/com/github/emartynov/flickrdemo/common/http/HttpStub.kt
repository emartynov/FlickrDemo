package com.github.emartynov.flickrdemo.common.http

class HttpStub : Http {
    private val stubs = HashMap<String, ByteArray>()
    val invokedUrls = mutableListOf<String>()

    override fun get(url: String) = stubs[url] as ByteArray

    fun stub(url: String, response: ByteArray) {
        invokedUrls.add(url)
        stubs[url] = response
    }
}
