package com.example.playlistmaker.ui.settings.view_model

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.app.App
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.settings.interactors.SettingsInteractor
import com.example.playlistmaker.domain.share.ShareInteractor

class ViewModelSettings(private var shareInteractor:ShareInteractor, private var settingsInteractor: SettingsInteractor):ViewModel() {

    init {
        shareInteractor = Creator.provideShareInteractor()
        settingsInteractor = Creator.provideSettingsInteractor()
    }

    private var backLiveData = MutableLiveData(false)

            fun back(){
                backLiveData.value = true
            }

    private var themeLiveData = MutableLiveData(settingsInteractor.isDark())

    fun getBack():LiveData<Boolean> = backLiveData

    fun shareApp(){
        shareInteractor.shareApp()
    }

    fun openSupport(){
        shareInteractor.openSupport()
    }

    fun userPolicy(){
        shareInteractor.userPolicy()
    }

    fun getTheme():LiveData<Boolean>{
        val res = if (themeLiveData.value!!)
            "day"
        else
            "night"
        return themeLiveData
    }

    fun changeTheme(){
        themeLiveData.value = settingsInteractor.changeTheme()
        val res = if (themeLiveData.value!!)
            "day"
        else
            "night"
        installTheme(themeLiveData.value!!)
    }

    private fun installTheme(flagTheme:Boolean){
        if(flagTheme)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }



    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val app = App()
                    return ViewModelSettings(
                        Creator.provideShareInteractor(), Creator.provideSettingsInteractor()
                    ) as T
                }
            }
    }
}