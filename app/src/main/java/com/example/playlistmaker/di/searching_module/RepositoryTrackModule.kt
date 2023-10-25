package com.example.playlistmaker.di.searching_module

import com.example.playlistmaker.data.search.RepositoryTrackImpl
import com.example.playlistmaker.data.search.history.SearchingHistoryImpl
import com.example.playlistmaker.domain.search.RepositoryTrack
import com.example.playlistmaker.domain.search.history.SearchingHistory
import org.koin.dsl.module

val repositoryTrackModule = module {
    single <RepositoryTrack> {
        RepositoryTrackImpl(get())
    }
    single <SearchingHistory> {
        SearchingHistoryImpl(get(), get())
    }
}