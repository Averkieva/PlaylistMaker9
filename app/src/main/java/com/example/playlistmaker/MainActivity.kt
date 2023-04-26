package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sBut = findViewById<Button>(R.id.search)
        val mBut = findViewById<Button>(R.id.media)
        val setBut = findViewById<Button>(R.id.settings)

        sBut.setOnClickListener {
            navigateTo(SearchingActivity::class.java)
        }
        mBut.setOnClickListener {
            navigateTo(MediatekaActivity::class.java)
        }
        setBut.setOnClickListener {
            navigateTo(SettingsActivity::class.java)
        }
    }

    private fun navigateTo(clazz: Class<out AppCompatActivity>) {
        val intent = Intent(this, clazz)
        startActivity(intent)
    }
}