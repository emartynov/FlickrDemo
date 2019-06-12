package com.github.emartynov.flickrdemo.common.http

import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL

interface Http {
    fun get(url: String): ByteArray
}

internal class HttpImpl : Http {
    override fun get(url: String): ByteArray {
        val urlConnection = URL(url).openConnection() as HttpURLConnection

        try {
            val stream = BufferedInputStream(urlConnection.inputStream)
            return stream.readBytes()
        } finally {
            urlConnection.disconnect()
        }
    }
}
