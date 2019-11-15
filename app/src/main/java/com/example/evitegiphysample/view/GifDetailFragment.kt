package com.example.evitegiphysample.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.evitegiphysample.R
import com.example.evitegiphysample.api.ApiGif
import com.example.evitegiphysample.viewmodel.GifDetailViewModel
import kotlinx.android.synthetic.main.gif_detail_fragment.*

class GifDetailFragment : Fragment() {

    private lateinit var viewModel: GifDetailViewModel

    private val selectedGifObserver = Observer<ApiGif>{
        Glide.with(gif_iv.context).asGif().load(it.images.original.url).into(gif_iv)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.gif_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(GifDetailViewModel::class.java)
        viewModel.selectedGifLiveData.observe(this, selectedGifObserver)
    }

    companion object {
        fun newInstance() = GifDetailFragment()
    }

}
