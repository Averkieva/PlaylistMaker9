package com.example.playlistmaker.presentation.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.*
import com.example.playlistmaker.app.App
import com.example.playlistmaker.data.dto.TrackResponse
import com.example.playlistmaker.data.network.ITunesApi
import com.example.playlistmaker.domain.SearchHistory
import com.example.playlistmaker.domain.models.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SearchingActivity : AppCompatActivity() {
    private lateinit var inputEditText: EditText
    private lateinit var nothingFoundPicture: ImageView
    private lateinit var nothingFoundText: TextView
    private lateinit var problemsWithLoadingPicture: ImageView
    private lateinit var problemsWithLoadingText: TextView
    private lateinit var refreshButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var tracksAdapter: TrackAdapter
    private lateinit var searchHistoryAdapter: TrackAdapter
    private lateinit var searchHistoryView: View
    private lateinit var searchHistoryRecyclerView: RecyclerView
    private lateinit var searchHistoryButton: Button
    private lateinit var historyLinearLayout: LinearLayout
    val searchHistory = SearchHistory()
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    lateinit var progressBar: ProgressBar

    companion object {
        const val QUERY = "QUERY"
        const val URL_API = "https://itunes.apple.com"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val DEBOUNCE_DELAY_3000L = 3000L
    }

    private val searchRunnable = Runnable { search(inputEditText) }

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL_API)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesApi::class.java)
    var tracks = ArrayList<Track>()
    var enterIsPressed:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.searchHistoryList = searchHistory.read()
        setContentView(R.layout.activity_searching)

        nothingFoundPicture = findViewById(R.id.nothingFoundPicture)
        nothingFoundText = findViewById(R.id.nothingFoundText)
        problemsWithLoadingPicture = findViewById(R.id.problemsWithLoadingPicture)
        problemsWithLoadingText = findViewById(R.id.problemsWithLoadingText)
        refreshButton = findViewById(R.id.refreshButton)
        searchHistoryView = findViewById(R.id.searchedText)
        recyclerView = findViewById(R.id.result_recyclerView)
        searchHistoryRecyclerView = findViewById(R.id.history_recyclerView)
        searchHistoryButton = findViewById(R.id.clearHistoryButton)
        historyLinearLayout = findViewById(R.id.hidingHistory)
        inputEditText = findViewById(R.id.inputEditText)
        progressBar = findViewById(R.id.progressBar)

        nothingFoundPicture.visibility = View.GONE
        nothingFoundText.visibility = View.GONE
        problemsWithLoadingPicture.visibility = View.GONE
        problemsWithLoadingText.visibility = View.GONE
        refreshButton.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        searchHistoryView.visibility = View.GONE
        searchHistoryRecyclerView.visibility = View.GONE
        searchHistoryButton.visibility = View.GONE
        historyLinearLayout.visibility = View.GONE

        tracksAdapter = TrackAdapter(tracks) {
            if (clickDebounce())
                clicker(it)
        }
        searchHistoryAdapter = TrackAdapter(App.searchHistoryList) {
            if (clickDebounce()) {
                clicker(it)
                searchHistoryAdapter.notifyDataSetChanged()
            }
        }
        recyclerView.adapter = tracksAdapter
        searchHistoryRecyclerView.adapter = searchHistoryAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        searchHistoryRecyclerView.layoutManager = LinearLayoutManager(this)

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty() && App.searchHistoryList.isNotEmpty()) {
                searchHistoryView.visibility = View.VISIBLE
                searchHistoryRecyclerView.visibility = View.VISIBLE
                searchHistoryButton.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                nothingFoundPicture.visibility = View.GONE
                nothingFoundText.visibility = View.GONE
                problemsWithLoadingPicture.visibility = View.GONE
                problemsWithLoadingText.visibility = View.GONE
                refreshButton.visibility = View.GONE
                historyLinearLayout.visibility = View.VISIBLE
            } else {
                searchHistoryView.visibility = View.GONE
                searchHistoryRecyclerView.visibility = View.GONE
                searchHistoryButton.visibility = View.GONE
            }
        }
        inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (inputEditText.hasFocus() && p0?.isEmpty() == true && App.searchHistoryList.isNotEmpty()) {
                    searchHistoryView.visibility = View.VISIBLE
                    searchHistoryRecyclerView.visibility = View.VISIBLE
                    searchHistoryButton.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    nothingFoundPicture.visibility = View.GONE
                    nothingFoundText.visibility = View.GONE
                    problemsWithLoadingPicture.visibility = View.GONE
                    problemsWithLoadingText.visibility = View.GONE
                    refreshButton.visibility = View.GONE
                    historyLinearLayout.visibility = View.VISIBLE
                } else {
                    searchHistoryView.visibility = View.GONE
                    searchHistoryRecyclerView.visibility = View.GONE
                    searchHistoryButton.visibility = View.GONE
                    historyLinearLayout.visibility = View.GONE
                }
                if (!inputEditText.text.isNullOrEmpty()) {
                    recyclerView.visibility = View.GONE
                    searchDebounce()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (inputEditText.text.isNotEmpty()) {
                    recyclerView.visibility = View.GONE
                    search(inputEditText)
                    enterIsPressed = true
                    handler.postDelayed({enterIsPressed = false}, DEBOUNCE_DELAY_3000L)
                }
                true
            }
            false
        }

        val backButton = findViewById<ImageView>(R.id.returnButton)
        backButton.setOnClickListener {
            this.finish()
        }
        searchHistoryButton.setOnClickListener {
            App.searchHistoryList.clear()
            searchHistoryView.visibility = View.GONE
            searchHistoryRecyclerView.visibility = View.GONE
            searchHistoryButton.visibility = View.GONE
            tracksAdapter.tracks.clear()
            tracksAdapter.notifyDataSetChanged()
            searchHistoryAdapter.notifyDataSetChanged()

        }
        val clearButton = findViewById<ImageView>(R.id.cancel_button)
        clearButton.setOnClickListener {
            inputEditText.setText("")
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            inputEditText.clearFocus()
            tracks.clear()
            recyclerView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            nothingFoundPicture.visibility = View.GONE
            nothingFoundText.visibility = View.GONE
            problemsWithLoadingPicture.visibility = View.GONE
            problemsWithLoadingText.visibility = View.GONE
            refreshButton.visibility = View.GONE

        }
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(QUERY, inputEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val searchQuery = savedInstanceState.getString(QUERY, "")
        inputEditText.setText(searchQuery)
    }

    override fun onResume() {
        super.onResume()
        isClickAllowed = true
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun search(inputEditText: EditText) { //функция поиска
        tracks.clear()
        if (!inputEditText.text.isNullOrEmpty()) {
            if (!enterIsPressed)
                progressBar.visibility = View.VISIBLE
            iTunesService.search(inputEditText.text.toString())
                .enqueue(object : Callback<TrackResponse> {
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        progressBar.visibility = View.GONE
                        if (response.code() == 200) {
                            tracks.clear()
                            recyclerView.visibility = View.VISIBLE
                            nothingFoundPicture.visibility = View.GONE
                            nothingFoundText.visibility = View.GONE
                            problemsWithLoadingPicture.visibility = View.GONE
                            problemsWithLoadingText.visibility = View.GONE
                            refreshButton.visibility = View.GONE
                            if (response.body()?.results?.isNotEmpty() == true) {
                                tracks.addAll(response.body()?.results!!)
                                tracksAdapter.notifyDataSetChanged()
                            }
                            if (tracksAdapter.tracks.isEmpty()) {
                                nothingFoundPicture.visibility = View.VISIBLE
                                nothingFoundText.visibility = View.VISIBLE
                                tracksAdapter.notifyDataSetChanged()
                            }
                            progressBar.visibility = View.GONE
                        } else {
                            problemsWithLoadingPicture.visibility = View.VISIBLE
                            problemsWithLoadingText.visibility = View.VISIBLE
                            refreshButton.setOnClickListener { search(inputEditText) }
                            refreshButton.visibility = View.VISIBLE
                            recyclerView.visibility = View.GONE
                            progressBar.visibility = View.GONE
                            tracksAdapter.notifyDataSetChanged()
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        problemsWithLoadingPicture.visibility = View.VISIBLE
                        problemsWithLoadingText.visibility = View.VISIBLE
                        refreshButton.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                        refreshButton.setOnClickListener { search(inputEditText) }
                        progressBar.visibility = View.GONE
                    }
                })
            return
        }
    }

    private fun clicker(item: Track) {
        val intent = Intent(this, AudioPlayerActivity::class.java)
        intent.putExtra(AudioPlayerActivity.KEY_TRACK_NAME, item.trackName)
        intent.putExtra(AudioPlayerActivity.KEY_TRACK_ARTIST, item.artistName)
        val trackTime = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(item.trackTimeMillis)
        intent.putExtra(AudioPlayerActivity.KEY_TRACK_TIME, trackTime)
        intent.putExtra(AudioPlayerActivity.KEY_TRACK_ALBUM, item.collectionName)
        intent.putExtra(AudioPlayerActivity.KEY_TRACK_YEAR, item.releaseDate)
        intent.putExtra(AudioPlayerActivity.KEY_TRACK_GENRE, item.primaryGenreName)
        intent.putExtra(AudioPlayerActivity.KEY_TRACK_COUNTRY, item.country)
        intent.putExtra(AudioPlayerActivity.KEY_TRACK_COVER, item.artworkUrl100)
        intent.putExtra(AudioPlayerActivity.URL, item.previewUrl)
        this.startActivity(intent)
        searchHistory.editSearchHistory(item)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

}