package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat

class AudioPlayerActivity : AppCompatActivity() {

    enum class StatesOfPlaying{
        STATE_DEFAULT,
        STATE_PREPARED,
        STATE_PLAYING,
        STATE_PAUSED
    }

    private lateinit var audioPlayerTrackName:TextView
    private lateinit var audioPlayerArtistName:TextView
    private lateinit var audioPlayerTime:TextView
    private lateinit var audioPlayerAlbumName:TextView
    private lateinit var audioPlayerYearNumber:TextView
    private lateinit var audioPlayerGenreName:TextView
    private lateinit var audioPlayerCountryName:TextView
    private lateinit var audioPlayerCover:ImageView
    private lateinit var audioPlayerBackButton:ImageButton
    private lateinit var audioPlayerPlayButton:ImageButton
    private lateinit var audioPlayerPauseButton:ImageButton
    private lateinit var audioPlayerTrackTimer:TextView

    private val mediaPlayer = MediaPlayer()
    private var mainThreadHandler: Handler? = null
    private var statesOfPlaying = StatesOfPlaying.STATE_DEFAULT
    var time = ""

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
        const val URL = "Url"
        const val AUDIO_DELAY_MILLIS = 100L
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_player)
        initViews()
        audioPlayerPlayButton.setOnClickListener { playBackControl() }
        audioPlayerPauseButton.setOnClickListener { playBackControl() }
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

        val url = intent.extras?.getString(URL)
        if (!url.isNullOrEmpty())
            preparePlayer(url)
    }
    private fun preparePlayer(url: String){
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener{
            audioPlayerPlayButton.isEnabled = true
            statesOfPlaying = StatesOfPlaying.STATE_PREPARED
            audioPlayerPlayButton.visibility = View.VISIBLE
            audioPlayerPauseButton.visibility = View.GONE
        }
        mediaPlayer.setOnCompletionListener{
            statesOfPlaying = StatesOfPlaying.STATE_PREPARED
            audioPlayerPlayButton.visibility = View.VISIBLE
            audioPlayerPauseButton.visibility = View.GONE
        }
    }

    private fun startPlayer(){
        mediaPlayer.start()
        statesOfPlaying = StatesOfPlaying.STATE_PLAYING
        audioPlayerPlayButton.visibility = View.GONE
        audioPlayerPauseButton.visibility = View.VISIBLE
        mainThreadHandler?.post(duration())
    }

    private fun pausePlayer(){
        super.onPause()
        mediaPlayer.pause()
        statesOfPlaying = StatesOfPlaying.STATE_PAUSED
        audioPlayerPlayButton.visibility = View.VISIBLE
        audioPlayerPauseButton.visibility = View.GONE
    }

    private fun playBackControl(){
        when (statesOfPlaying){
            StatesOfPlaying.STATE_PLAYING->{
                pausePlayer()
            }
            StatesOfPlaying.STATE_PREPARED, StatesOfPlaying.STATE_PAUSED->{
                startPlayer()
            }
            else->{}
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayer.isPlaying)
            mediaPlayer.pause()
        statesOfPlaying = StatesOfPlaying.STATE_PAUSED
        audioPlayerPlayButton.visibility = View.VISIBLE
        audioPlayerPauseButton.visibility = View.GONE
    }

    private fun duration():Runnable{
        return object:Runnable{
            override fun run(){
                if (statesOfPlaying==StatesOfPlaying.STATE_PLAYING){
                    time = SimpleDateFormat("mm:ss").format(mediaPlayer.currentPosition)
                    audioPlayerTrackTimer.text = time
                    mainThreadHandler?.postDelayed(this, AUDIO_DELAY_MILLIS)
                }
                else{
                    audioPlayerTrackTimer.text = "00:00"
                    mainThreadHandler?.post(this)
                }
            }
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
        audioPlayerPlayButton = findViewById(R.id.audioPlayerPlayButton)
        audioPlayerPauseButton = findViewById(R.id.audioPlayerPauseButton)
        audioPlayerTrackTimer = findViewById(R.id.audioPlayerTrackTimer)
        mainThreadHandler = Handler(Looper.getMainLooper())
    }
}