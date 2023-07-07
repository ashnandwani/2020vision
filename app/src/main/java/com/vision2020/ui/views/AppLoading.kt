package com.vision2020.ui.views
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import com.vision2020.R

class AppLoading {
    var mProgress: Dialog? = null
    fun dismissLoader() {
        try {
            if (mProgress != null) {
                mProgress!!.cancel()
                mProgress!!.dismiss()
                mProgress = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showLoader(con: Context) {
        if (mProgress != null) {
            mProgress!!.cancel()
        }
        mProgress = Dialog(con)
        mProgress!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mProgress!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val wmlp = mProgress!!.window!!.attributes
        wmlp.gravity = Gravity.CENTER
        mProgress!!.window!!.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mProgress!!.setCancelable(true)
        mProgress!!.setContentView(R.layout.loading_dialog)
        mProgress!!.show()
    }

    companion object {
        var mInstance: AppLoading? = null
        fun initLoader() {
            if (mInstance == null) mInstance =
                AppLoading()
        }

        val loader: AppLoading?
            get() = if (mInstance != null) mInstance else {
                mInstance = AppLoading()
                mInstance
            }
    }
}