package apps.jizzu.cryptocoin.presenter

import android.content.Context
import android.content.Intent
import android.net.Uri
import apps.jizzu.cryptocoin.BuildConfig
import apps.jizzu.cryptocoin.R
import apps.jizzu.cryptocoin.utils.DeviceInfo
import apps.jizzu.cryptocoin.view.about.AboutView

class AboutPresenter(private val mContext: Context) {

    private lateinit var mView: AboutView

    fun attachView(view: AboutView) {
        this.mView = view
    }

    fun sendFeedbackButtonClicked() {
        val email = Intent(Intent.ACTION_SENDTO)
        email.data = Uri.Builder().scheme("mailto").build()
        email.putExtra(Intent.EXTRA_EMAIL, arrayOf("ilya.ponomarenko.dev@gmail.com"))
        email.putExtra(Intent.EXTRA_SUBJECT, mContext.resources.getString(R.string.feedbackTitle))
        email.putExtra(Intent.EXTRA_TEXT, mContext.resources.getString(R.string.feedbackDeviceInfo) + DeviceInfo.deviceInfo
                + "\n" + mContext.resources.getString(R.string.feedbackAppVersion) + BuildConfig.VERSION_NAME
                + "\n" + mContext.resources.getString(R.string.feedback) + "\n")
        mView.sendFeedback(email)
    }

    fun rateThisAppButtonClicked() {
        val appPackageName = mContext.packageName
        mView.rateThisApp(appPackageName)
    }

    fun openSiteLink(link: String) {
        val uri = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        mView.openSiteLink(intent)
    }
}