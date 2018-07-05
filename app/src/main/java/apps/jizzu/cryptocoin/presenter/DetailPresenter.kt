package apps.jizzu.cryptocoin.presenter

import android.content.Intent
import android.net.Uri
import apps.jizzu.cryptocoin.view.detail.DetailView

class DetailPresenter {

    private lateinit var mView: DetailView

    fun attachView(view: DetailView) {
        this.mView = view
    }

    fun openSiteLink(link: String) {
        val uri = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        mView.openSiteLink(intent)
    }
}