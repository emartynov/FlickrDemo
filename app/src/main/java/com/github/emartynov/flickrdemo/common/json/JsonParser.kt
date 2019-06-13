package com.github.emartynov.flickrdemo.common.json

import org.json.JSONObject

interface JsonParser<T> {
    fun parse(json: String): T
    fun parse(jsonObject: JSONObject): T
}
