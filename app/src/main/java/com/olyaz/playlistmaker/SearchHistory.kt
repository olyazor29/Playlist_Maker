package com.olyaz.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val SEARCH_HISTORY = "search_history"

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    fun readHistoryFromSharedPref(): ArrayList<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY, null) ?: return ArrayList()
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun writeHistoryToSharedPref(searchHistory: ArrayList<Track>) {
        val json = Gson().toJson(searchHistory)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY, json)
            .apply()
    }

    fun addNewItemToHistory(newItem: Track, historyAdapter: TrackHistoryAdapter) {
        val history = readHistoryFromSharedPref()

        val existingIndex = history.indexOfFirst { it.trackId == newItem.trackId }

        if (existingIndex != -1) {
            history.removeAt(existingIndex)
            historyAdapter.notifyItemRemoved(existingIndex)
        } else if (history.size >= 10) {
            history.removeAt(history.size - 1)
            historyAdapter.notifyItemRemoved(history.size)
        }

        history.add(0, newItem)
        historyAdapter.notifyItemInserted(0)

        writeHistoryToSharedPref(history)
    }

    fun clearSearchHistory() {
        sharedPreferences.edit()
            .remove(SEARCH_HISTORY)
            .apply()
    }

}