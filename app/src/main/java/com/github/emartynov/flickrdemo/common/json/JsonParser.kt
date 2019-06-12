package com.github.emartynov.flickrdemo.common.json

interface JsonParser<T> {
    fun parse(json: String): T
}
