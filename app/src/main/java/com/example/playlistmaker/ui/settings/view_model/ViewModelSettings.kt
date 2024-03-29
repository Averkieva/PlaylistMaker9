package com.example.playlistmaker.ui.settings.view_model

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.interactors.SettingsInteractor
import com.example.playlistmaker.domain.share.ShareInteractor

class ViewModelSettings(
    private var shareInteractor: ShareInteractor,
    private var settingsInteractor: SettingsInteractor
) : ViewModel() {

    private var backLiveData = MutableLiveData(false)

    fun back() {
        backLiveData.value = true
    }

    var themeLiveData = MutableLiveData(settingsInteractor.isDark())

    fun getBack(): LiveData<Boolean> = backLiveData

    fun shareApp() {
        shareInteractor.shareApp()
    }

    fun openSupport() {
        shareInteractor.openSupport()
    }

    fun userPolicy() {
        shareInteractor.userPolicy()
    }

    fun getTheme(): LiveData<Boolean> {
        return themeLiveData
    }

    fun changeTheme() {
        themeLiveData.value = settingsInteractor.changeTheme()
        installTheme(themeLiveData.value!!)
    }

    private fun installTheme(flagTheme: Boolean) {
        if (flagTheme)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}