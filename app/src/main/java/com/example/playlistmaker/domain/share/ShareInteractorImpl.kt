package com.example.playlistmaker.domain.share

class ShareInteractorImpl(private var externalNavigator: ExternalNavigator,):ShareInteractor
{

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