package com.vision2020
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics


class AppApplication : Application()  {
    init {
        instance = this
      //  instance!!.unregisterActivityLifecycleCallbacks(this)
        //instance!!.registerActivityLifecycleCallbacks(this)
    }
    fun checkIfHasNetwork(): Boolean {
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        var result = false
        val cm =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities =
                cm.getNetworkCapabilities(cm.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        result = true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        result = true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetwork = cm.activeNetworkInfo
            if (activeNetwork != null) { // connected to the internet
                when (activeNetwork.type) {
                    ConnectivityManager.TYPE_WIFI -> {
                        result = true
                    }
                    ConnectivityManager.TYPE_MOBILE -> {
                        result = true
                    }
                    ConnectivityManager.TYPE_VPN -> {
                        return true
                    }
                }
            }
        }
        return result
    }



    companion object {
        private var instance: AppApplication? = null
        var logoutListener: LogoutListener? = null
        var timer: CountDownTimer? = null
        fun getInstance(): AppApplication? {
            return instance
        }
        fun hasNetwork(): Boolean {
            return instance!!.checkIfHasNetwork()
        }
        fun getPref(inst:AppApplication): SharedPreferences {
            var sharedPref: SharedPreferences = inst!!.getSharedPreferences("vision2020", 0)
            return sharedPref
        }
        fun userSessionStart() {
            if (timer != null) {
                timer!!.cancel()
            }

            timer= object : CountDownTimer(1000 * 60 * 1, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.d("session App", "session Session working"+ millisUntilFinished / 1000)
                }

                override fun onFinish() {
                    Log.d("session App", "Session Destroyed")
                }
            }.start()
        }

        fun resetSession() {
            userSessionStart()
        }

        fun stopSession() {
            timer!!.cancel()
        }

        fun registerSessionListener(listener: LogoutListener) {
            logoutListener = listener
        }
    }

   /* override fun onActivityPaused(p0: Activity?) {
        Log.d("session App", "session onActivityPaused")
    }

    override fun onActivityResumed(p0: Activity?) {Log.d("session App", "session onActivityResumed")
    }

    override fun onActivityStarted(p0: Activity?) {Log.d("session App", "session onActivityStarted")
    }

    override fun onActivityDestroyed(p0: Activity?) {
        Log.d("session App", "session onActivityDestroyed")
    }

    override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {Log.d("session App", "session onActivitySaveInstanceState")
    }

    override fun onActivityStopped(p0: Activity?) {Log.d("session App", "session onActivityStopped")
    }

    override fun onActivityCreated(p0: Activity?, p1: Bundle?) {Log.d("session App", "session onActivityCreated")
    }*/
}

interface LogoutListener {
    fun onSessionLogout()
}
