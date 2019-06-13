package com.github.emartynov.flickrdemo.imagelist.usecase

import com.github.emartynov.flickrdemo.imagelist.data.PageData

class LoadListUseCaseStub : LoadListUseCase {
    var data: PageData? = null

    override fun loadPhotos(search: String, callback: (PageData) -> Unit) {
        data?.run {
            callback(this)
        }
    }
}
