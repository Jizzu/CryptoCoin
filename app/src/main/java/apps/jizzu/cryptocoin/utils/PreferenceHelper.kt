package apps.jizzu.cryptocoin.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper private constructor() {
    private lateinit var mContext: Context
    private lateinit var mPreferences: SharedPreferences

    fun init(context: Context) {
        mContext = context
        mPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
    }

    fun putInt(key: String, value: Int) {
        mPreferences.edit().apply {
            putInt(key, value)
            apply()
        }
    }

    fun getInt(key: String) = mPreferences.getInt(key, 0)

    companion object {
        const val ITEM_POSITION = "item_position"
        private var mInstance: PreferenceHelper? = null

        fun getInstance(): PreferenceHelper {
            if (mInstance == null) {
                mInstance = PreferenceHelper()
            }
            return mInstance as PreferenceHelper
        }
    }
}