package com.olyaz.playlistmaker

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.olyaz.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val SEARCH_VALUE_KEY = "SEARCH_EDIT_TEXT_VALUE"

class SearchActivity : AppCompatActivity() {

    private var searchHistoryTracks =  ArrayList<Track>()
    private var searchStringValue: String? = null
    private lateinit var binding: ActivitySearchBinding

    private val iTunesSearchBaseUrl= "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesSearchBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesSearchService = retrofit.create(ITunesSearchAPI::class.java)
    private val listOfTracks = ArrayList<Track>()
    private lateinit var trackAdapter: TrackAdapter
    private val trackHistoryAdapter = TrackHistoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val searchHistory = SearchHistory(getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE))
        trackAdapter = TrackAdapter { track: Track ->
            searchHistory.addNewItemToHistory(track, trackHistoryAdapter)
        }

        searchHistoryTracks = searchHistory.readHistoryFromSharedPref()

        trackAdapter.tracks = listOfTracks
        binding.searchRecyclerView.adapter = trackAdapter

        trackHistoryAdapter.tracksHistory = searchHistoryTracks
        binding.searchHistoryRecyclerView.adapter = trackHistoryAdapter


        binding.searchEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus && binding.searchEditText.text.isEmpty() && searchHistoryTracks.isNotEmpty()) {
                binding.searchHistory.show()
            } else {
                binding.searchHistory.gone()
            }
        }

        binding.searchToolbar.setNavigationOnClickListener {
            finish()
        }

        if (savedInstanceState != null) {
            binding.searchEditText.setText(searchStringValue)
            binding.clearButton.isVisible = !binding.searchEditText.text.isNullOrEmpty()
        }

        binding.clearButton.setOnClickListener {
            binding.searchEditText.text.clear()
            binding.clearButton.hide()
            val inputManager = getSystemService<InputMethodManager>()
            inputManager?.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
            listOfTracks.clear()
            trackAdapter.notifyDataSetChanged()
            binding.searchRecyclerView.gone()
            binding.placeholderMessage.gone()
        }

        binding.searchEditText.addTextChangedListener(
            onTextChanged = {s: CharSequence?, start: Int, before: Int, count: Int ->
                searchStringValue = s.toString()
                binding.clearButton.isVisible = !searchStringValue.isNullOrEmpty()
                if (binding.searchEditText.hasFocus() && s?.isEmpty() == true && searchHistoryTracks.isNotEmpty()) {
                    binding.searchHistory.show()
                } else {
                    binding.searchHistory.gone()
                }
            }
        )

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchSongs()
                return@setOnEditorActionListener true
            }
            false
        }

        binding.placeholderButton.setOnClickListener {
            searchSongs()
        }

        binding.clearSearchHistoryButton.setOnClickListener {
            searchHistory.clearSearchHistory()
            trackHistoryAdapter.tracksHistory.clear()
            trackHistoryAdapter.notifyDataSetChanged()
            binding.searchHistory.gone()
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
        if (binding.searchEditText.text.isNotEmpty()) {
            iTunesSearchService.search(searchStringValue!!).enqueue(object : Callback<TrackResponse>{
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    if (response.code() == 200) {
                        binding.placeholderMessage.gone()
                        binding.searchRecyclerView.show()
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
        binding.searchRecyclerView.gone()
        binding.placeholderMessage.show()
        when (reason) {
            SearchError.EMPTY -> {
                binding.placeholderText.text = getString(R.string.nothing_found)
                binding.placeholderButton.gone()
                binding.placeholderImage.setImageResource(R.drawable.empty_search)
            }

            SearchError.CONNECTION -> {
                binding.placeholderText.text = getString(R.string.connection_error)
                binding.placeholderButton.show()
                binding.placeholderImage.setImageResource(R.drawable.connection_error)
            }
        }
    }

}