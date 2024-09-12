package com.olyaz.playlistmaker

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.olyaz.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val SEARCH_VALUE_KEY = "SEARCH_EDIT_TEXT_VALUE"

class SearchActivity : AppCompatActivity() {

    private var searchStringValue: String? = null
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchEditText: EditText
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var placeholderMessageView: LinearLayout
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderText: TextView
    private lateinit var placeholderButton: Button

    private val iTunesSearchBaseUrl= "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesSearchBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesSearchService = retrofit.create(ITunesSearchAPI::class.java)
    private val listOfTracks = ArrayList<Track>()
    private val trackAdapter = TrackAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        searchEditText = binding.searchEditText
        searchRecyclerView = binding.searchRecyclerView
        placeholderMessageView = binding.placeholderMessage
        placeholderImage = binding.placeholderImage
        placeholderText = binding.placeholderText
        placeholderButton = binding.placeholderButton

        trackAdapter.tracks = listOfTracks
        searchRecyclerView.adapter = trackAdapter

        binding.searchToolbar.setNavigationOnClickListener {
            finish()
        }

        if (savedInstanceState != null) {
            searchEditText.setText(searchStringValue)
            binding.clearButton.isVisible = !binding.searchEditText.text.isNullOrEmpty()
        }

        binding.clearButton.setOnClickListener {
            binding.searchEditText.text.clear()
            binding.clearButton.visibility = View.INVISIBLE
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
            searchRecyclerView.visibility = View.GONE
            placeholderMessageView.visibility = View.GONE
            listOfTracks.clear()
            trackAdapter.notifyDataSetChanged()
        }

        searchEditText.addTextChangedListener(
            onTextChanged = {s: CharSequence?, start: Int, before: Int, count: Int ->
                searchStringValue = s.toString()
                binding.clearButton.isVisible = !searchStringValue.isNullOrEmpty()
            }
        )

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchSongs()
                true
            }
            false
        }

        placeholderButton.setOnClickListener {
            searchSongs()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_VALUE_KEY,searchStringValue)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchStringValue = savedInstanceState.getString(SEARCH_VALUE_KEY)
    }

    private fun searchSongs() {
        if (searchEditText.text.isNotEmpty()) {
            iTunesSearchService.search(searchStringValue!!).enqueue(object : Callback<TrackResponse>{
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    if (response.code() == 200) {
                        placeholderMessageView.visibility = View.GONE
                        searchRecyclerView.visibility = View.VISIBLE
                        listOfTracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            listOfTracks.addAll(response.body()?.results!!)
                            trackAdapter.notifyDataSetChanged()
                        }
                        if (listOfTracks.isEmpty()) {
                            showMessage(SearchError.EMPTY)
                        }
                    } else {
                        showMessage(SearchError.CONNECTION)
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    showMessage(SearchError.CONNECTION)
                }

            })
        }
    }

    private fun showMessage(reason: SearchError) {
        searchRecyclerView.visibility = View.GONE
        placeholderMessageView.visibility = View.VISIBLE
        when (reason) {
            SearchError.EMPTY -> {
                placeholderText.text = getString(R.string.nothing_found)
                placeholderButton.visibility = View.GONE
                placeholderImage.setImageResource(R.drawable.empty_search)
            }

            SearchError.CONNECTION -> {
                placeholderText.text = getString(R.string.connection_error)
                placeholderButton.visibility = View.VISIBLE
                placeholderImage.setImageResource(R.drawable.connection_error)
            }
        }
    }
}