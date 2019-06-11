package com.github.emartynov.flickrdemo.http

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class HttpShould {
    private val http = Http()

    @Test
    fun `Fetch binary http content`() {
        val content = http.get("https://api.github.com/gists/9d2bc8dd7def1fe17702ba101fad8bdd")

        assertThat(String(content)).contains("\"content\":\"{}\"")
    }
}
