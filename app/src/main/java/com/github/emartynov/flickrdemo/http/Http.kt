package com.github.emartynov.flickrdemo.http

import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL

class Http {
    fun get(url: String): ByteArray {
        val urlConnection = URL(url).openConnection() as HttpURLConnection

        try {
            val stream = BufferedInputStream(urlConnection.inputStream)
            return stream.readBytes()
        } finally {
            urlConnection.disconnect()
        }
    }
}
