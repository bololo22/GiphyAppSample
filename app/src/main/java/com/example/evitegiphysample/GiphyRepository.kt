package com.example.evitegiphysample

import com.example.evitegiphysample.api.ApiGifResponse
import com.example.evitegiphysample.rest.GiphyClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GiphyRepository {
    private var client: GiphyClient =
        GiphyClient()
    var searchQuery = ""

    suspend fun searchGif(offset: Long = 0): ApiGifResponse? = withContext(Dispatchers.Default) {
        val gifResponse = client.searchGifs( searchQuery, offset = offset)
        return@withContext if (gifResponse.isSuccessful) {
            gifResponse.body()
        } else {
            null
        }
    }
}