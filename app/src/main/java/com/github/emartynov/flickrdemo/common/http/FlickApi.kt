package com.github.emartynov.flickrdemo.common.http

import androidx.annotation.VisibleForTesting

@VisibleForTesting
const val FLICKR_KEY = "3e7cc266ae2b0e0d78e279ce8e361736"

object FlickApi {
    fun getSearchUrl(searchString: String) =
        "https://api.flickr.com/services/rest/" +
                "?method=flickr.photos.search" +
                "&api_key=$FLICKR_KEY" +
                "&format=json" +
                "&nojsoncallback=1" +
                "&safe_search=1" +
                "&text=$searchString"
}
