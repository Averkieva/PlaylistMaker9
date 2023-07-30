package com.example.playlistmaker

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var audioPlayerTrackName:TextView
    private lateinit var audioPlayerArtistName:TextView
    private lateinit var audioPlayerTime:TextView
    private lateinit var audioPlayerAlbumName:TextView
    private lateinit var audioPlayerYearNumber:TextView
    private lateinit var audioPlayerGenreName:TextView
    private lateinit var audioPlayerCountryName:TextView
    private lateinit var audioPlayerCover:ImageView
    private lateinit var audioPlayerBackButton:ImageButton

    companion object{
        const val KEY_TRACK_NAME = "Track Name"
        const val KEY_TRACK_ARTIST = "Artist Name"
        const val KEY_TRACK_TIME = "Track Time"
        const val KEY_TRACK_ALBUM = "Album"
        const val KEY_TRACK_YEAR = "Year"
        const val KEY_TRACK_GENRE = "Genre"
        const val KEY_TRACK_COUNTRY = "Country"
        const val KEY_TRACK_COVER = "Cover"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_player)
        initViews()
        audioPlayerBackButton.setOnClickListener {
            finish()
        }

        audioPlayerTrackName.text = intent.extras?.getString(KEY_TRACK_NAME) ?: "Unknown Track"
        audioPlayerArtistName.text = intent.extras?.getString(KEY_TRACK_ARTIST) ?: "Unknown Artist"
        audioPlayerTime.text = intent.extras?.getString(KEY_TRACK_TIME) ?: "12:34"
        audioPlayerAlbumName.text = intent.extras?.getString(KEY_TRACK_ALBUM) ?: "Unknown Album"
        audioPlayerYearNumber.text = (intent.extras?.getString(KEY_TRACK_YEAR) ?: "Year").take(4)
        audioPlayerGenreName.text = intent.extras?.getString(KEY_TRACK_GENRE) ?: "Unknown Genre"
        audioPlayerCountryName.text = intent.extras?.getString(KEY_TRACK_COUNTRY) ?: "Unknown Country"

        val image = (intent.extras?.getString(KEY_TRACK_COVER))

        if (image != null) {
            image.replace("100x100bb.jpg", "512x512bb.jpg")
            Glide.with(this)
                .load(image)
                .placeholder(R.drawable.audio_player_cover)
                .into(audioPlayerCover)
        }


    }
    private fun initViews(){
        audioPlayerTrackName = findViewById(R.id.audioPlayerTrackName)
        audioPlayerArtistName = findViewById(R.id.audioPlayerArtistName)
        audioPlayerTime = findViewById(R.id.audioPlayerTime)
        audioPlayerAlbumName = findViewById(R.id.audioPlayerAlbumName)
        audioPlayerYearNumber = findViewById(R.id.audioPlayerYearNumber)
        audioPlayerGenreName = findViewById(R.id.audioPlayerGenreName)
        audioPlayerCountryName = findViewById(R.id.audioPlayerCountryName)
        audioPlayerCover = findViewById(R.id.audioPlayerCover)
        audioPlayerBackButton = findViewById(R.id.audioPlayerBackButton)
    }
}