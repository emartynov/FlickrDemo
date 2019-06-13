package com.github.emartynov.flickrdemo.imagelist.model

import com.github.emartynov.flickrdemo.common.async.AsyncImpl
import com.github.emartynov.flickrdemo.common.http.HttpImpl
import com.github.emartynov.flickrdemo.imagelist.data.ImageData
import com.github.emartynov.flickrdemo.imagelist.data.ImageDataJsonParser
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
    var state: State = State.LOADING,
    var currentPage: Int = 0,
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
        currentPage = 0
        totalPages = 0
        images.clear()
        state = State.LOADING

        loadListUseCase.loadPhotos(searchString) {
            currentPage = it.page
            totalPages = it.totalPages
            images.addAll(it.images)
            state = State.READY

            updateObserverWithChanges()
        }
        updateObserverWithChanges()
    }

    private fun updateObserverWithChanges() {
        setChanged()
        notifyObservers()
    }

    fun clear() {
        loadListUseCase.cancel()
    }
}
