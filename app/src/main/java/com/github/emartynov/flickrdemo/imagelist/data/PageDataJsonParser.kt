package com.github.emartynov.flickrdemo.imagelist.data

import com.github.emartynov.flickrdemo.common.json.JsonParser
import org.json.JSONObject

class PageDataJsonParser(private val imageDataJsonParser: ImageDataJsonParser) : JsonParser<PageData> {
    override fun parse(jsonObject: JSONObject): PageData {
        val pagesJsonObject = jsonObject.getJSONObject("photos")
        val photosArray = pagesJsonObject.getJSONArray("photo")
        val parsedImages = mutableListOf<ImageData>()

        for (i in 0 until photosArray.length()) {
            val imageData = imageDataJsonParser.parse(photosArray.getJSONObject(i))
            parsedImages.add(imageData)
        }

        return PageData(
            page = pagesJsonObject.getInt("page"),
            totalPages = pagesJsonObject.getInt("pages"),
            images = parsedImages
        )
    }

    override fun parse(json: String) = parse(JSONObject(json))
}
