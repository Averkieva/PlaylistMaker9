package com.example.playlistmaker.domain.search.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Track(
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: String?,
    val artworkUrl100: String?,
    val trackId: Long?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?
): Parcelable {
    override fun hashCode(): Int {
        return this.trackId.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other !is Track)
            return false
        return this.trackId == other.trackId
    }
}

