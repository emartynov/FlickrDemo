package com.github.emartynov.flickrdemo.common.http

import androidx.annotation.VisibleForTesting
import com.github.emartynov.flickrdemo.imagelist.data.ImageData

@VisibleForTesting
const val FLICKR_KEY = "3e7cc266ae2b0e0d78e279ce8e361736"

object FlickApi {
    fun getSearchUrl(searchString: String, page: Int) =
        "https://api.flickr.com/services/rest/" +
                "?method=flickr.photos.search" +
                "&api_key=$FLICKR_KEY" +
                "&format=json" +
                "&nojsoncallback=1" +
                "&safe_search=1" +
                "&text=$searchString" +
                "&page=$page"

    fun getImageUrl(imageData: ImageData) =
        "https://farm${imageData.farm}.static.flickr.com/${imageData.server}/${imageData.id}_${imageData.secret}.jpg"
}
