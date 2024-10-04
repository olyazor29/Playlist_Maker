package com.olyaz.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val USER_PREFERENCES = "app_preferences"
const val IS_DARK_THEME = "is_dark_theme"

class App : Application() {
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE)
        switchTheme(sharedPrefs.getBoolean(IS_DARK_THEME, false))
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}