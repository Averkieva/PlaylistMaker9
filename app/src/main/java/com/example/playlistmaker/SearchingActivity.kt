package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchingActivity : AppCompatActivity() {
    private lateinit var inputEditText: EditText
    private lateinit var nothingFoundPicture: ImageView
    private lateinit var nothingFoundText: TextView
    private lateinit var problemsWithLoadingPicture: ImageView
    private lateinit var problemsWithLoadingText: TextView
    private lateinit var refreshButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var tracksAdapter: TrackAdapter


    companion object {
        const val QUERY = "QUERY"
        const val URL_API = "https://itunes.apple.com"
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL_API)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesApi::class.java)
    var tracks = ArrayList<Track>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searching)
        nothingFoundPicture = findViewById(R.id.nothingFoundPicture)
        nothingFoundText = findViewById(R.id.nothingFoundText)
        problemsWithLoadingPicture = findViewById(R.id.problemsWithLoadingPicture)
        problemsWithLoadingText = findViewById(R.id.problemsWithLoadingText)
        refreshButton = findViewById(R.id.refreshButton)
        nothingFoundPicture.visibility = View.GONE
        nothingFoundText.visibility = View.GONE
        problemsWithLoadingPicture.visibility = View.GONE
        problemsWithLoadingText.visibility = View.GONE
        refreshButton.visibility = View.GONE
        recyclerView = findViewById<RecyclerView>(R.id.result_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        tracksAdapter = TrackAdapter(tracks)
        recyclerView.adapter = tracksAdapter
        inputEditText = findViewById(R.id.inputEditText)
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (inputEditText.text.isNotEmpty()){
                    iTunesService.search(inputEditText.text.toString()).enqueue(object : Callback<TrackResponse> {
                        override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
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
                            } else {
                                problemsWithLoadingPicture.visibility = View.VISIBLE
                                problemsWithLoadingText.visibility = View.VISIBLE
                                refreshButton.setOnClickListener { search(inputEditText) }
                                refreshButton.visibility = View.VISIBLE
                                recyclerView.visibility = View.GONE
                                tracksAdapter.notifyDataSetChanged()
                            }
                        }

                        override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                            problemsWithLoadingPicture.visibility = View.VISIBLE
                            problemsWithLoadingText.visibility = View.VISIBLE
                            refreshButton.visibility = View.VISIBLE
                            recyclerView.visibility = View.GONE
                            refreshButton.setOnClickListener { search(inputEditText) }
                        }
                    })

                }
                    true
            }
            false
        }
        val backButton = findViewById<ImageView>(R.id.returnButton)
        backButton.setOnClickListener {
            this.finish()
        }
        val clearButton = findViewById<ImageView>(R.id.cancel_button)
        clearButton.setOnClickListener {
            inputEditText.setText("")
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            inputEditText.clearFocus()
            tracks.clear()
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

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun search(inputEditText: EditText) { //функция поиска
        tracks.clear()
        nothingFoundPicture.visibility = View.GONE
        nothingFoundText.visibility = View.GONE
        problemsWithLoadingPicture.visibility = View.GONE
        problemsWithLoadingText.visibility = View.GONE
        refreshButton.visibility = View.GONE
        iTunesService.search(inputEditText.text.toString()).enqueue(object : Callback<TrackResponse> {
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
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
                } else {
                    problemsWithLoadingPicture.visibility = View.VISIBLE
                    problemsWithLoadingText.visibility = View.VISIBLE
                    refreshButton.setOnClickListener { search(inputEditText) }
                    refreshButton.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    tracksAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                problemsWithLoadingPicture.visibility = View.VISIBLE
                problemsWithLoadingText.visibility = View.VISIBLE
                refreshButton.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                refreshButton.setOnClickListener { search(inputEditText) }
            }
        })
    }

}