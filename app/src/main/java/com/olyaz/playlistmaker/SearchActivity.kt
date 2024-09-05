package com.olyaz.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.internal.ViewUtils.hideKeyboard

const val SEARCH_VALUE_KEY = "SEARCH_EDIT_TEXT_VALUE"
private var searchStringValue: String? = null

class SearchActivity : AppCompatActivity() {

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
            clearEditTextButton.visibility = View.VISIBLE
        }

        clearEditTextButton.setOnClickListener {
            searchEditText.text.clear()
            clearEditTextButton.visibility = View.INVISIBLE
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(searchEditText.windowToken, 0)
        }


        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchStringValue = s.toString()
                clearEditTextButton.visibility = View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }
        searchEditText.addTextChangedListener(searchTextWatcher)
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