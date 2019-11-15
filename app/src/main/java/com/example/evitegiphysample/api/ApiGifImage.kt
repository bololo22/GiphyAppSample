package com.example.evitegiphysample.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiGifImage(@Json(name = "original") val original: ApiGifOriginal,
                       @Json(name = "preview_gif") val preview_gif: ApiGifPreview,
                       @Json(name = "downsized") val downsized: ApiGifDownsized
)