package com.example.playlistmaker

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

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
        const val year = 4
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_player)
        initViews()
        audioPlayerBackButton.setOnClickListener {
            finish()
        }

        audioPlayerTrackName.text = intent.extras?.getString(KEY_TRACK_NAME) ?: getString(R.string.unknownTrack)
        audioPlayerArtistName.text = intent.extras?.getString(KEY_TRACK_ARTIST) ?: getString(R.string.unknownArtist)
        audioPlayerTime.text = intent.extras?.getString(KEY_TRACK_TIME) ?: getString(R.string.track_time)
        audioPlayerAlbumName.text = intent.extras?.getString(KEY_TRACK_ALBUM) ?: getString(R.string.unknownAlbum)
        audioPlayerYearNumber.text = (intent.extras?.getString(KEY_TRACK_YEAR) ?: getString(R.string.unknownYear)).take(year)
        audioPlayerGenreName.text = intent.extras?.getString(KEY_TRACK_GENRE) ?: getString(R.string.unknownGenre)
        audioPlayerCountryName.text = intent.extras?.getString(KEY_TRACK_COUNTRY) ?: getString(R.string.unknownCountry)

        val image = (intent.extras?.getString(KEY_TRACK_COVER))

        if (image != null) {
            image.replace("100x100bb.jpg", "512x512bb.jpg")
            Glide.with(this)
                .load(image)
                .placeholder(R.drawable.audio_player_cover)
                .centerCrop()
                .transform(RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.icon_padding)))
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