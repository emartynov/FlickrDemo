package com.github.emartynov.flickrdemo.imagelist.data

import com.github.emartynov.flickrdemo.common.json.JsonParser
import org.json.JSONObject

class ImageDataJsonParser : JsonParser<ImageData> {
    override fun parse(jsonObject: JSONObject) =
        ImageData(
            id = jsonObject.getString("id"),
            secret = jsonObject.getString("secret"),
            server = jsonObject.getString("server"),
            farm = jsonObject.getInt("farm"),
            title = jsonObject.getString("title")
        )

    override fun parse(json: String) = parse(JSONObject(json))
}
