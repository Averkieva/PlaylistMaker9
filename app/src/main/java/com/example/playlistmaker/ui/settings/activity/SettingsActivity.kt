package com.example.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.view_model.ViewModelSettings
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModelSettings by viewModel<ViewModelSettings>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.returnButton.setOnClickListener {
            viewModelSettings.back()
        }
        viewModelSettings.getBack().observe(this) { backLiveData -> back(backLiveData) }

        viewModelSettings.getTheme().observe(this) { theme ->
            binding.themeSwitcher.isChecked = !theme
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

    private fun back(back: Boolean) {
        if (back) finish()
    }
}