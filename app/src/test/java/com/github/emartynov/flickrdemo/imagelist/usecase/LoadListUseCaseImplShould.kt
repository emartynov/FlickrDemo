package com.github.emartynov.flickrdemo.imagelist.usecase

import com.github.emartynov.flickrdemo.common.async.AsyncStub
import com.github.emartynov.flickrdemo.common.http.AsyncResult
import com.github.emartynov.flickrdemo.common.http.Failure
import com.github.emartynov.flickrdemo.common.http.HttpStub
import com.github.emartynov.flickrdemo.imagelist.data.PageData
import com.github.emartynov.flickrdemo.imagelist.data.PageDataJsonParserStub
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class LoadListUseCaseImplShould {
    private val http = HttpStub()
    private val jsonPageDataParser = PageDataJsonParserStub()
    private val async = AsyncStub()

    private val useCase = LoadListUseCaseImpl(http, jsonPageDataParser, async)

    @Test
    fun `Correctly form url for search request`() {
        val searchString = "kittens"
        val page = 2

        val url = formSearchString(searchString, page)
        http.stub(url, ByteArray(0))
        jsonPageDataParser.stubbedData = PageData(page = 0, totalPages = 1, images = emptyList())

        useCase.loadPhotos(searchString, page) {}

        assertThat(http.invokedUrls).containsExactly(url)
    }

    private fun formSearchString(searchString: String, page: Int) =
        "https://api.flickr.com/services/rest/?method=flickr.photos.search" +
                "&api_key=3e7cc266ae2b0e0d78e279ce8e361736" +
                "&format=json" +
                "&nojsoncallback=1" +
                "&safe_search=1" +
                "&text=$searchString" +
                "&page=$page"

    @Test
    fun `Use json parser`() {
        val searchString = "test"
        val page = 1

        val url = formSearchString(searchString, page)
        val response = "Data"
        http.stub(url, response.toByteArray())
        jsonPageDataParser.stubbedData = PageData(page = 0, totalPages = 1, images = emptyList())

        useCase.loadPhotos(searchString, page) {}

        assertThat(jsonPageDataParser.passedData).isEqualTo(response)
    }

    @Test
    fun `Call callback after data processing`() {
        var data: PageData? = null

        val searchString = "some"
        val page = 1
        val url = formSearchString(searchString, page)
        val pageData = PageData(page = 0, totalPages = 1, images = emptyList())
        http.stub(url, ByteArray(0))
        jsonPageDataParser.stubbedData = pageData

        useCase.loadPhotos(searchString, page) { d -> data = d.requireSuccessData() }

        assertThat(data).isEqualTo(pageData)
    }

    @Test
    fun `Cancel when asked`() {
        async.execute = false
        useCase.loadPhotos("some", 1) {}

        useCase.cancel()

        assertThat(async.isCancelled).isTrue()
    }

    @Test
    fun `Pass failure to callback`() {
        http.error(IllegalStateException("Test"))
        var recordedResult: AsyncResult<PageData>? = null

        useCase.loadPhotos("some", 1) { recordedResult = it }

        assertThat(recordedResult).isInstanceOf(Failure::class.java)
    }
}
