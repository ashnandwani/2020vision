package com.vision2020.utils
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
class ConnectionDetector(// connected to the internet
    private var _context: Context
) : BroadcastReceiver() {
    /**
     * Checking for all possible internet providers
     */
    val isConnectingToInternet: Boolean
        get() {
            var result = false
            val cm =
                _context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        result = true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        result = true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                        return true
                    }
                }
            } else {
                val activeNetwork = cm.activeNetworkInfo
                if (activeNetwork != null) { // connected to the internet
                    if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    } else if (activeNetwork.type == ConnectivityManager.TYPE_VPN) {
                        return true
                    }
                }
            }
            return result
        }

    override fun onReceive(arg0: Context, arg1: Intent) {
        _context = arg0
        val check = isConnectingToInternet
        Log.d("check", "is $check")
    }

}