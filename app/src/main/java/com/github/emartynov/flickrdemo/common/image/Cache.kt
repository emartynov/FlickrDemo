package com.github.emartynov.flickrdemo.common.image

import android.graphics.Bitmap
import android.util.LruCache

interface Cache {
    fun get(key: Triple<String, Int, Int>): Bitmap?
    fun put(key: Triple<String, Int, Int>, bitmap: Bitmap)
}

private const val KILOBYTE = 1024

internal class CacheImpl : Cache {
    private val memoryCache: LruCache<Triple<String, Int, Int>, Bitmap>

    // initialisation copied from https://developer.android.com/topic/performance/graphics/cache-bitmap
    init {
        val maxMemory = (Runtime.getRuntime().maxMemory() / KILOBYTE).toInt()
        // Use 1/8th of the available memory for this memory cache.
        val cacheSize = maxMemory / 8

        memoryCache = object : LruCache<Triple<String, Int, Int>, Bitmap>(cacheSize) {

            override fun sizeOf(key: Triple<String, Int, Int>, bitmap: Bitmap): Int {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.byteCount / KILOBYTE
            }
        }
    }

    override fun get(key: Triple<String, Int, Int>): Bitmap? = memoryCache[key]

    override fun put(key: Triple<String, Int, Int>, bitmap: Bitmap) {
        memoryCache.put(key, bitmap)
    }
}
