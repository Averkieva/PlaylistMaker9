package com.example.playlistmaker.ui.search.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchingBinding
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.search.adapter.TrackAdapter
import com.example.playlistmaker.ui.search.view_model.ViewModelSearching
import com.example.playlistmaker.ui.search.view_model.states.StatesOfSearching
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchingFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tracksAdapter: TrackAdapter
    private lateinit var searchHistoryAdapter: TrackAdapter
    private lateinit var searchHistoryRecyclerView: RecyclerView
    private var _binding: FragmentSearchingBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomNavigator: BottomNavigationView
    private var isClickAllowed = true
    private val viewModelSearching by viewModel<ViewModelSearching>()
    private var latestSearchingText: String? = null
    private var searchingJob: Job? = null


    companion object {
        const val QUERY = "QUERY"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //viewModelSearching.provideSearchHistory()

        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)

        viewModelSearching.getSearchingLiveData().observe(viewLifecycleOwner) { searchLiveData ->

            //Log.d("+++"," fragment <- viewModel :: ${searchLiveData.toString()}")

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
        binding.cancelButton.visibility = View.INVISIBLE

        clickDebounce()

        changeCrossButton()

        enterSearching()

        tracksAdapter = TrackAdapter {
            if (isClickAllowed)
                clicker(it)
        }

        recyclerView = binding.resultRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = tracksAdapter

        searchHistoryAdapter = TrackAdapter {
            if (isClickAllowed) {
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

       /* viewModelSearching.provideSearchHistory().observe(viewLifecycleOwner) { value ->

            value.ifEmpty { emptyList() }
        }*/
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        val bundle = Bundle()
        bundle.putParcelable("track", item)

        viewModelSearching.add(item)

        val navController = findNavController()
        navController.navigate(R.id.searchingFragment_to_audioPlayerFragment, bundle)
    }

    private fun clickDebounce() {
        GlobalScope.launch { clickDebouncer() }
    }

    private suspend fun clickDebouncer() {
        isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            delay(CLICK_DEBOUNCE_DELAY)
            isClickAllowed = true
        }
    }

    private fun searchDebounce() {
        val changingText = binding.inputEditText.text.toString()
        if (latestSearchingText == changingText) return
        latestSearchingText = changingText
        searchingJob?.cancel()
        searchingJob = lifecycleScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            search()
        }
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
        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
/*            if (hasFocus && binding.inputEditText.text.isEmpty()) {
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
            }*/
        }
    }

    private fun onTextChange() {
        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (
                    binding.inputEditText.hasFocus() && s?.isEmpty() == true
                    //&& viewModelSearching.provideSearchHistory().value?.isNotEmpty() ?: false
                ) {
                    viewModelSearching.clearSearchingHistoryList()
                } else {
                    resultsInvisible()
                }

                if (!binding.inputEditText.text.isNullOrEmpty()) {
                    searchDebounce()
                }
            }


            override fun afterTextChanged(s: Editable?) {
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
                    bottomNavigator.visibility = View.VISIBLE
                    searchDebounce()
                    tracksAdapter.notifyDataSetChanged()
                }
                true
            }
            false
        }
    }

}