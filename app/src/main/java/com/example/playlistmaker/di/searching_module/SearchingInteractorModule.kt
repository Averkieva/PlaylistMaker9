package com.example.playlistmaker.di.searching_module

import com.example.playlistmaker.domain.search.history.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.history.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.search.interactors.SearchingInteractor
import com.example.playlistmaker.domain.search.interactors.SearchingInteractorImpl
import org.koin.dsl.module

val searchInteractorModule = module {
    single <SearchingInteractor> {
        SearchingInteractorImpl(get())
    }

    single <SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }
}