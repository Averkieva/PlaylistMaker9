package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val backButton = findViewById<ImageView>(R.id.returnButton)
        backButton.setOnClickListener {
            this.finish()
        }
        val shareButton = findViewById<TextView>(R.id.share)
        shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.android_developer_url))
            startActivity(intent)
        }
        val supportButton = findViewById<TextView>(R.id.support)
        supportButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.email))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.sub))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.txt))
            startActivity(intent)
        }
        val userPolicyButton = findViewById<TextView>(R.id.userPolicy)
        userPolicyButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = (Uri.parse(getString(R.string.practicum_offer)))
            startActivity(intent)
        }
    }
}