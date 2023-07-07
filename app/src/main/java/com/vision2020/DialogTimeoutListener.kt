package com.vision2020

import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import android.os.CountDownTimer
import androidx.appcompat.app.AlertDialog
import java.util.*
import java.util.concurrent.TimeUnit

class DialogTimeoutListener(val mcon: Context, val callback: Callback) : OnShowListener, DialogInterface.OnDismissListener {
    private var mCountDownTimer: CountDownTimer? = null
    override fun onShow(dialog: DialogInterface) {
        val defaultButton =
            (dialog as AlertDialog).getButton(AlertDialog.BUTTON_NEGATIVE)
        val positiveButtonText = defaultButton.text
        mCountDownTimer =
            object : CountDownTimer(AUTO_DISMISS_MILLIS.toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    /* if (millisUntilFinished > 60000) {
                    defaultButton.setText(String.format(
                            Locale.getDefault(), "%s (%d:%02d)",
                            positiveButtonText,
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished % 60000)
                    ));
                } else {*/
                    defaultButton.text = String.format(
                        Locale.getDefault(), "%s (%d)",
                        positiveButtonText,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1 //add one so it never displays zero
                    )
                    /*  }*/
                }

                override fun onFinish() {
                    if (dialog.isShowing) {
                        dialog.dismiss()
                        callback.res("")
                    }
                }
            }
        (mCountDownTimer as CountDownTimer).start()
    }

    override fun onDismiss(dialog: DialogInterface) {
        mCountDownTimer!!.cancel()
    }

    companion object {
        private const val AUTO_DISMISS_MILLIS = 15000
    }
    interface Callback {
        fun res( type: String) {}
    }
}