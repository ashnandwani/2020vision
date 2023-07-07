package com.vision2020.ui.views.dialog

import android.content.Context
import android.view.Gravity

class AddGroupDialog(context: Context,
                     themeResId: Int,
                     private val layoutId: Int):BaseDialog(context,themeResId) {
    init {
        val windowLayout = this.window!!.attributes
        windowLayout.gravity = Gravity.CENTER
        window!!.attributes = windowLayout
    }
    override fun getContentView(): Int {
        return  layoutId
    }

    override fun onCreateStuff() {


    }

    fun showDialog() {
        if (this.isShowing) {
            return
        } else {
            this.show()
        }

    }

    fun dismissDialog() {
        if (this.isShowing) {
            this.dismiss()
        } else {
            return
        }

    }

}