package com.example.evitegiphysample.rest

import com.example.evitegiphysample.api.ApiGifResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyService {

    @GET("v1/gifs/search")
    suspend fun searchGifs(@Query("api_key") api_key: String,
                                          @Query("q") q: String,
                                          @Query("limit") limit: Long,
                                          @Query("offset") offset: Long,
                                          @Query("rating") rating: String,
                                          @Query("lang") lang: String): Response<ApiGifResponse?>

    @GET("v1/gifs/search")
    fun searchGifsAsync(@Query("api_key") api_key: String,
                           @Query("q") q: String,
                           @Query("limit") limit: Long,
                           @Query("offset") offset: Long,
                           @Query("rating") rating: String,
                           @Query("lang") lang: String): Deferred<Response<ApiGifResponse?>>
}