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
        val editor = mPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(key: String): Int {
        return mPreferences.getInt(key, 0)
    }

    companion object {

        const val ITEM_POSITION = "item_position"

        private var mInstance: PreferenceHelper? = null

        val instance: PreferenceHelper
            get() {
                if (mInstance == null) {
                    mInstance = PreferenceHelper()
                }
                return mInstance as PreferenceHelper
            }
    }
}