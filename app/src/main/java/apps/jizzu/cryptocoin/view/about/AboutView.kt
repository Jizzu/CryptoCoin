package apps.jizzu.cryptocoin.view.about

import android.content.Intent

interface AboutView {

    fun sendFeedback(email: Intent)

    fun rateThisApp(appPackageName: String)

    fun openSiteLink(intent: Intent)
}