package com.example.playlistmaker.domain.share

import com.example.playlistmaker.creator.Creator

class ShareInteractorImpl(private var externalNavigator: ExternalNavigator,):ShareInteractor
{

    init {
        externalNavigator = Creator.provideExternalNavigator()
    }

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openSupport() {
        externalNavigator.openMail()
    }

    override fun userPolicy() {
        externalNavigator.openLink()
    }

    private fun getShareAppLink():String {
        return externalNavigator.getShareLink()
    }
}