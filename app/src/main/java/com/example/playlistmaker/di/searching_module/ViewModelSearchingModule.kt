package com.example.playlistmaker.di.searching_module

import com.example.playlistmaker.ui.search.view_model.ViewModelSearching
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelSearchingModule = module{
    viewModel { ViewModelSearching(get(), get()) }
}