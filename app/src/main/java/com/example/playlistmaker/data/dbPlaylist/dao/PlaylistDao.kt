package com.example.playlistmaker.data.dbPlaylist.dao

import androidx.room.*
import com.example.playlistmaker.data.dbPlaylist.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(playlist: PlaylistEntity)

    @Update(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun updatePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    fun getPlaylist(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlist_table WHERE playlistId=:searchId")
    fun searchPlaylist(searchId: Int): PlaylistEntity

    @Delete(entity = PlaylistEntity::class)
    fun deletePlaylist(playlist: PlaylistEntity)

}