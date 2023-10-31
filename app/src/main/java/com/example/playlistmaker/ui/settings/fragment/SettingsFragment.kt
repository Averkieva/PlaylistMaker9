package com.example.playlistmaker.ui.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.ui.settings.view_model.ViewModelSettings
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val viewModelSettings by viewModel<ViewModelSettings>()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelSettings.getBack()

        viewModelSettings.getTheme().observe(viewLifecycleOwner) { theme ->
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}