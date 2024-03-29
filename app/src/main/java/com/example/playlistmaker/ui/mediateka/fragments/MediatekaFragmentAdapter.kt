package com.example.playlistmaker.ui.mediateka.fragments

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.ui.playlist.fragment.PlaylistFragment

class MediatekaFragmentAdapter(parent: Fragment) :
    FragmentStateAdapter(parent) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavouriteTracksFragment.newInstance()
            else -> PlaylistFragment.newInstance()
        }
    }
}