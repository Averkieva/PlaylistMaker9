package com.example.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.view_model.ViewModelSettings

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModelSettings: ViewModelSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModelSettings = ViewModelProvider(
            this,
            ViewModelSettings.getViewModelFactory()
        )[ViewModelSettings::class.java]

        binding.returnButton.setOnClickListener {
            viewModelSettings.back()
        }
        viewModelSettings.getBack().observe(this){backLiveData -> back(backLiveData)}

        viewModelSettings.getTheme().observe(this){theme ->
            binding.themeSwitcher.isChecked =! theme
        }

        binding.themeSwitcher.setOnClickListener {
            viewModelSettings.changeTheme()
        }

        binding.share.setOnClickListener {
            viewModelSettings.shareApp()
        }

        binding.support.setOnClickListener {
            viewModelSettings.openSupport()
        }

        binding.userPolicy.setOnClickListener {
            viewModelSettings.userPolicy()
        }
    }

    private fun back(back:Boolean){
        if(back) finish()
    }
}