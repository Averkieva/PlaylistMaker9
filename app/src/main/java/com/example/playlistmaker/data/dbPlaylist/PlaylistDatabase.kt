package com.example.playlistmaker.data.dbPlaylist

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.dbPlaylist.dao.PlaylistDao

@Database(version = 1, entities = [PlaylistEntity::class])
abstract class PlaylistDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
}