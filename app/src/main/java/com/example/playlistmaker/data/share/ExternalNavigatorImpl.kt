package com.example.playlistmaker.data.share

import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.app.App
import com.example.playlistmaker.domain.share.ExternalNavigator

class ExternalNavigatorImpl(private val application: App):ExternalNavigator {
    override fun getShareLink(): String {
        return application.getString(R.string.android_developer_url)
    }

    override fun shareLink(link: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, application.getString(R.string.android_developer_url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intent)
    }

    override fun openMail() {
        val intent = Intent.createChooser(Intent(Intent.ACTION_SENDTO).apply{
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, application.getString(R.string.email))
            putExtra(Intent.EXTRA_SUBJECT, application.getString(R.string.sub))
            putExtra(Intent.EXTRA_TEXT, application.getString(R.string.txt))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }, "chooser")
        application.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    override fun openLink() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = (Uri.parse(application.getString(R.string.practicum_offer)))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intent)
    }
}