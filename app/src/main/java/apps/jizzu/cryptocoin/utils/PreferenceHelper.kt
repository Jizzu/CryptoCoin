package apps.jizzu.cryptocoin.utils

import android.content.SharedPreferences
import javax.inject.Inject

class PreferenceHelper @Inject constructor(private val mPreferences: SharedPreferences) {

    fun putInt(key: String, value: Int) {
        mPreferences.edit().apply {
            putInt(key, value)
            apply()
        }
    }

    fun getInt(key: String) = mPreferences.getInt(key, 0)

    companion object {
        const val SORT_KEY = "sort_key"
    }
}