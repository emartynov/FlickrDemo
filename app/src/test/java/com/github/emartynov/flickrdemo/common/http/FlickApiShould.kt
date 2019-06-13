package com.github.emartynov.flickrdemo.common.http

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class FlickApiShould {
    @Test
    fun `Correctly form search url`() {
        val searchString = "test"

        assertThat(FlickApi.getSearchUrl(searchString))
            .isEqualTo(
                "https://api.flickr.com/services/rest/" +
                        "?method=flickr.photos.search" +
                        "&api_key=$FLICKR_KEY" +
                        "&format=json" +
                        "&nojsoncallback=1" +
                        "&safe_search=1" +
                        "&text=$searchString"
            )
    }
}
