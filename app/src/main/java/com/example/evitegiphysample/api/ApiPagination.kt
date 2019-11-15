package com.example.evitegiphysample.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiPagination(
    @Json(name = "total_count") val total_count: Long,
    @Json(name = "count") val count: Long,
    @Json(name = "offset") val offset: Long
)