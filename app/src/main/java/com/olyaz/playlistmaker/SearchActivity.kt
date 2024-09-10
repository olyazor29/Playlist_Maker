package com.olyaz.playlistmaker

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.olyaz.playlistmaker.databinding.ActivitySearchBinding


private const val SEARCH_VALUE_KEY = "SEARCH_EDIT_TEXT_VALUE"


class SearchActivity : AppCompatActivity() {

    private var searchStringValue: String? = null
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mockListOfTracks: ArrayList<Track> = arrayListOf(
            Track(
                "Smells Like Teen Spirit",
                "Nirvana",
                "5:01",
                "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"),
            Track(
                "Billie Jean",
                "Michael Jackson",
                "4:35",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"),
            Track(
                "Stayin' Alive",
                "Bee Gees",
                "4:10",
                "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"),
            Track(
                "Whole Lotta Love",
                "Led Zeppelin",
                "5:01",
                "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"),
            Track(
                "Sweet Child O'Mine",
                "Guns N' Roses",
                "5:03",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"),
            )


        binding.searchToolbar.setNavigationOnClickListener {
            finish()
        }

        if (savedInstanceState != null) {
            binding.searchEditText.setText(searchStringValue)
            binding.clearButton.isVisible = !binding.searchEditText.text.isNullOrEmpty()
        }

        binding.clearButton.setOnClickListener {
            binding.searchEditText.text.clear()
            binding.clearButton.visibility = View.INVISIBLE
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
        }

        binding.searchEditText.addTextChangedListener(
            onTextChanged = {s: CharSequence?, start: Int, before: Int, count: Int ->
                searchStringValue = s.toString()
                binding.clearButton.isVisible = !searchStringValue.isNullOrEmpty()
            }
        )

        val searchRecyclerView = findViewById<RecyclerView>(R.id.searchRecyclerView)
        val trackAdapter = TrackAdapter(mockListOfTracks)
        searchRecyclerView.adapter = trackAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_VALUE_KEY,searchStringValue)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchStringValue = savedInstanceState.getString(SEARCH_VALUE_KEY)
    }
}