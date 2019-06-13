package com.github.emartynov.flickrdemo.common.http

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class HttpImplShould {
    private val http = HttpImpl()

    // This is an integration test - it will fail without internet access
    @Test
    fun `Fetch binary http content`() {
        val content = http.get("https://api.github.com/gists/9d2bc8dd7def1fe17702ba101fad8bdd")

        assertThat(String(content)).contains("\"content\":\"{}\"")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Throw exception when not http`() {
        http.get("some://haha")
    }
}
