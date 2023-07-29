package com.example.playlistmaker

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class AudioPlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_player)

        val audioPlayerTrackName = findViewById<TextView>(R.id.audioPlayerTrackName)
        val audioPlayerArtistName = findViewById<TextView>(R.id.audioPlayerArtistName)
        val audioPlayerTime = findViewById<TextView>(R.id.audioPlayerTime)
        val audioPlayerAlbumName = findViewById<TextView>(R.id.audioPlayerAlbumName)
        val audioPlayerYearNumber = findViewById<TextView>(R.id.audioPlayerYearNumber)
        val audioPlayerGenreName = findViewById<TextView>(R.id.audioPlayerGenreName)
        val audioPlayerCountryName = findViewById<TextView>(R.id.audioPlayerCountryName)
        val audioPlayerCover = findViewById<ImageView>(R.id.audioPlayerCover)
        val audioPlayerBackButton = findViewById<ImageButton>(R.id.audioPlayerBackButton)
        audioPlayerBackButton.setOnClickListener {
            finish()
        }

        audioPlayerTrackName.text = intent.extras?.getString("Track Name") ?: "Unknown Track"
        audioPlayerArtistName.text = intent.extras?.getString("Artist Name") ?: "Unknown Artist"
        audioPlayerTime.text = intent.extras?.getString("Track Time") ?: "12:34"
        audioPlayerAlbumName.text = intent.extras?.getString("Album") ?: "Unknown Album"
        audioPlayerYearNumber.text = (intent.extras?.getString("Year") ?: "Year").take(4)
        audioPlayerGenreName.text = intent.extras?.getString("Genre") ?: "Unknown Genre"
        audioPlayerCountryName.text = intent.extras?.getString("Country") ?: "Unknown Country"

        val image = (intent.extras?.getString("Cover") ?: "Unknown Cover").replace("100x100bb.jpg", "512x512bb.jpg")

        if (image != "Unknown Cover") {
            image.replace("100x100bb.jpg", "512x512bb.jpg")
            Glide.with(this)
                .load(image)
                .placeholder(R.drawable.audio_player_cover)
                .into(audioPlayerCover)
        }


    }
}