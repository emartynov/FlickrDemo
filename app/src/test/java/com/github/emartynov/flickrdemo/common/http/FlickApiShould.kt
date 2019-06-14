package com.github.emartynov.flickrdemo.common.http

import com.github.emartynov.flickrdemo.imagelist.data.ImageData
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class FlickApiShould {
    @Test
    fun `Correctly form search url`() {
        val searchString = "test"
        val page = 6

        assertThat(FlickApi.getSearchUrl(searchString, page))
            .isEqualTo(
                "https://api.flickr.com/services/rest/" +
                        "?method=flickr.photos.search" +
                        "&api_key=$FLICKR_KEY" +
                        "&format=json" +
                        "&nojsoncallback=1" +
                        "&safe_search=1" +
                        "&text=$searchString" +
                        "&page=$page"
            )
    }

    @Test
    fun `Correctly form image url`() {
        val imageData = ImageData(id = "id", secret = "secret", server = "server", farm = 1, title = "Some")

        assertThat(FlickApi.getImageUrl(imageData))
            .isEqualTo("https://farm1.static.flickr.com/server/id_secret.jpg")
    }
}
