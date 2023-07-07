package com.vision2020.ui.views
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.vision2020.R
import com.vision2020.utils.AppConstant.ERROR
import com.vision2020.utils.AppConstant.INFO
import com.vision2020.utils.AppConstant.SUCCESS
import com.vision2020.utils.AppConstant.WARNING

class AppToastMsg(context: Context?) : Toast(context) {
    companion object {

        fun makeText(context: Context?, message: String?, duration: Int, type: Int, androidIcon: Boolean): Toast {
            val toast = Toast(context)
            toast.duration = duration
            val layout: View = LayoutInflater.from(context).inflate(R.layout.layout_toast, null, false)
            val l1 = layout.findViewById<View>(R.id.toast_text) as TextView
            val linearLayout = layout.findViewById<View>(R.id.toast_type) as LinearLayout
            val img = layout.findViewById<View>(R.id.toast_icon) as ImageView
            val img1 = layout.findViewById<View>(R.id.imageView4) as ImageView
            l1.text = message
            if (androidIcon) img1.visibility = View.VISIBLE else if (!androidIcon) img1.visibility = View.GONE
            when (type) {
                SUCCESS -> linearLayout.setBackgroundResource(R.drawable.success_shape)
                ERROR -> linearLayout.setBackgroundResource(R.drawable.error_shape)
                WARNING -> linearLayout.setBackgroundResource(R.drawable.warning_shape)
                INFO -> linearLayout.setBackgroundResource(R.drawable.info_shape)
            }
            toast.view = layout
            return toast
        }
    }
}