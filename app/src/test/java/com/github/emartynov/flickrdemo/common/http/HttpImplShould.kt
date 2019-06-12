package com.github.emartynov.flickrdemo.common.http

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class HttpImplShould {
    private val http = HttpImpl()

    @Test
    fun `Fetch binary http content`() {
        val content = http.get("https://api.github.com/gists/9d2bc8dd7def1fe17702ba101fad8bdd")

        assertThat(String(content)).contains("\"content\":\"{}\"")
    }
}
