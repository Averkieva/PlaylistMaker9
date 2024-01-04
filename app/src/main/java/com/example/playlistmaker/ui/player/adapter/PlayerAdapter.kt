package com.example.playlistmaker.ui.player.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.AudioPlayerResultOfPlaylistBinding
import com.example.playlistmaker.domain.search.model.Playlist

class PlayerAdapter(private var _it: List<Playlist>, private val clickListener: Click) :
    RecyclerView.Adapter<PlaylistViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view =
            LayoutInflater.from(parent.context)
        return PlaylistViewHolder(AudioPlayerResultOfPlaylistBinding.inflate(view, parent, false))
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(_it[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(_it[position])
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = _it.size

    fun interface Click {
        fun onClick(playlist: Playlist)
    }
}

class PlaylistViewHolder(private val binding: AudioPlayerResultOfPlaylistBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val corner = itemView.resources.getDimensionPixelSize(R.dimen.icon_padding)

    @SuppressLint("SetTextI18n")
    fun bind(playlist: Playlist) {
        binding.titleAlbum.text = playlist.playlistName
        binding.numberOfTracks.text =
            itemView.context.applicationContext.resources.getQuantityString(
                R.plurals.tracks,
                playlist.trackList.size,
                playlist.trackList.size
            )

        //размер обложки в зависимости от наличия картинки

        if (playlist.playlistUri.isEmpty()) {
            binding.albumCover.setImageResource(R.drawable.search_placeholder)
            val playlistCover = binding.albumCover.layoutParams
            playlistCover.width =
                itemView.resources.getDimensionPixelSize(R.dimen.width_placeholder)
            playlistCover.height =
                itemView.resources.getDimensionPixelSize(R.dimen.height_placeholder)
            binding.albumCover.layoutParams = playlistCover
        } else {
            val width = itemView.resources.getDimensionPixelSize(R.dimen.width_and_height_picture)
            val height = itemView.resources.getDimensionPixelSize(R.dimen.width_and_height_picture)

            Glide.with(itemView)
                .load(playlist.playlistUri)
                .centerCrop()
                .placeholder(R.drawable.search_placeholder)
                .transform(CenterCrop(), RoundedCorners(corner))
                .override(width, height)
                .into(binding.albumCover)
        }
    }
}
