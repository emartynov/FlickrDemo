package com.github.emartynov.flickrdemo.imagelist.data

import org.json.JSONObject

class ImageDataParser {
    fun parse(json: String): ImageData {
        val jsonObject = JSONObject(json)

        return ImageData(
            id = jsonObject.getString("id"),
            secret = jsonObject.getString("secret"),
            server = jsonObject.getString("server"),
            farm = jsonObject.getInt("farm"),
            title = jsonObject.getString("title")
        )
    }
}
