package com.example.playlistmaker

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val trackID: Long
){
    override fun hashCode(): Int {
        return this.trackID.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other !is Track)
            return false
        return this.trackID == other.trackID
    }
}

