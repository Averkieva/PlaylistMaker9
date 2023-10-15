package com.example.playlistmaker.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ResultOfSearchBinding
import com.example.playlistmaker.domain.search.model.Track

class TrackAdapter(private val clickListener:Click) : RecyclerView.Adapter<TracksViewHolder>() {
    private var _it: List<Track> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view =
            LayoutInflater.from(parent.context)
        return TracksViewHolder(ResultOfSearchBinding.inflate(view, parent, false))
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(_it[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(_it[position])
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = _it.size

    fun interface Click {
        fun onClick(track: Track)
    }

    fun setIt(it: List<Track>) {
        _it = it
        notifyDataSetChanged()
    }


}

class TracksViewHolder(private val binding: ResultOfSearchBinding) : RecyclerView.ViewHolder(binding.root) {

    val corner = itemView.resources.getDimensionPixelSize(R.dimen.cover_radius)

    fun bind(track: Track) {
        binding.trackArticle.text = track.trackName
        binding.trackArtist.text = track.artistName
        binding.trackTime.text = track.trackTimeMillis
        Glide.with(binding.trackPic.context)
            .load(track.artworkUrl100)
            .centerCrop()
            .placeholder(R.drawable.search_placeholder)
            .transform(RoundedCorners(corner))
            .into(binding.trackPic)
    }

}
