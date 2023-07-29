package com.example.playlistmaker

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TrackAdapter(
    val tracks: ArrayList<Track>
) : RecyclerView.Adapter<TracksViewHolder>() {
    private val searchingActivity = SearchingActivity()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.result_of_search, parent, false)
        return TracksViewHolder(view)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, AudioPlayerActivity::class.java)
            intent.putExtra("Track Name", tracks[position].trackName)
            intent.putExtra("Artist Name", tracks[position].artistName)
            val trackTime = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(tracks[position].trackTimeMillis)
            intent.putExtra("Track Time", trackTime)
            intent.putExtra("Album", tracks[position].collectionName)
            intent.putExtra("Year", tracks[position].releaseDate)
            intent.putExtra("Genre", tracks[position].primaryGenreName)
            intent.putExtra("Country", tracks[position].country)
            intent.putExtra("Cover", tracks[position].artworkUrl100)
            holder.itemView.context.startActivity(intent)
            searchingActivity.searchHistory.editSearchHistory(tracks[position])
        }
    }

    override fun getItemCount(): Int = tracks.size

}

class TracksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.track_article)
    private val artistName: TextView = itemView.findViewById(R.id.track_artist)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)
    private val artworkUrl100: ImageView = itemView.findViewById(R.id.track_pic)
    val corner = itemView.resources.getDimensionPixelSize(R.dimen.cover_radius)
    var numberTrack: Long = 0

    fun bind(track: Track) {
        numberTrack = track.trackId
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        Glide.with(artworkUrl100.context)
            .load(track.artworkUrl100)
            .centerCrop()
            .placeholder(R.drawable.search_placeholder)
            .transform(RoundedCorners(corner))
            .into(artworkUrl100)
    }

}
