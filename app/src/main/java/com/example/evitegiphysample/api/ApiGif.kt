package com.example.evitegiphysample.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class ApiGif(@Json(name = "type") val type: String,
                  @Json(name = "id") val id: String,
                  @Json(name = "title") val title: String,
                  @Json(name = "import_datetime") val import_datetime: String,
                  @Json(name = "images") val images: ApiGifImage
)