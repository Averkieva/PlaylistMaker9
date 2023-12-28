package com.example.playlistmaker.data.db.dao

import androidx.room.*
import com.example.playlistmaker.data.db.FavouriteEntity

@Dao
interface FavouriteDao {
    @Insert(entity = FavouriteEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack(track: FavouriteEntity)

    @Delete(entity = FavouriteEntity::class)
    fun deleteTrack(track: FavouriteEntity)

    @Query("SELECT * FROM favourite_table ORDER BY durationTime DESC")
    fun getTrack(): List<FavouriteEntity>

    @Query("SELECT * FROM favourite_table WHERE trackId=:trackId")
    fun getTrackId(trackId: Long): FavouriteEntity?

}