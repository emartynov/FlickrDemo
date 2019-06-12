package com.github.emartynov.flickrdemo.imagelist.data

import com.github.emartynov.flickrdemo.common.json.JsonParser
import org.json.JSONObject

class ImageDataJsonParser : JsonParser<ImageData> {
    override fun parse(json: String): ImageData {
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
