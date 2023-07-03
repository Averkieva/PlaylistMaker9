package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val searchButton = findViewById<Button>(R.id.search)
        val mediaButton = findViewById<Button>(R.id.media)
        val settingsButton = findViewById<Button>(R.id.settings)

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