package com.github.emartynov.flickrdemo.imagelist.data

data class PageData(
    val images: List<ImageData>,
    val page: Int,
    val totalPages: Int
)
