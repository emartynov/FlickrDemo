package com.github.emartynov.flickrdemo.imagelist.data

import androidx.test.runner.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ImageDataParserShould {
    private val parser = ImageDataJsonParser()

    @Test
    fun correctlyParseJsonToObject() {
        val json = """
            {
                "id": "23451156376",
                "owner": "28017113@N08",
                "secret": "8983a8ebc7",
                "server": "578",
                "farm": 1,
                "title": "Merry Christmas!",
                "ispublic": 1,
                "isfriend": 0,
                "isfamily": 0
            }
        """.trimIndent()

        val data = parser.parse(json)

        assertThat(data)
            .isEqualTo(
                ImageData(
                    id = "23451156376",
                    secret = "8983a8ebc7",
                    server = "578",
                    farm = 1,
                    title = "Merry Christmas!"
                )
            )
    }
}
