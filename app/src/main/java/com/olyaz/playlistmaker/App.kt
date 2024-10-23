package com.olyaz.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

const val USER_PREFERENCES = "app_preferences"
const val IS_DARK_THEME = "is_dark_theme"

class App : Application() {
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE)
        if (!sharedPrefs.contains(IS_DARK_THEME)) {
            val isSystemThemeDark = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            switchTheme(isSystemThemeDark)
            sharedPrefs.edit()
                .putBoolean(IS_DARK_THEME, isSystemThemeDark)
                .apply()
        } else {
            switchTheme(sharedPrefs.getBoolean(IS_DARK_THEME, false))
        }
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