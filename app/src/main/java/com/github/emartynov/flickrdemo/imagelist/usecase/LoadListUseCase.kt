package com.github.emartynov.flickrdemo.imagelist.usecase

import com.github.emartynov.flickrdemo.common.async.Async
import com.github.emartynov.flickrdemo.common.http.FlickApi
import com.github.emartynov.flickrdemo.common.http.Http
import com.github.emartynov.flickrdemo.common.json.JsonParser
import com.github.emartynov.flickrdemo.imagelist.data.PageData
import java.util.concurrent.Callable

class LoadListUseCase(
    private val http: Http,
    private val jsonParser: JsonParser<PageData>,
    private val async: Async
) {
    fun loadPhotos(search: String, callback: (PageData) -> Unit) {
        async.queue(
            job = Callable {
                val data = http.get(FlickApi.getSearchUrl(search))
                jsonParser.parse(String(data))
            },
            callback = callback
        )
    }
}
