package com.vision2020.utils
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.util.TypedValue
import com.vision2020.AppApplication

class AppPreference(mActivity: AppApplication) {
    private val preferences: SharedPreferences = mActivity.getSharedPreferences(mActivity.packageName, Context.MODE_PRIVATE)

    fun setString(key: String, value: String) {
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String, defaultValue: String): String? {
        return preferences.getString(key, defaultValue)
    }

    fun getString(key: String): String? {
        return preferences.getString(key, "")
    }

    fun setBoolean(key: String, value: Boolean?) {
        val editor = preferences.edit()
        editor.putBoolean(key, value!!)
        editor.apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean?): Boolean {
        return preferences.getBoolean(key, defaultValue!!)
    }

    fun setInt(key: String, value: Int) {
        val editor = preferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    /*fun getInt(key: String, defaultValue: Int): Int {
        return preferences.getInt(key, defaultValue)
    }*/

    fun getInt(key: String): Int {
        return preferences.getInt(key, -1)
    }

    companion object {
        fun dpToPx(context: Context, dp: Float): Int {
            val scale = context.resources.displayMetrics.density
            return Math.round(dp * scale)
        }

        fun dpToPx(dp: Float, resources: Resources): Int {
            val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
            return px.toInt()
        }
    }

}
