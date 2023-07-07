package com.vision2020.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.vision2020.AppApplication
import com.vision2020.R
import com.vision2020.utils.*
import kotlinx.android.synthetic.main.fragment_effects_on_face.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class TestActivity : AppCompatActivity() ,ImageResponse{
    lateinit var rootImgFacePic: WebView

    override fun onRestart() {
        super.onRestart()
        try {
            Log.v("Nisha", "onRestart:::" + photoFile!!.absolutePath)
        }catch (ex:Exception){
            Log.v("Nisha", "onResume ex:::" )
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            Log.v("Nisha", "onResume:::" + photoFile!!.absolutePath)
        }catch (ex:Exception){
            Log.v("Nisha", "onResume ex:::" )
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_new)
        rootImgFacePic = findViewById(R.id.rootImgFacePic)

        if(isNetworkAvailable(this) ==true) {
            rootImgFacePic.loadUrl(
                "http://13.212.254.224:3000/mask_on_profile?" +
                        "authToken=c0d4518cd2d33dcacc3753c661b570e4c30a8244f0333b5a2f913c189090baa340ecaa853e46fabc"
            )
            initWebView(this)
        }else{
            responseHandler(AppConstant.ERROR, getString(R.string.internet_error))
        }
    }

    private var m_downX = 0f

    @SuppressLint("ClickableViewAccessibility")
    private fun initWebView(mcon: Activity) {
        // mUploadMessage.onReceiveValue( null)
        rootImgFacePic.setWebChromeClient(MyWebChromeClient(mcon))

        rootImgFacePic.setWebViewClient(object : WebViewClient() {
            override fun onPageStarted(
                view: WebView,
                url: String,
                favicon: Bitmap?
            ) {
                super.onPageStarted(view, url, favicon)
                try {
                    invalidateOptionsMenu()
                } catch (ex: Exception) {
                }
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                rootImgFacePic.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                try {
                    invalidateOptionsMenu()
                } catch (ex: Exception) {
                }
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
            }
        })
        rootImgFacePic.clearCache(true)
        rootImgFacePic.clearHistory()
        rootImgFacePic.getSettings().setJavaScriptEnabled(true)
        rootImgFacePic.setHorizontalScrollBarEnabled(false)
        rootImgFacePic.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.pointerCount > 1) {
                //Multi touch detected
                return@OnTouchListener true
            }
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    m_downX = event.x
                }
                MotionEvent.ACTION_MOVE, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {

                    // set x so that it doesn't move
                    event.setLocation(m_downX, event.y)
                }
            }
            false
        })
    }
    private var mUploadMessage: ValueCallback<Array<Uri?>?>? = null
    //private var mCapturedImageURI: Uri? = null
    var photoFile: File? = null
    private inner class MyWebChromeClient(var context: Activity) : WebChromeClient() {

        override fun onShowFileChooser(
            webView: WebView?, filePathCallback: ValueCallback<Array<Uri?>?>,
            fileChooserParams: FileChooserParams?
        ): Boolean {
            mUploadMessage = filePathCallback
            requestStoragePermission()
            return true
        }

        private fun requestStoragePermission() {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openFileExplorer()
                return
            } else {
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), 123
                )
                mUploadMessage!!.onReceiveValue(null)
            }
        }


        private fun displayMessage(
            context: Context,
            message: String
        ) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }


        private fun openFileExplorer() {
            if (!hasPermissions(
                    this@TestActivity,
                    *PERMISSIONS
                )
            ) {
                ActivityCompat.requestPermissions(this@TestActivity, PERMISSIONS, PERMISSION_ALL)
            } else ImageCapture(this@TestActivity, this@TestActivity, false)
        }
    }
    var mCurrentPhotoPath: String? = null
    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File =
            this@TestActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }
    fun hasPermissions(
        context: Context?,
        vararg permissions: String?
    ): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission!!
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    var selectedType = 1001
    var permissionsRequired = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val PERMISSION_ALL = 2
    var PERMISSIONS = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA
    )
    private val PERMISSION_CALLBACK_CONSTANT = 100
    private val REQUEST_PERMISSION_SETTING = 101
    protected val CAMERA_REQUEST = 6001
    protected val GALLERY_PICTURE = 6002
    override fun cameraImage(mFile: File?) {
        selectedType = 6001
        mTempFile = mFile
        askPermissions()
    }

    override fun galleryImage(mFile: File?) {
        selectedType = 6002
        mTempFile = mFile
        askPermissions()
    }

    fun askPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this@TestActivity,
                permissionsRequired.get(0)
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this@TestActivity,
                permissionsRequired.get(1)
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this@TestActivity,
                permissionsRequired.get(2)
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@TestActivity,
                    permissionsRequired.get(0)
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    this@TestActivity,
                    permissionsRequired.get(1)
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    this@TestActivity,
                    permissionsRequired.get(2)
                )
            ) {
                val builder = AlertDialog.Builder(this@TestActivity)
                builder.setTitle("Need Multiple Permissions")
                builder.setMessage("This app needs Camera and storage permissions.")
                builder.setPositiveButton(
                    "Grant"
                ) { dialog, which ->
                    dialog.cancel()
                    ActivityCompat.requestPermissions(
                        this@TestActivity,
                        permissionsRequired, PERMISSION_CALLBACK_CONSTANT
                    )
                }
                builder.setNegativeButton(
                    "Cancel"
                ) { dialog, which -> dialog.cancel() }
                builder.show()


            } else if (AppApplication.getPref(AppApplication.getInstance()!!)
                    .getBoolean(permissionsRequired.get(0), false)
            ) {
                val builder = AlertDialog.Builder(this@TestActivity)
                builder.setTitle("Need Multiple Permissions")
                builder.setMessage("This app needs Camera and storage permissions.")
                builder.setPositiveButton(
                    "Grant"
                ) { dialog, which ->
                    dialog.cancel()
                    val intent =
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts(
                        "package",
                        this@TestActivity.getPackageName(),
                        null
                    )
                    intent.data = uri
                    startActivityForResult(
                        intent, REQUEST_PERMISSION_SETTING
                    )
                    Toast.makeText(
                        this@TestActivity,
                        "Go to Permissions to Grant Camera and storage",
                        Toast.LENGTH_LONG
                    )
                }
                builder.setNegativeButton(
                    "Cancel"
                ) { dialog, which -> dialog.cancel() }
                builder.show()
            } else {
                ActivityCompat.requestPermissions(
                    this@TestActivity,
                    permissionsRequired, PERMISSION_CALLBACK_CONSTANT
                )
            }
        } else {
            proceedAfterPermission()
        }
    }


    /**
     * after permission granted
     */
    fun proceedAfterPermission() {
        if (selectedType == 6001) {
            openCameraActivity()
        } else {
            openGallery()
        }
    }

    private var mTempFile: File? = null
    private val TEMP_PHOTO_FILE_NAME =
        System.currentTimeMillis().toString()

    private fun openCameraActivity() {
        val intent = Intent(this@TestActivity, MyProgressCameraActivity::class.java)
        intent.putExtra("whichScreen", "MyProfile")
        startActivityForResult(
            intent,
            CAMERA_REQUEST
        )
    }

    var bitmap: Bitmap? = null

    fun openGallery() {
        //mTempFile = new File(Utility.userImagesPath(), TEMP_PHOTO_FILE_NAME);

        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"


        val pickIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(getIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        startActivityForResult(chooserIntent, GALLERY_PICTURE)


//            val pictureActionIntent = Intent(Intent.ACTION_PICK)
//            pictureActionIntent.type = "image/*"
//            startActivityForResult(
//                pictureActionIntent,
//                GALLERY_PICTURE
//            )
    }
    @Throws(IOException::class)
    fun copyStream(input: InputStream, output: OutputStream) {
        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (input.read(buffer).also { bytesRead = it } != -1) {
            output.write(buffer, 0, bytesRead)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == GALLERY_PICTURE) {
                try {
                    val inputStream = this@TestActivity.contentResolver.openInputStream(
                        data!!.data!!
                    )
                    val fileOutputStream =
                        FileOutputStream(mTempFile)
                    inputStream?.let { copyStream(it, fileOutputStream) }
                    fileOutputStream.close()
                    inputStream!!.close()
                    val options = BitmapFactory.Options()
                    options.inSampleSize = 1
                    val mImageUri =
                        Uri.fromFile(mTempFile!!.absoluteFile)
                    CropImage.activity(mImageUri)
                        .setAspectRatio(1, 1)
                        .setMinCropResultSize(500, 500)
                        .setFixAspectRatio(true)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            } else if (requestCode == CAMERA_REQUEST) {
                val bmOptions = BitmapFactory.Options()
                bmOptions.inSampleSize = 1
                val selectedImagePath = data!!.getStringExtra("path")
                mTempFile = File(selectedImagePath)
                val mImageUri = Uri.fromFile(mTempFile)
                CropImage.activity(mImageUri)
                    .setAspectRatio(1, 1)
                    .setMinCropResultSize(500, 500)
                    .setFixAspectRatio(true)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this)
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val result: CropImage.ActivityResult = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        var  bitmapImage = MediaStore.Images.Media.getBitmap(this@TestActivity.getContentResolver(), result.getUri())
                        val value =
                            (bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth())) as Double
                        val scaled =
                            Bitmap.createScaledBitmap(
                                bitmapImage,
                                512,
                                value.roundToInt(),
                                true
                            )
                        Log.v("Sucess: 2", "scaled:.." + scaled)
                        mUploadMessage!!.onReceiveValue(arrayOf(getImageUri(scaled!!)))
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    mUploadMessage!!.onReceiveValue(null)
                }
            }

            else if (requestCode == 1) {
                if (resultCode != 0) {
                    var res: Uri? = null
                    try {
                        if (resultCode !== Activity.RESULT_OK) {
                            null
                        } else {
                            if (photoFile != null) {
                                var  bitmapImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                                val value =
                                    (bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth())) as Double
                                val scaled =
                                    Bitmap.createScaledBitmap(
                                        bitmapImage,
                                        512,
                                        value.roundToInt(),
                                        true
                                    )
                                Log.v("error: 2", "scaled:.." + scaled)
                                mUploadMessage!!.onReceiveValue(arrayOf(getImageUri(scaled!!)))
                            } else {
                                res = data!!.data
                                var  bitmapImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                    ImageDecoder.decodeBitmap(
                                        ImageDecoder.createSource(
                                            this@TestActivity.contentResolver,
                                            res!!
                                        )
                                    )
                                } else {
                                    MediaStore.Images.Media.getBitmap(
                                        this@TestActivity.contentResolver,
                                        res
                                    )
                                }
                                val value =
                                    (bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth())) as Double
                                val scaled =
                                    Bitmap.createScaledBitmap(
                                        bitmapImage,
                                        512,
                                        value.roundToInt(),
                                        true
                                    )
                                Log.v("error: 2", "scaled:.." + scaled)
                                mUploadMessage!!.onReceiveValue(arrayOf(getImageUri(scaled!!)))
                            }
                        }
                    } catch (e: java.lang.Exception) {
                        Log.v("error: 1 ...", "......" + e.message)
                        Toast.makeText(this@TestActivity, "error: 1 ...", Toast.LENGTH_LONG).show()
                        mUploadMessage!!.onReceiveValue(null)
                    }

                } else {
                    Log.v("error: 3", "else case:..")
                    Toast.makeText(this@TestActivity, "error: 3 ...", Toast.LENGTH_LONG).show()
                    mUploadMessage!!.onReceiveValue(null)

                }
            }
        } catch (ex: Exception) {
            Log.v("error: 2", "camera not work:..")
            this@TestActivity?.showToastMsg("Cann't capture , please upload image again", 2)
        }
    }

    private fun getImageUri(inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            this@TestActivity.getContentResolver(),
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            Log.v("Nisha", "onDestroy:::" + photoFile!!.absolutePath)
        }catch (ex:Exception){
            Log.v("Nisha", "onDestroy ex:::" )
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            Log.v("Nisha", "onPause:::" + photoFile!!.absolutePath)
        }catch (ex:Exception){
            Log.v("Nisha", "onPause ex:::" )
        }
    }

    override fun onStop() {
        super.onStop()
        try {
            Log.v("Nisha", "onStop:::" + photoFile!!.absolutePath)
        }catch (ex:Exception){
            Log.v("Nisha", "onStop ex:::" )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                var i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.setType("image/*")
                startActivityForResult(Intent.createChooser(i, "File Chooser"), 1)
            } else {
                Toast.makeText(this@TestActivity, "Oops you just denied the permission", Toast.LENGTH_LONG)
                    .show();
            }
        }
    }


}
