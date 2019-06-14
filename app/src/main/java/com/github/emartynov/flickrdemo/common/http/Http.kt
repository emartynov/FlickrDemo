package com.github.emartynov.flickrdemo.common.http

import android.util.Log
import com.github.emartynov.flickrdemo.BuildConfig
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL

interface Http {
    fun get(url: String): ByteArray
}

internal class HttpImpl : Http {
    override fun get(url: String): ByteArray {
        if (!(url.startsWith("http://") || url.startsWith("https://")))
            throw IllegalArgumentException("Supposed to be used only with http protocol in $url")

        if (BuildConfig.DEBUG) {
            Log.d("HTTP", "Loading data from $url")
        }

        @Suppress("CAST_NEVER_SUCCEEDS") // we are sure it will be http connection
        val urlConnection = URL(url).openConnection() as HttpURLConnection

        try {
            val stream = BufferedInputStream(urlConnection.inputStream)
            return stream.readBytes()
        } finally {
            urlConnection.disconnect()
        }
    }
}
