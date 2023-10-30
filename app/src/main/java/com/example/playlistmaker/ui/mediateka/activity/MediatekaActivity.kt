package com.example.playlistmaker.ui.mediateka.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediatekaBinding
import com.example.playlistmaker.ui.mediateka.fragments.MediatekaAdapter
import com.example.playlistmaker.ui.mediateka.fragments.SelectPage
import com.google.android.material.tabs.TabLayoutMediator

class MediatekaActivity : AppCompatActivity(), SelectPage {

    private lateinit var binding: ActivityMediatekaBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediatekaBinding.inflate(layoutInflater)
        binding.backMediaButton.setOnClickListener { finish() }
        setContentView(binding.root)

        binding.viewPager.adapter = MediatekaAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favourite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

    override fun navigateTo(page: Int) {
        binding.viewPager.currentItem = page
    }
}