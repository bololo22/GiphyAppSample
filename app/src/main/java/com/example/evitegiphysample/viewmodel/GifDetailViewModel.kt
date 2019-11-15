package com.example.evitegiphysample.viewmodel

import android.app.Application
import androidx.annotation.MainThread
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.evitegiphysample.api.ApiGif

class GifDetailViewModel  (application: Application) : AndroidViewModel(application){

    private val selectedGifMutableLiveData = MutableLiveData<ApiGif>()
    val selectedGifLiveData: LiveData<ApiGif> = selectedGifMutableLiveData

    @MainThread
    fun setSelectedGif(apiGif: ApiGif) {
        selectedGifMutableLiveData.value = apiGif
    }
}
