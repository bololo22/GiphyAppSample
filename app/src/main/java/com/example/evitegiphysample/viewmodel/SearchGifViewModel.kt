package com.example.evitegiphysample.viewmodel

import android.app.Application
import androidx.annotation.MainThread
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.evitegiphysample.GiphyRepository
import com.example.evitegiphysample.livedata.Event
import com.example.evitegiphysample.api.ApiGif
import com.example.evitegiphysample.api.ApiGifResponse
import com.example.evitegiphysample.api.ApiPagination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.math.E

class SearchGifViewModel (application: Application) : AndroidViewModel(application),
    CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val onSearchItemMenuClickedMutableLiveData = MutableLiveData<Event<Boolean>>()
    val onSearchItemMenuClickedLiveData: LiveData<Event<Boolean>> = onSearchItemMenuClickedMutableLiveData

    private val gifListMutableLiveData = MutableLiveData<List<ApiGif>?>()
    val gifListLiveData: LiveData<List<ApiGif>?> = gifListMutableLiveData

    private val selectedGifMutableLiveData = MutableLiveData<Event<ApiGif>>()
    val selectedGifLiveData: LiveData<Event<ApiGif>> = selectedGifMutableLiveData

    private val selectedGifPaginationMutableLiveData = MutableLiveData<ApiPagination?>()

    private val exampleMutableLiveData = MutableLiveData<List<ExampleData>>()
    val exampleLiveData : LiveData<List<ExampleData>> = exampleMutableLiveData

    fun createMockData()  {
        val list = listOf(
        ExampleData("Evento 1", "Descripcion 1", 20.00),
        ExampleData("Evento 2", "Descripcion 1", 20.00),
        ExampleData("Evento 3", "Descripcion 1", 20.00),
        ExampleData("Evento 4", "Descripcion 1", 20.00),
        ExampleData("Evento 5", "Descripcion 1", 20.00),
        ExampleData("Evento 6", "Descripcion 1", 20.00),
        ExampleData("Evento 7", "Descripcion 1", 20.00),
        ExampleData("Evento 8", "Descripcion 1", 20.00)
        )

        exampleMutableLiveData.postValue(list)
    }

    @MainThread
    fun onSearchItemMenuClicked() {
        onSearchItemMenuClickedMutableLiveData.value = Event(true)
    }

    @MainThread
    fun onBackClicked() {
        onSearchItemMenuClickedMutableLiveData.value = Event(false)
    }

    @MainThread
    fun searchEvents(searchQuery: String) {
        launch {
            try {
                GiphyRepository.searchQuery = searchQuery
                val result = GiphyRepository.searchGif()
                selectedGifPaginationMutableLiveData.value = result?.pagination
                gifListMutableLiveData.value = result?.data
            }catch (e: Exception){
                gifListMutableLiveData.value = null
                throw e
            }
        }
    }

    @MainThread
    fun onGifSelected(apiGif: ApiGif) {
        selectedGifMutableLiveData.value = Event(apiGif)
    }

    fun loadMore() {
        val currentOffset = selectedGifPaginationMutableLiveData.value?.offset
        val offset = if(currentOffset != null) currentOffset+20 else 0
        launch {
            try {
                val result = GiphyRepository.searchGif(offset)
                selectedGifPaginationMutableLiveData.value = result?.pagination
                val gifList: MutableList<ApiGif>? = mutableListOf()
                gifList?.addAll(gifListMutableLiveData.value?: mutableListOf())
                gifList?.addAll(result?.data?: mutableListOf())
                gifListMutableLiveData.value = gifList
            }catch (e: Exception){
                gifListMutableLiveData.value = null
                throw e
            }
        }

    }
}

data class ExampleData(val title: String, val description: String, val price: Double)