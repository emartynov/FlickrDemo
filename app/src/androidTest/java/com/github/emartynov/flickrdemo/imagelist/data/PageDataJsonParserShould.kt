package com.github.emartynov.flickrdemo.imagelist.data

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PageDataJsonParserShould {
    private val parser = PageDataJsonParser(ImageDataJsonParser())

    @Test
    fun parsePageObjectCorrectly() {
        val json = """
{
  "photos": {
    "page": 1,
    "pages": 1634,
    "perpage": 1,
    "total": "163353",
    "photo": [
      {
        "id": "48056810136",
        "owner": "181900351@N04",
        "secret": "48fdcd495a",
        "server": "65535",
        "farm": 66,
        "title": "Me and my girl #kitten",
        "ispublic": 1,
        "isfriend": 0,
        "isfamily": 0
      }
    ]
  },
  "stat": "ok"
}
        """.trimIndent()

        val data = parser.parse(json)

        assertThat(data)
            .isEqualTo(
                PageData(
                    page = 1,
                    totalPages = 1634,
                    images = listOf(
                        ImageData(
                            id = "48056810136",
                            secret = "48fdcd495a",
                            server = "65535",
                            farm = 66,
                            title = "Me and my girl #kitten"
                        )
                    )
                )
            )
    }
}
