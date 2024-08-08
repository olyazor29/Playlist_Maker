package com.olyaz.playlistmaker

import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val settingToolBar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.settings_Toolbar)

        settingToolBar.setNavigationOnClickListener {
            finish()
        }
    }

}