package com.github.emartynov.flickrdemo.common.json

import com.github.emartynov.flickrdemo.imagelist.data.ImageData
import com.github.emartynov.flickrdemo.imagelist.data.ImageDataJsonParser
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class JsonParserFactoryImplShould {
    @Test
    fun `Return corresponded json parser`() {
        val parser = ImageDataJsonParser()
        val factory = JsonParserFactoryImpl(ImageData::class.java to parser)

        val parserFromFactory: JsonParser<ImageData> = factory.create()

        assertThat(parserFromFactory).isEqualTo(parser)
    }
}
