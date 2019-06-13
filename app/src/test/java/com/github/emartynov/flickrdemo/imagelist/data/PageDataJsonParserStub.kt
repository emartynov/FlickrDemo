package com.github.emartynov.flickrdemo.imagelist.data

import com.github.emartynov.flickrdemo.common.json.JsonParser
import org.json.JSONObject

class PageDataJsonParserStub : JsonParser<PageData> {
    var stubbedData: PageData? = null
    var passedData: String? = null
        private set

    override fun parse(json: String): PageData {
        passedData = json
        return stubbedData ?: throw IllegalArgumentException("Stub parse data first")
    }

    override fun parse(jsonObject: JSONObject) = stubbedData ?: throw IllegalArgumentException("Stub parse data first")
}
