package com.example.playlistmaker.data.dbTrack

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.dbTrack.dao.TrackDao

@Database(version = 1, entities = [TrackEntity::class])
abstract class TrackDatabase : RoomDatabase() {
    abstract fun trackListingDao(): TrackDao
}