package com.vision2020.utils
import `in`.mayanknagwanshi.imagepicker.ImageSelectActivity
import android.app.Activity
import android.content.Context
import android.content.Intent
import com.vision2020.ui.MainActivity
import com.vision2020.ui.user.LoginActivity
import com.vision2020.ui.user.SignUpActivity
import com.vision2020.utils.AppConstant.CAMERA_CODE
fun Context.navigateToHome(activity: Activity, activityName: Class<MainActivity>) {
    val intent = Intent(activity,activityName)
    startActivity(intent)
    activity.finish()
}

fun Context.navigateToLogin(activity: Activity, activityName: Class<LoginActivity>){
    val intent = Intent(activity,activityName)
    startActivity(intent)

}

fun Context.navigateToSignUp(activity: Activity,activityName: Class<SignUpActivity>){
    val intent = Intent(activity,activityName)
    startActivity(intent)
}

fun Context.navigateForCamera(activity: Activity){
    val intent = Intent(this, ImageSelectActivity::class.java)
    intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true)
    intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true)
    intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, false)
   activity.startActivityForResult(intent, CAMERA_CODE);
}
