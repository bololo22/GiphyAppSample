package com.example.evitegiphysample.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiGifResponse(
    @Json(name = "data") val data: List<ApiGif>,
    @Json(name = "pagination") val pagination: ApiPagination
)