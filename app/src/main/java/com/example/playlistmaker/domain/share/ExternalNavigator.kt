package com.example.playlistmaker.domain.share

interface ExternalNavigator {
    fun shareLink(link: String)

    fun getShareLink(): String

    fun openMail()

    fun openLink()
}