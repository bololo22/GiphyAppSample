package com.example.evitegiphysample.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.evitegiphysample.R
import com.example.evitegiphysample.livedata.EventObserver
import com.example.evitegiphysample.viewmodel.GifDetailViewModel
import com.example.evitegiphysample.viewmodel.SearchGifViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var searchGifViewModel: SearchGifViewModel
    private lateinit var gifDetailViewModel: GifDetailViewModel
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchGifViewModel = ViewModelProviders.of(this).get(SearchGifViewModel::class.java)
        gifDetailViewModel = ViewModelProviders.of(this).get(GifDetailViewModel::class.java)
        setupSearchGifObservers()

        navHostFragment = main_container as NavHostFragment
        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.gifDetailFragment -> {
                    supportActionBar?.show()
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                }
                R.id.gifSearchFragment -> {
                    supportActionBar?.title = destination.label
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
                else -> {
                    supportActionBar?.show()
                }
            }
        }
    }

    override fun onSupportNavigateUp() = findNavController(this,
        R.id.main_container
    ).navigateUp()

    private fun setupSearchGifObservers(){
        searchGifViewModel.onSearchItemMenuClickedLiveData.observe(this, EventObserver {
            if(it) supportActionBar?.hide() else supportActionBar?.show()
        })

        searchGifViewModel.selectedGifLiveData.observe(this, EventObserver {
            supportActionBar?.title = it.title
            this.findNavController(R.id.main_container).navigate(R.id.action_gifSearchFragment_to_gifDetailFragment)
            gifDetailViewModel.setSelectedGif(it)
        })
    }
}
