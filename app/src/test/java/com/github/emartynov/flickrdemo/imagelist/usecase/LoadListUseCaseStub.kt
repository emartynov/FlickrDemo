package com.github.emartynov.flickrdemo.imagelist.usecase

import com.github.emartynov.flickrdemo.common.http.AsyncResult
import com.github.emartynov.flickrdemo.imagelist.data.PageData

class LoadListUseCaseStub : LoadListUseCase {
    var data: AsyncResult<PageData>? = null
    var isCancelled = false
    var numberOfCalls = 0

    override fun loadPhotos(search: String, page: Int, callback: (AsyncResult<PageData>) -> Unit) {
        numberOfCalls++
        data?.run {
            callback(this)
        }
    }

    override fun cancel() {
        isCancelled = true
    }
}
