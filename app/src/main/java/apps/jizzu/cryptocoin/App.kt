package apps.jizzu.cryptocoin

import android.app.Application
import apps.jizzu.cryptocoin.utils.PreferenceHelper

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        PreferenceHelper.getInstance().init(applicationContext)
    }
}