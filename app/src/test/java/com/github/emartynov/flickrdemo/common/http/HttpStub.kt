package com.github.emartynov.flickrdemo.common.http

class HttpStub : Http {
    private val stubs = HashMap<String, ByteArray>()

    override fun get(url: String) = stubs[url] as ByteArray

    fun stub(url: String, response: ByteArray) {
        stubs[url] = response
    }
}
