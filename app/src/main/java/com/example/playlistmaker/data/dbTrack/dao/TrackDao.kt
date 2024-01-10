package com.example.playlistmaker.data.dbTrack.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.playlistmaker.data.dbTrack.TrackEntity
import com.example.playlistmaker.domain.search.model.Track

@Dao
interface TrackDao {
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    fun insertTrack(track: Track)
}