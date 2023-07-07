package com.vision2020.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.vision2020.AppApplication
import com.vision2020.R
import com.vision2020.ui.MainActivity
import com.vision2020.ui.user.LoginActivity
import com.vision2020.ui.views.AppToastMsg
import com.vision2020.utils.AppConstant.CODE_SESSION_EXPIRED
import com.vision2020.utils.AppConstant.CODE_SUCCESS
import com.vision2020.utils.AppConstant.ERROR
import com.vision2020.utils.AppConstant.TOKEN_INVALID
import dmax.dialog.SpotsDialog
import org.json.JSONObject

fun getAppPref(): AppPreference {
    return AppPreference(AppApplication.getInstance()!!)
}

fun Context.isAppConnected(): Boolean {
    return if (ConnectionDetector(this).isConnectingToInternet) {
        true
    } else {
        //AppToastMsg.makeText(this, getString(R.string.internet_error), Toast.LENGTH_SHORT, ERROR, false).show()
        return false
    }
}

fun Context.showToastMsg(msg: String, type: Int) {
    AppToastMsg.makeText(this, msg, Toast.LENGTH_SHORT, type, false).show()
}

fun handleJson(response: String): Pair<String, String> {
    return if (response.startsWith("{")) {
        val obj = JSONObject(response)
        val statusCode = obj.getString("http_code")
        val msg = obj.getString("message")
        Pair(statusCode, msg)
    } else {
        Pair("500", "Internal Server Error")
    }

}

fun Context.responseHandler(statusCode: Int, message: String): Boolean {
    return when (statusCode) {
        CODE_SUCCESS -> true
        else -> {
            if (statusCode == CODE_SESSION_EXPIRED || statusCode == TOKEN_INVALID) {
                //logOut()
            }
            if (message.isNotEmpty()) {
                showToastMsg(message, 2)
            }
            /* if (message != "Information not available") {
                 showToast(message)
             }*/
            false
        }
    }
}

fun Context.progressDialog(txt: String): AlertDialog {
    return SpotsDialog.Builder().setContext(this).setCancelable(false).setMessage(txt)
            .build()
}

fun Context.spanText(textView: AppCompatTextView, string: String, start: Int, end: Int, returnValue: (Boolean) -> Unit) {
    val click: ClickableSpan = object : ClickableSpan() {
        override fun onClick(view: View) {
            returnValue(true)
        }
    }
    returnValue(false)
    val string = SpannableString(string)
    string.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorBlueDark)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    string.setSpan(click, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    textView.text = string
    textView.movementMethod = LinkMovementMethod.getInstance()

}

fun NavController.navigateSafe(
        navDirections: NavDirections? = null
) {
    try {
        navDirections?.let {
            this.navigate(navDirections)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.logoutUser(mcon:Activity,msg:String) {
    /*var settings = AppApplication.getPref(AppApplication.getInstance()!!)
    settings.edit().clear().commit();
    val intent = Intent(mcon, LoginActivity::class.java)
    startActivity(intent)*/
    mcon?.showToastMsg(msg, 2)
}

 fun isNetworkAvailable(mcon:Context): Boolean {
    val connectivityManager =mcon!!.getSystemService(Context.CONNECTIVITY_SERVICE)
    return if (connectivityManager is ConnectivityManager) {
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        networkInfo?.isConnected ?: false
    } else false
}

