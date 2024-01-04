package com.example.playlistmaker.ui.mediateka.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ResultOfPlaylistBinding
import com.example.playlistmaker.domain.search.model.Playlist

class PlaylistAdapter(private var _it: List<Playlist>, private val clickListener: Click) :
    RecyclerView.Adapter<PlaylistViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view =
            LayoutInflater.from(parent.context)
        return PlaylistViewHolder(ResultOfPlaylistBinding.inflate(view, parent, false))
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

class PlaylistViewHolder(private val binding: ResultOfPlaylistBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val corner = itemView.resources.getDimensionPixelSize(R.dimen.icon_padding)

    @SuppressLint("SetTextI18n")
    fun bind(playlist: Playlist) {
        binding.namePlaylist.text = playlist.playlistName
        binding.countPlaylist.text =
            itemView.context.applicationContext.resources.getQuantityString(
                R.plurals.tracks,
                playlist.trackList.size,
                playlist.trackList.size
            )

        //размер обложки в зависимости от наличия картинки

        if (playlist.playlistUri.isEmpty()) {
            binding.playlistAlbum.setImageResource(R.drawable.search_placeholder)
            val playlistCover = binding.playlistAlbum.layoutParams
            playlistCover.width =
                itemView.resources.getDimensionPixelSize(R.dimen.width_placeholder)
            playlistCover.height =
                itemView.resources.getDimensionPixelSize(R.dimen.height_placeholder)
            binding.playlistAlbum.layoutParams = playlistCover
        } else {
            val width = itemView.resources.getDimensionPixelSize(R.dimen.width_and_height_picture)
            val height = itemView.resources.getDimensionPixelSize(R.dimen.width_and_height_picture)

            Glide.with(itemView)
                .load(playlist.playlistUri)
                .centerCrop()
                .placeholder(R.drawable.search_placeholder)
                .transform(CenterCrop(), RoundedCorners(corner))
                .override(width, height)
                .into(binding.playlistAlbum)
        }
    }
}
