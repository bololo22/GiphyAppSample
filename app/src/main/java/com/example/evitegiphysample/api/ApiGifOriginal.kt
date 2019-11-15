package com.example.evitegiphysample.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiGifOriginal(@Json(name = "url") val url: String,
                       @Json(name = "webp") val webp: String
)