package com.example.evitegiphysample.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.evitegiphysample.R
import com.example.evitegiphysample.api.ApiGif
import com.example.evitegiphysample.viewmodel.SearchGifViewModel
import kotlinx.android.synthetic.main.search_gif_fragment.*
import kotlinx.android.synthetic.main.search_toolbar.*

class SearchGifFragment : Fragment() {

    private lateinit var viewModel: SearchGifViewModel
    private var gifAdapter : GifsAdapter? = null

    private val gifListObserver = Observer<List<ApiGif>?>{
        loading_progress_bar?.visibility = View.GONE
        if(!it.isNullOrEmpty()) {
            gifAdapter?.setData(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_gif_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gifAdapter = GifsAdapter(view.context,{
            viewModel.onGifSelected(it)
        },{
            viewModel.loadMore()
        })
        gif_search_recycler_view?.adapter = gifAdapter
        val gridLayoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        gif_search_recycler_view?.layoutManager = gridLayoutManager
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupSearchToolbar()

        viewModel = ViewModelProviders.of(activity!!).get(SearchGifViewModel::class.java)

        viewModel.gifListLiveData.removeObserver(gifListObserver)
        viewModel.gifListLiveData.observe(this, gifListObserver)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val searchMenuItem = menu.findItem(R.id.action_search)
        searchMenuItem?.isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            viewModel.onSearchItemMenuClicked()
            showSearchAppBar()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSearchAppBar(){
        search_app_bar?.visibility = View.VISIBLE
        search_app_bar?.addOnLayoutChangeListener(object: View.OnLayoutChangeListener{
            override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                search_app_bar?.removeOnLayoutChangeListener(this)
                search_edit_text?.setText("")
                circleReveal( true)
            }
        })
    }

    private fun hideSearchBar(){
        search_app_bar?.visibility = View.GONE
        search_app_bar?.addOnLayoutChangeListener(object: View.OnLayoutChangeListener{
            override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                search_app_bar?.removeOnLayoutChangeListener(this)
                search_edit_text?.setText("")
                circleReveal( false)
            }
        })
    }

    private fun setupSearchToolbar(){
        search_edit_text?.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (search_edit_text?.text.toString().isEmpty()) {
                        search_clear?.visibility = View.GONE
                        voice_search?.visibility = View.VISIBLE
                    } else {
                        search_clear?.visibility = (View.VISIBLE)
                        voice_search?.visibility = (View.GONE)
                    }
                }

                override fun afterTextChanged(s: Editable) {}
            }
        )

        search_edit_text?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search_edit_text?.clearFocus()
                hideSoftKeyboard()
                viewModel.searchEvents(search_edit_text?.text.toString())
                loading_progress_bar?.visibility = View.VISIBLE
            }
            false
        }

        search_edit_text?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }

        search_back?.setOnClickListener { _ ->
            hideSoftKeyboard()
            viewModel.onBackClicked()
            hideSearchBar()
        }

        voice_search?.setOnClickListener { _ -> startVoiceRecognitionActivity() }

        search_clear?.setOnClickListener { _ ->
            search_edit_text?.setText("")
        }
    }

    private fun startVoiceRecognitionActivity() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, resources.getString(R.string.voice_search))
        startActivityForResult(intent,
            REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (matches?.isNotEmpty() == true) {
                val query = matches[0]
                search_edit_text?.setText(query)
                search_edit_text?.setSelection(search_edit_text!!.text.length)
                viewModel.searchEvents(query)
                loading_progress_bar?.visibility = View.VISIBLE
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun hideSoftKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun circleReveal(isShow: Boolean) {

        var width= events_search_toolbar.width

        width-=resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material)

        val cx = width
        val cy = events_search_toolbar.height / 2

        val anim: Animator
        anim = if (isShow)
            ViewAnimationUtils.createCircularReveal(events_search_toolbar, cx, cy, 0f, width.toFloat())
        else
            ViewAnimationUtils.createCircularReveal(events_search_toolbar, cx, cy, width.toFloat(), 0f)

        anim.duration = 220.toLong()

        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if (isShow) {
                    super.onAnimationEnd(animation)
                    search_edit_text?.requestFocus()
                }
            }
        })

        if (isShow)
            events_search_toolbar.visibility = View.VISIBLE

        anim.start()
    }

    companion object {
        private const val REQUEST_CODE = 1010
        fun newInstance() = SearchGifFragment()
    }
}
