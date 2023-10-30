package com.example.playlistmaker.ui.mediateka.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediatekaBinding
import com.example.playlistmaker.ui.mediateka.fragments.MediatekaFragmentAdapter
import com.example.playlistmaker.ui.mediateka.fragments.SelectPage
import com.google.android.material.tabs.TabLayoutMediator

class MediatekaFragment : Fragment(), SelectPage {

    private lateinit var binding: FragmentMediatekaBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentMediatekaBinding.inflate(layoutInflater)

        binding.viewPager.adapter = MediatekaFragmentAdapter(this)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favourite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()

        return binding.root
    }

    override fun navigateTo(page: Int) {
        binding.viewPager.currentItem = page
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}