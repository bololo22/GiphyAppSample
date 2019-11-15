package com.example.evitegiphysample.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiGifDownsized(@Json(name = "url") val url: String
)