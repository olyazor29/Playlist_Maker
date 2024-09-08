package com.olyaz.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val settingsToolBar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.settings_Toolbar)

        settingsToolBar.setNavigationOnClickListener {
            finish()
        }

        val shareTextView = findViewById<MaterialTextView>(R.id.shareTextView)
        shareTextView.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.course_link))
                type = "text/plain"
            }

            startActivity(Intent.createChooser(shareIntent, null))
        }

        val supportTextView = findViewById<MaterialTextView>(R.id.supportTextView)
        supportTextView.setOnClickListener {
            val supportIntent = Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:${getString(R.string.student_email)}")
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.support_email_text))
            }

            if (supportIntent.resolveActivity(packageManager) != null) {
                startActivity(supportIntent)
            } else {
                Toast.makeText(this, getString(R.string.mail_error), Toast.LENGTH_SHORT).show()
            }


        }

        val agreementTextView = findViewById<MaterialTextView>(R.id.agreementTextView)
        agreementTextView.setOnClickListener {
            val agreementIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.agreement_link)))
            startActivity(agreementIntent)
        }


        val themeSwitch = findViewById<SwitchMaterial>(R.id.themeSwitch)
        themeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                themeSwitch.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                themeSwitch.isChecked = false
            }
        }
    }
}