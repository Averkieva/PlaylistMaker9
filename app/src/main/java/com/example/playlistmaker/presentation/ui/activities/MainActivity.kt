package com.example.playlistmaker.presentation.ui.activities

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.playlistmaker.app.App
import com.example.playlistmaker.R

class MainActivity : AppCompatActivity() {
    companion object {
        const val PREFERENCE = "pref"
        const val CHOOSE_THEME_PREF = "darkTheme"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val searchButton = findViewById<Button>(R.id.search)
        val mediaButton = findViewById<Button>(R.id.media)
        val settingsButton = findViewById<Button>(R.id.settings)
        val sharedPrefs = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        val currentNight = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val darkTheme = sharedPrefs.getBoolean(
            CHOOSE_THEME_PREF,
            currentNight == Configuration.UI_MODE_NIGHT_YES
        )
        (applicationContext as App).switchTheme(darkTheme)
        searchButton.setOnClickListener {
            navigateTo(SearchingActivity::class.java)
        }
        mediaButton.setOnClickListener {
            navigateTo(MediatekaActivity::class.java)
        }
        settingsButton.setOnClickListener {
            navigateTo(SettingsActivity::class.java)
        }
    }

    private fun navigateTo(classes: Class<out AppCompatActivity>) {
        val intent = Intent(this, classes)
        startActivity(intent)
    }
}