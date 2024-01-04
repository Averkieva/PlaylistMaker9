package com.example.playlistmaker.data.dbFavourite

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.dbFavourite.dao.FavouriteDao

@Database(version = 1, entities = [FavouriteEntity::class])

abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): FavouriteDao
}