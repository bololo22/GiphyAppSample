package com.example.evitegiphysample.rest

import com.example.evitegiphysample.api.ApiGifResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

class GiphyClient {
    private val giphyService: GiphyService by lazy {
        val httpClient = OkHttpClient.Builder()

        val moshi = Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .build()

        return@lazy Retrofit
            .Builder()
            .baseUrl("https://api.giphy.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(httpClient.build())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(GiphyService::class.java)
    }

    suspend fun searchGifs(searchGif: String,
                           api_key : String = "RLKO87ZlMJSnTN9fXR2HBcVkXtI16BIJ",
                           quantitySearch: Long = 20,
                           offset: Long = 0,
                           rating: String = "G",
                           lang: String = "en") : Response<ApiGifResponse?> = withContext(Dispatchers.IO) {
        return@withContext giphyService.searchGifs(api_key, searchGif, quantitySearch, offset, rating, lang)
    }

    suspend fun searchGifsAsync(searchGif: String,
                           api_key : String = "RLKO87ZlMJSnTN9fXR2HBcVkXtI16BIJ",
                           quantitySearch: Long = 20,
                           offset: Long = 0,
                           rating: String = "G",
                           lang: String = "en") : Response<ApiGifResponse?> = withContext(Dispatchers.IO) {
        return@withContext giphyService.searchGifsAsync(api_key, searchGif, quantitySearch, offset, rating, lang).await()
    }
}