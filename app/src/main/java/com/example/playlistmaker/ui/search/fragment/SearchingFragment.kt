package com.example.playlistmaker.ui.search.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchingBinding
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.player.activity.AudioPlayerActivity
import com.example.playlistmaker.ui.search.adapter.TrackAdapter
import com.example.playlistmaker.ui.search.view_model.ViewModelSearching
import com.example.playlistmaker.ui.search.view_model.states.StatesOfSearching
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchingFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var tracksAdapter: TrackAdapter
    private lateinit var searchHistoryAdapter: TrackAdapter
    private lateinit var searchHistoryRecyclerView: RecyclerView
    private lateinit var binding: FragmentSearchingBinding
    private lateinit var bottomNavigator: BottomNavigationView

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val viewModelSearching by viewModel<ViewModelSearching>()


    companion object {
        const val QUERY = "QUERY"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val DEBOUNCE_DELAY_3000L = 3000L
    }

    private val searchRunnable = Runnable { search() }

    var enterIsPressed: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)

        viewModelSearching.getSearchingLiveData().observe(viewLifecycleOwner) { searchLiveData ->
            when (val states = searchLiveData) {
                is StatesOfSearching.Loading -> loading()
                is StatesOfSearching.Search -> baseSearch()
                is StatesOfSearching.ErrorConnection -> errorConnection()
                is StatesOfSearching.ErrorFound -> errorFound()
                is StatesOfSearching.SearchAndHistory -> searchAndHistory(states.history)
                is StatesOfSearching.SearchCompleted -> searchCompleted(states.data)
            }
        }

        onFocus()

        onTextChange()

        binding.cancelButton.setOnClickListener {
            binding.inputEditText.setText("")
            val keyboard =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(binding.inputEditText.windowToken, 0)
            binding.inputEditText.clearFocus()
            viewModelSearching.clearSearchingHistoryList()
        }

        changeCrossButton()

        enterSearching()

        tracksAdapter = TrackAdapter {
            if (clickDebounce())
                clicker(it)
        }

        recyclerView = binding.resultRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = tracksAdapter

        searchHistoryAdapter = TrackAdapter {
            if (clickDebounce()) {
                clicker(it)
            }
        }

        searchHistoryRecyclerView = binding.historyRecyclerView
        searchHistoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        searchHistoryRecyclerView.adapter = searchHistoryAdapter

        binding.clearHistoryButton.setOnClickListener {
            resultsInvisible()
            viewModelSearching.clearHistory()
        }

        viewModelSearching.provideSearchHistory().observe(viewLifecycleOwner) { value ->
            value.ifEmpty { emptyList() }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(QUERY, binding.inputEditText.text.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            val searchQuery = savedInstanceState.getString(QUERY, "")
            binding.inputEditText.setText(searchQuery)
        }
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

    private fun search() {
        viewModelSearching.requestSearch(binding.inputEditText.text.toString())
    }

    private fun clicker(item: Track) {
        viewModelSearching.add(item)
        val intent = Intent(requireContext(), AudioPlayerActivity::class.java)
        intent.putExtra("track", item)
        this.startActivity(intent)
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

    private fun resultsInvisible() {
        binding.searchedText.visibility = View.GONE
        searchHistoryRecyclerView.visibility = View.GONE
        binding.clearHistoryButton.visibility = View.GONE
    }

    private fun loading() {
        binding.progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        binding.refreshButton.visibility = View.GONE
        binding.nothingFoundPicture.visibility = View.GONE
        binding.problemsWithLoadingPicture.visibility = View.GONE
        binding.problemsWithLoadingText.visibility = View.GONE
        binding.nothingFoundText.visibility = View.GONE
        binding.hidingHistory.visibility = View.GONE
        resultsInvisible()
        tracksAdapter.notifyDataSetChanged()
    }

    private fun baseSearch() {
        recyclerView.visibility = View.GONE
        binding.refreshButton.visibility = View.GONE
        binding.nothingFoundPicture.visibility = View.GONE
        binding.problemsWithLoadingPicture.visibility = View.GONE
        binding.problemsWithLoadingText.visibility = View.GONE
        binding.nothingFoundText.visibility = View.GONE
        binding.hidingHistory.visibility = View.GONE
        resultsInvisible()
    }

    private fun errorConnection() {
        binding.problemsWithLoadingPicture.visibility = View.VISIBLE
        binding.problemsWithLoadingText.visibility = View.VISIBLE
        binding.refreshButton.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        binding.refreshButton.setOnClickListener { search() }
        binding.progressBar.visibility = View.GONE
        binding.hidingHistory.visibility = View.GONE
        resultsInvisible()
    }

    private fun errorFound() {
        binding.nothingFoundPicture.visibility = View.VISIBLE
        binding.nothingFoundText.visibility = View.VISIBLE
        binding.searchedText.visibility = View.GONE
        recyclerView.visibility = View.GONE
        searchHistoryRecyclerView.visibility = View.GONE
        binding.clearHistoryButton.visibility = View.VISIBLE
        binding.problemsWithLoadingPicture.visibility = View.GONE
        binding.problemsWithLoadingText.visibility = View.GONE
        binding.refreshButton.visibility = View.GONE
        binding.hidingHistory.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        resultsInvisible()
    }

    private fun searchAndHistory(history: List<Track>) {
        searchHistoryAdapter.setIt(history)
        searchHistoryAdapter.notifyDataSetChanged()
        binding.searchedText.visibility = View.VISIBLE
        binding.clearHistoryButton.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        searchHistoryRecyclerView.visibility = View.VISIBLE
        binding.nothingFoundPicture.visibility = View.GONE
        binding.nothingFoundText.visibility = View.GONE
        binding.problemsWithLoadingPicture.visibility = View.GONE
        binding.problemsWithLoadingText.visibility = View.GONE
        binding.refreshButton.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.hidingHistory.visibility = View.VISIBLE
    }

    private fun searchCompleted(data: List<Track>) {
        binding.progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        binding.nothingFoundPicture.visibility = View.GONE
        binding.nothingFoundText.visibility = View.GONE
        binding.problemsWithLoadingPicture.visibility = View.GONE
        binding.problemsWithLoadingText.visibility = View.GONE
        binding.refreshButton.visibility = View.GONE
        tracksAdapter.setIt(data)
        binding.clearHistoryButton.visibility = View.GONE
        binding.hidingHistory.visibility = View.GONE
        resultsInvisible()
    }

    private fun onFocus() {
        binding.inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && binding.inputEditText.text.isEmpty()) {
                viewModelSearching.provideSearchHistory()
                    .observe(viewLifecycleOwner) { searchHistoryList ->
                        if (searchHistoryList.isNotEmpty()) {
                            searchHistoryRecyclerView.visibility = View.VISIBLE
                        } else {
                            resultsInvisible()
                            searchHistoryRecyclerView.visibility = View.GONE
                        }
                    }
            } else {
                resultsInvisible()
            }
        }
    }

    var searchingText = ""

    private fun onTextChange() {
        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.inputEditText.hasFocus() && p0?.isEmpty() == true && viewModelSearching.provideSearchHistory().value?.isNotEmpty() ?: false) {
                    viewModelSearching.clearSearchingHistoryList()
                } else {
                    resultsInvisible()
                }
                if (!binding.inputEditText.text.isNullOrEmpty()) {
                    searchingText = binding.inputEditText.text.toString()
                    searchDebounce()
                }
            }


            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun changeCrossButton() {

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.cancelButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        binding.inputEditText.addTextChangedListener(simpleTextWatcher)
    }

    private fun enterSearching() {
        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.inputEditText.text.isNotEmpty()) {
                    searchingText = binding.inputEditText.text.toString()
                    bottomNavigator.visibility = View.VISIBLE
                    search()
                    tracksAdapter.notifyDataSetChanged()
                    enterIsPressed = true
                    handler.postDelayed(
                        { enterIsPressed = false }, DEBOUNCE_DELAY_3000L
                    )
                }
                true
            }
            false
        }
    }

}