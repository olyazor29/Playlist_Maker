package com.olyaz.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView
import com.olyaz.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPrefs = getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE)

        binding.settingsToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.shareTextView.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.course_link))
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, null))
        }

        binding.supportTextView.setOnClickListener {
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

        binding.agreementTextView.setOnClickListener {
            val agreementIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.agreement_link)))
            startActivity(agreementIntent)
        }

        binding.themeSwitch.isChecked = (applicationContext as App).darkTheme

        binding.themeSwitch.setOnCheckedChangeListener { switcher, isChecked ->
            (applicationContext as App).switchTheme(isChecked)
            sharedPrefs.edit()
                .putBoolean(IS_DARK_THEME, isChecked)
                .apply()
        }
    }
}