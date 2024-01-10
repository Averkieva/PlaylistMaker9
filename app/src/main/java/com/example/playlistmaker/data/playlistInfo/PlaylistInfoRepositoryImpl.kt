package com.example.playlistmaker.data.playlistInfo

import com.example.playlistmaker.data.converters.TrackDbConvertor
import com.example.playlistmaker.data.dbTrack.TrackDatabase
import com.example.playlistmaker.domain.playlistInfo.repository.PlaylistInfoRepository
import com.example.playlistmaker.domain.search.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistInfoRepositoryImpl(private val trackDatabase: TrackDatabase) :
    PlaylistInfoRepository {

    override fun getTrack(playlist: Playlist): Flow<List<Track>> = flow {
        val trackList = playlist.trackList
            .filterNotNull()
            .mapNotNull { id ->
                trackDatabase.trackListingDao().getId(searchId = id)
                    ?.let { TrackDbConvertor().map(it) }
            }
            .toList()
        emit(trackList)
    }

    override fun duration(playlist: Playlist): Flow<String> = flow {
        val duration = playlist.trackList
            .filterNotNull()
            .mapNotNull { id ->
                trackDatabase.trackListingDao().getId(searchId = id)
                    ?.let { TrackDbConvertor().map(it) }
                    ?.trackTimeMillis?.split(":")
                    ?.let { (minutes, seconds) ->
                        minutes.toIntOrNull() to seconds.toIntOrNull()
                    }
            }
            .fold(VALUE_0) { acc, (minutes, seconds) ->
                acc + (minutes ?: VALUE_0) * VALUE_60 + (seconds ?: VALUE_0)
            }
        val hours = duration / (VALUE_60 * VALUE_60)
        val minutes = (duration / VALUE_60) % VALUE_60
        val seconds = duration % VALUE_60

        val finishTime = if (hours == VALUE_0) {
            String.format("%02d:%02d", minutes, seconds)
        } else {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }
        emit(finishTime)
    }

    companion object {
        const val VALUE_60 = 60
        const val VALUE_0 = 0
    }
}