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


private const val SEARCH_VALUE_KEY = "SEARCH_EDIT_TEXT_VALUE"


class SearchActivity : AppCompatActivity() {

    private var searchStringValue: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchToolBar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.searchToolbar)

        searchToolBar.setNavigationOnClickListener {
            finish()
        }

        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val clearEditTextButton = findViewById<ImageButton>(R.id.clearButton)

        if (savedInstanceState != null) {
            searchEditText.setText(searchStringValue)
            clearEditTextButton.isVisible = !searchEditText.text.isNullOrEmpty()
        }

        clearEditTextButton.setOnClickListener {
            searchEditText.text.clear()
            clearEditTextButton.visibility = View.INVISIBLE
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(searchEditText.windowToken, 0)
        }

        searchEditText.addTextChangedListener(
            onTextChanged = {s: CharSequence?, start: Int, before: Int, count: Int ->
                searchStringValue = s.toString()
                clearEditTextButton.isVisible = !searchStringValue.isNullOrEmpty()
            }
        )
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