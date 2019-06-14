package com.github.emartynov.flickrdemo.imagelist.model

import com.github.emartynov.flickrdemo.common.async.AsyncImpl
import com.github.emartynov.flickrdemo.common.http.AsyncResult
import com.github.emartynov.flickrdemo.common.http.Failure
import com.github.emartynov.flickrdemo.common.http.HttpImpl
import com.github.emartynov.flickrdemo.common.http.Success
import com.github.emartynov.flickrdemo.imagelist.data.ImageData
import com.github.emartynov.flickrdemo.imagelist.data.ImageDataJsonParser
import com.github.emartynov.flickrdemo.imagelist.data.PageData
import com.github.emartynov.flickrdemo.imagelist.data.PageDataJsonParser
import com.github.emartynov.flickrdemo.imagelist.usecase.LoadListUseCase
import com.github.emartynov.flickrdemo.imagelist.usecase.LoadListUseCaseImpl
import java.util.*

enum class State {
    LOADING,
    READY,
    ERROR
}

class ImageListSearchModel(
    var state: State = State.READY,
    var currentPage: Int = 1,
    var totalPages: Int = 0,
    var searchString: String? = null,
    val images: MutableList<ImageData> = mutableListOf(),
    private val loadListUseCase: LoadListUseCase = LoadListUseCaseImpl(
        HttpImpl(),
        PageDataJsonParser(ImageDataJsonParser()),
        AsyncImpl()
    )
) : Observable() {
    fun search(searchString: String) {
        loadListUseCase.cancel()
        this.searchString = searchString
        currentPage = 1
        totalPages = 0
        images.clear()
        state = State.LOADING

        loadListUseCase.loadPhotos(searchString, currentPage) { processPhotosLoad(it) }
        updateObserverWithChanges()
    }

    private fun processSuccess(data: PageData) {
        currentPage = data.page
        totalPages = data.totalPages
        images.addAll(data.images)
        state = State.READY
    }

    private fun updateObserverWithChanges() {
        setChanged()
        notifyObservers()
    }

    fun clear() {
        loadListUseCase.cancel()
    }

    fun retry() {
        search(searchString ?: "error")
    }

    fun loadPage(pageToLoad: Int) {
        // guard of loading more pages
        if (state == State.LOADING && pageToLoad == currentPage + 1) return

        state = State.LOADING

        // search string should not be null, added fallback of error if assumption is wrong
        loadListUseCase.loadPhotos(searchString ?: "error", pageToLoad) { processPhotosLoad(it) }
        updateObserverWithChanges()
    }

    private fun processPhotosLoad(result: AsyncResult<PageData>) {
        when (result) {
            is Success -> processSuccess(result.data)
            is Failure -> state = State.ERROR
        }

        updateObserverWithChanges()
    }
}
