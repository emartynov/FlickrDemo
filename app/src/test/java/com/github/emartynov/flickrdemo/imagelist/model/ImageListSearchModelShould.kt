package com.github.emartynov.flickrdemo.imagelist.model

import com.github.emartynov.flickrdemo.common.http.AsyncResult
import com.github.emartynov.flickrdemo.imagelist.data.ImageData
import com.github.emartynov.flickrdemo.imagelist.data.PageData
import com.github.emartynov.flickrdemo.imagelist.usecase.LoadListUseCaseStub
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ImageListSearchModelShould {
    private val loadListUseCase = LoadListUseCaseStub()
    private val model = ImageListSearchModel(loadListUseCase = loadListUseCase)

    @Test
    fun `Update fields after search`() {
        val imageData = createTestImageData()
        val totalPages = 456
        val page = 100
        val searchString = "test"
        loadListUseCase.data =
            AsyncResult.success(PageData(page = page, totalPages = totalPages, images = listOf(imageData)))

        model.search(searchString)

        assertThat(model.currentPage).isEqualTo(page)
        assertThat(model.totalPages).isEqualTo(totalPages)
        assertThat(model.images).containsExactly(imageData)
    }

    @Test
    fun `Notify observer when data is loaded`() {
        loadListUseCase.data = AsyncResult.success(PageData(page = 100, totalPages = 456, images = emptyList()))
        var dataChanged = false
        model.addObserver { _, _ -> dataChanged = true }

        model.search("test")

        assertThat(dataChanged).isTrue()
    }

    @Test
    fun `Reset data when new search starts`() {
        model.currentPage = 1
        model.totalPages = 1
        model.images.add(createTestImageData())

        model.search("some")

        assertThat(model.currentPage).isEqualTo(1)
        assertThat(model.totalPages).isEqualTo(0)
        assertThat(model.images).isEmpty()
    }

    @Test
    fun `Cancel previous load when search`() {
        model.currentPage = 1
        model.totalPages = 1
        model.images.add(createTestImageData())

        model.search("some")

        assertThat(loadListUseCase.isCancelled).isTrue()
    }

    @Test
    fun `Set state to loading when search`() {
        model.state = State.READY

        model.search("some")

        assertThat(model.state).isEqualTo(State.LOADING)
    }

    @Test
    fun `Set state to ready after successful data loading`() {
        loadListUseCase.data = AsyncResult.success(PageData(page = 100, totalPages = 456, images = emptyList()))

        model.search("test")

        assertThat(model.state).isEqualTo(State.READY)
    }

    @Test
    fun `Convert failure to the error state`() {
        loadListUseCase.data = AsyncResult.failure(NullPointerException("Test"))

        model.search("test")

        assertThat(model.state).isEqualTo(State.ERROR)
    }

    @Test
    fun `Delegate next page load to use case`() {
        loadListUseCase.data = AsyncResult.success(PageData(page = 1, totalPages = 456, images = emptyList()))
        model.search("test")
        loadListUseCase.data = AsyncResult.success(PageData(page = 2, totalPages = 456, images = emptyList()))

        model.loadPage(2)

        assertThat(model.currentPage).isEqualTo(2)
    }

    @Test
    fun `Ignore sequential asks for page load`() {
        loadListUseCase.data = AsyncResult.success(PageData(page = 1, totalPages = 456, images = emptyList()))
        model.search("test")
        loadListUseCase.data = null

        model.loadPage(2)
        model.loadPage(2)
        model.loadPage(2)

        assertThat(loadListUseCase.numberOfCalls).isEqualTo(2)
    }

    @Test
    fun `Set state to loading when next page is requested`() {
        loadListUseCase.data = AsyncResult.success(PageData(page = 1, totalPages = 456, images = emptyList()))
        model.search("test")
        loadListUseCase.data = null

        model.loadPage(2)

        assertThat(model.state).isEqualTo(State.LOADING)
    }

    @Test
    fun `Notify observer when page is loaded`() {
        loadListUseCase.data = AsyncResult.success(PageData(page = 100, totalPages = 456, images = emptyList()))
        model.search("test")
        var dataChanged = false
        model.addObserver { _, _ -> dataChanged = true }

        model.loadPage(2)

        assertThat(dataChanged).isTrue()
    }

    private fun createTestImageData() =
        ImageData(id = "id", server = "server", secret = "secret", farm = 1, title = "title")
}
