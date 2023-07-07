package com.vision2020.ui.faceDetection

import BaseFragment
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
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fondesa.kpermissions.PermissionStatus
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.anyPermanentlyDenied
import com.fondesa.kpermissions.anyShouldShowRationale
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.request.PermissionRequest
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.norulab.exofullscreen.MediaPlayer
import com.norulab.exofullscreen.preparePlayer
import com.norulab.exofullscreen.setSource
import com.theartofdev.edmodo.cropper.CropImage
import com.vision2020.AppApplication
import com.vision2020.R
import com.vision2020.adapter.DrugVideoAdapter
import com.vision2020.data.response.DrugList
import com.vision2020.ui.cameralayout.MyCamera
import com.vision2020.ui.views.dialog.DrugListDialog
import com.vision2020.ui.views.dialog.ItemCallBack
import com.vision2020.utils.*
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams
import kotlinx.android.synthetic.main.fragment_effects_on_face.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


class EffectsOnFaceFragment : BaseFragment<FaceViewModel>(), DrugVideoAdapter.ItemClick,
    PermissionRequest.Listener, ImageResponse {
    companion object {
        private const val LEFT_EYE = 4
        private const val RADIUS = 10f
        private const val TEXT_SIZE = 50f
        private const val CORNER_RADIUS = 2f
        private const val STROKE_WIDTH = 5f
    }


    private var drugList = arrayListOf<DrugList.Data>()
    private var drugName: String = "Select Drug"
    private var baseUrl: String = "http://ec2-15-206-63-201.ap-south-1.compute.amazonaws.com:3000"

    // private var baseUrl: String = "http://ec2-54-169-110-218.ap-southeast-1.compute.amazonaws.com:3000"
    // private var baseUrl: String = "http://192.168.1.115:3000"
    // private var baseUrl: String = "http://localhost:3000"
    private var isPermission: Boolean = false
    private val request by lazy { permissionsBuilder(Manifest.permission.READ_EXTERNAL_STORAGE).build() }
    private var mUploadMessage: ValueCallback<Array<Uri?>?>? = null
    override val layoutId: Int
        get() = R.layout.fragment_effects_on_face
    override val viewModel: FaceViewModel
        get() = ViewModelProvider(this).get(FaceViewModel::class.java)

    override fun onCreateStuff() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inItView()
    }

    var clickEvent: String = ""
    var type: String = ""

    // @SuppressLint("SetTextI18n")
    private fun inItView() {
        MediaPlayer.initialize(mContext)
        MediaPlayer.exoPlayer?.preparePlayer(video, false)
        video.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
        toggle1.setOnClickListener(View.OnClickListener {
            if (toggle1.isChecked()) {
                video.visibility = View.VISIBLE
                web.visibility = View.GONE
                video.visibility = View.VISIBLE
                web.visibility = View.GONE
                // tvText.visibility = View.GONE
                if (clickEvent.equals("true")) {
                    MediaPlayer.startPlayer()
                }
                if (drugList.size > 0) {

                } else {

                }

            } else {
                video.visibility = View.GONE
                web.visibility = View.VISIBLE
                web.visibility = View.VISIBLE
                // tvText.visibility = View.VISIBLE
                video.visibility = View.GONE
                if (clickEvent.equals("true")) {
                    MediaPlayer.pausePlayer()
                }
            }
        })


        Log.v(
            "url :", "sadasds:" + baseUrl + "/mask_on_profile?" +
                    "authToken=" + getAppPref().getString(AppConstant.KEY_TOKEN) + "&dgtype=" + if (drugName.equals(
                    "Select Drug"
                )
            ) "alcohol" else drugName.toLowerCase() + "&year=" + drugSeekBar.progress
        )
        web.clearHistory()
        web.clearCache(true)
        web.loadUrl(
            baseUrl + "/mask_on_profile?" +
                    "authToken=" + getAppPref().getString(AppConstant.KEY_TOKEN) + "&dgtype=" + if (drugName.equals(
                    "Select Drug"
                )
            ) "alcohol" else drugName.toLowerCase() + "&year=" + drugSeekBar.progress
        )
        startTimer()
        loaddrug()
        initWebView(requireActivity())

        drugSeekBar.setOnSeekChangeListener(onSeekChange)
        drugSeekBar.setProgress(1f)
    }

    //Declare timer
    var cTimer: CountDownTimer? = null

    //start timer function
    fun startTimer() {
        cTimer = object : CountDownTimer(20000, 1000) { // 20 seconds
            override fun onTick(millisUntilFinished: Long) {
                try {
                    drugSeekBar.setEnabled(false)
                    drugSeekBar.setFocusable(false)
                    editTextDrugName.setFocusable(false)
                    editTextDrugName.setEnabled(false)
                } catch (ex: Exception) {

                }
            }

            override fun onFinish() {
                try {
                    drugSeekBar.setEnabled(true)
                    drugSeekBar.setFocusable(true)
                    editTextDrugName.setFocusable(true)
                    editTextDrugName.setEnabled(true)
                    cancelTimer()
                } catch (ex: Exception) {

                }
            }
        }
        cTimer!!.start()
    }


    //cancel timer
    fun cancelTimer() {
        if (cTimer != null) cTimer!!.cancel()
    }

    private fun loaddrug() {
        progress = mContext.progressDialog(getString(R.string.request))
        request.addListener(this)
        request.send()
        if (mContext.isAppConnected()) {
            progress!!.show()
            mViewModel!!.drugList().observe(requireActivity(), androidx.lifecycle.Observer {
                try {
                    progress!!.dismiss()
                } catch (ex: java.lang.Exception) {

                }
                if (it != null) {
                    if (it.status_code == AppConstant.CODE_SUCCESS && it.data != null) {
                        drugList = it.data!!
                        //editTextDrugName.setText(drugList[0].dgName)
                        try {
                            editTextDrugName.setText("Select Drug")
                        } catch (ex: Exception) {

                        }
                        if (isPermission) {
                            //drugName = drugList[0].dgName
                            drugName = "Select Drug"
                            getVideoList()
                        }
                    } else {
                        mContext.responseHandler(it.status_code, it.message)
                    }
                } else {
                    mContext.showToastMsg(getString(R.string.server_error), 2)
                }
            })
        }
    }

    var onSeekChange = object : OnSeekChangeListener {
        override fun onSeeking(seekParams: SeekParams) {
            drugSeekBar.setProgress(seekParams.progressFloat)
            Log.v(
                "url :", "sadasds:" + baseUrl + "/mask_on_profile?" +
                        "authToken=" + getAppPref().getString(AppConstant.KEY_TOKEN) + "&dgtype=" + if (drugName.equals(
                        "Select Drug"
                    )
                ) "alcohol" else drugName.toLowerCase() + "&year=" + drugSeekBar.progress
            )
            web.clearHistory()
            web.clearCache(true)
            startTimer()
            web.loadUrl(
                baseUrl + "/mask_on_profile?" +
                        "authToken=" + getAppPref().getString(AppConstant.KEY_TOKEN) + "&dgtype=" + if (drugName.equals(
                        "Select Drug"
                    )
                ) "alcohol" else drugName.toLowerCase() + "&year=" + drugSeekBar.progress
            )
        }

        override fun onStartTrackingTouch(seekBar: IndicatorSeekBar?) {}
        override fun onStopTrackingTouch(seekBar: IndicatorSeekBar?) {}
    }

    private fun setData() {
        if (drugList.isNotEmpty()) {
            DrugListDialog(mContext, R.style.pullBottomFromTop, R.layout.dialog_options,
                drugList, "Select Drug", object : ItemCallBack.Callback {
                    override fun selected(pos: Int) {
                        editTextDrugName.setText(drugList[pos].dgName)
                        drugName = drugList[pos].dgName
                        if (isPermission) {
                            getVideoList()
                        }
                        drugSeekBar.setProgress(1f)

                        if (toggle1.isChecked != true) {
                            web.visibility = View.VISIBLE
                            // tvText.visibility = View.VISIBLE
                            video.visibility = View.GONE
                            if (clickEvent.equals("true")) {
                                MediaPlayer.pausePlayer()
                            }
                        }
                        Log.v(
                            "url :", "sadasds:" + baseUrl + "/mask_on_profile?" +
                                    "authToken=" + getAppPref().getString(AppConstant.KEY_TOKEN) + "&dgtype=" + if (drugName.equals(
                                    "Select Drug"
                                )
                            ) "alcohol" else drugName.toLowerCase() + "&year=" + drugSeekBar.progress
                        )
                        try {
                            web.clearHistory()
                            web.clearCache(true)
                            startTimer()
                            web.loadUrl(
                                baseUrl + "/mask_on_profile?" +
                                        "authToken=" + getAppPref().getString(AppConstant.KEY_TOKEN) + "&dgtype=" + if (drugName.equals(
                                        "Select Drug"
                                    )
                                ) "alcohol" else drugName.toLowerCase() + "&year=" + drugSeekBar.progress
                            )
                        } catch (ex: Exception) {

                        }
                    }

                }).show()
        }
    }


    private fun getVideoList() {
        val videoHashSet = HashSet<String>()
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.SIZE
        )
        val selection = MediaStore.Video.Media.DATA + " like?"
        //val selectionArgs = arrayOf("%Knox Manager/content%")
        val selectionArgs = arrayOf("%Drug/$drugName%")
        val videoCursor = mContext.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,
            selection, selectionArgs, MediaStore.Video.Media.DATE_TAKEN + " DESC"
        )
        try {
            videoCursor!!.moveToFirst()
            do {
                videoHashSet.add(
                    videoCursor.getString(
                        videoCursor.getColumnIndexOrThrow(
                            MediaStore.Video.Media.DISPLAY_NAME
                        )
                    ) + "::@" + videoCursor.getString(videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                )
            } while (videoCursor.moveToNext())
            videoCursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            videoCursor?.close()
        }
        try {
            rvVideo.layoutManager = LinearLayoutManager(mContext)
            val videoItemHashSet = HashSet<String>()
            for (i in 0 until videoHashSet.size) {
                //if (videoHashSet.elementAt(i).startsWith(drugName.toLowerCase())) {
                videoItemHashSet.add(videoHashSet.elementAt(i))
                //}
            }
            val downloadedList: ArrayList<String> = ArrayList(videoItemHashSet)
            val set: Set<String> = HashSet(downloadedList)
            downloadedList.clear()
            downloadedList.addAll(set)
            val videoAdapter = DrugVideoAdapter(mContext, downloadedList, this)
            rvVideo.adapter = videoAdapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        MediaPlayer.pausePlayer()
    }

    override fun onDestroy() {
        MediaPlayer.stopPlayer()
        super.onDestroy()
    }

    override fun initListeners() {
        editTextDrugName.setOnClickListener {
            setData()
        }
    }

    override fun getPosition(path: String) {
        if (toggle1.isChecked == true) {
            MediaPlayer.exoPlayer?.setSource(mContext, Uri.parse(path).toString())
            MediaPlayer.startPlayer()
            clickEvent = "true"
        } else {
            MediaPlayer.exoPlayer?.setSource(mContext, Uri.parse(path).toString())
            MediaPlayer.startPlayer()
            clickEvent = "true"
            toggle1.isChecked = true
            video.visibility = View.VISIBLE
            web.visibility = View.GONE
            //tvText.visibility = View.GONE
            clickEvent = "true"
        }
    }

    override fun onPermissionsResult(result: List<PermissionStatus>) {
        when {
            result.anyPermanentlyDenied() -> mContext.showPermanentlyDeniedDialog(result)
            result.anyShouldShowRationale() -> mContext.showRationaleDialog(result, request)
            result.allGranted() -> isPermission = true
        }
    }

    private var m_downX = 0f

    @SuppressLint("ClickableViewAccessibility")
    private fun initWebView(mcon: Activity) {
        // mUploadMessage.onReceiveValue( null)
        web.setWebChromeClient(MyWebChromeClient(mcon))

        web.setWebViewClient(object : WebViewClient() {
            override fun onPageStarted(
                view: WebView,
                url: String,
                favicon: Bitmap?
            ) {
                super.onPageStarted(view, url, favicon)
                try {
                    requireActivity().invalidateOptionsMenu()
                } catch (ex: Exception) {
                }
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                web.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                try {
                    requireActivity().invalidateOptionsMenu()
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
        web.clearCache(true)
        web.clearHistory()
        web.getSettings().setJavaScriptEnabled(true)
        web.setHorizontalScrollBarEnabled(false)
        web.setOnTouchListener(View.OnTouchListener { v, event ->
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

    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openFileExplorer()
            return
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 123
            )
            // mUploadMessage!!.onReceiveValue(null)
        }
    }

    private fun openFileExplorer() {
        if (!hasPermissions(
                requireActivity()!!,
                *PERMISSIONS
            )
        ) {
            //mUploadMessage!!.onReceiveValue(null)
            requestPermissions(PERMISSIONS, PERMISSION_ALL)
        } else ImageCapture(requireActivity()!!, this@EffectsOnFaceFragment, false)
    }

    //private var mCapturedImageURI: Uri? = null
    var photoFile: File? = null

    private inner class MyWebChromeClient(var context: Activity) : WebChromeClient() {
        override fun onShowFileChooser(
            webView: WebView?, filePathCallback: ValueCallback<Array<Uri?>?>,
            fileChooserParams: FileChooserParams?
        ): Boolean {
            mUploadMessage = filePathCallback
            openFileExplorer()
            return true
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
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
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
                requireActivity()!!,
                permissionsRequired.get(0)
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                requireActivity()!!,
                permissionsRequired.get(1)
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                requireActivity()!!,
                permissionsRequired.get(2)
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity()!!,
                    permissionsRequired.get(0)
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity()!!,
                    permissionsRequired.get(1)
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity()!!,
                    permissionsRequired.get(2)
                )
            ) {
                val builder = AlertDialog.Builder(requireActivity()!!)
                builder.setTitle("Need Multiple Permissions")
                builder.setMessage("This app needs Camera and storage permissions.")
                builder.setPositiveButton(
                    "Grant"
                ) { dialog, which ->
                    dialog.cancel()
                    ActivityCompat.requestPermissions(
                        requireActivity()!!,
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
                val builder = AlertDialog.Builder(requireActivity()!!)
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
                        requireActivity()!!.getPackageName(),
                        null
                    )
                    intent.data = uri
                    startActivityForResult(
                        intent, REQUEST_PERMISSION_SETTING
                    )
                    Toast.makeText(
                        requireActivity()!!,
                        "Go to Permissions to Grant Camera and storage",
                        Toast.LENGTH_LONG
                    )
                }
                builder.setNegativeButton(
                    "Cancel"
                ) { dialog, which ->
                    mUploadMessage!!.onReceiveValue(null)
                    dialog.cancel()
                }
                builder.show()
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity()!!,
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
            // mUploadMessage!!.onReceiveValue(null)
            openCameraActivity()
        } else {
            openGallery()
        }
    }

    private var mTempFile: File? = null
    private val TEMP_PHOTO_FILE_NAME =
        System.currentTimeMillis().toString()

    private fun openCameraActivity() {
        // val intent = Intent(requireActivity()!!, MyProgressCameraActivity::class.java)
        val intent = Intent(requireActivity()!!, MyCamera::class.java)

        startActivityForResult(
            intent,
            CAMERA_REQUEST
        )
    }

    var bitmap: Bitmap? = null

    fun openGallery() {
        val pictureActionIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pictureActionIntent.type = "image/*"
        startActivityForResult(
            pictureActionIntent,
            GALLERY_PICTURE
        )
    }
//        val intent = Intent(Intent.ACTION_PICK,
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        intent.type = "image/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//        startActivityForResult(Intent.createChooser(intent,
//                "Select Picture"), GALLERY_PICTURE)
//    }


    @Throws(IOException::class)
    fun copyStream(input: InputStream, output: OutputStream) {
        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (input.read(buffer).also { bytesRead = it } != -1) {
            output.write(buffer, 0, bytesRead)
        }
    }

    lateinit var bitmapImage: Bitmap
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            Log.v("Nisha", "Code::" + resultCode)
            if (requestCode == GALLERY_PICTURE) {
                if (resultCode == 0) {
                    mUploadMessage!!.onReceiveValue(null)
                } else {
                    try {
                        val inputStream = requireActivity()!!.contentResolver.openInputStream(
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
                        try {
                            var bitmapImage = MediaStore.Images.Media.getBitmap(
                                requireActivity()!!.getContentResolver(),
                                mImageUri
                            )
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

                        /* CropImage.activity(mImageUri)
                        .setAspectRatio(1, 1)
                        .setMinCropResultSize(500, 500)
                        .setFixAspectRatio(true)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(requireContext()!!, this)*/
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            } else if (requestCode == CAMERA_REQUEST) {
                if (resultCode == 0) {
                    mUploadMessage!!.onReceiveValue(null)
                } else {
                    val bmOptions = BitmapFactory.Options()
                    bmOptions.inSampleSize = 1
                    val selectedImagePath = data!!.getStringExtra("path")
                    mTempFile = File(selectedImagePath)
                    val mImageUri = Uri.fromFile(mTempFile)

                    try {
                        var bitmapImage = MediaStore.Images.Media.getBitmap(
                            requireActivity()!!.getContentResolver(),
                            mImageUri
                        )

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
                        Toast.makeText(context, "error::" + e.message, Toast.LENGTH_LONG).show()
                    }
                }
                /*   CropImage.activity(mImageUri)
                       .setAspectRatio(1, 1)
                       .setMinCropResultSize(500, 500)
                       .setFixAspectRatio(true)
                       .setGuidelines(CropImageView.Guidelines.ON)
                       .start(requireContext()!!, this)*/
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val result: CropImage.ActivityResult = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        var bitmapImage = MediaStore.Images.Media.getBitmap(
                            requireActivity()!!.getContentResolver(),
                            result.getUri()
                        )
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
            } else if (requestCode == 1) {
                if (resultCode != 0) {
                    var res: Uri? = null
                    try {
                        if (resultCode !== Activity.RESULT_OK) {
                            null
                        } else {
                            if (photoFile != null) {
                                var bitmPapImage =
                                    BitmapFactory.decodeFile(photoFile!!.absolutePath)
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
                                var bitmapImage =
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                        ImageDecoder.decodeBitmap(
                                            ImageDecoder.createSource(
                                                requireContext().contentResolver,
                                                res!!
                                            )
                                        )
                                    } else {
                                        MediaStore.Images.Media.getBitmap(
                                            requireContext().contentResolver,
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
                        Toast.makeText(context, "error: 1 ...", Toast.LENGTH_LONG).show()
                        mUploadMessage!!.onReceiveValue(null)
                    }

                } else {
                    Log.v("error: 3", "else case:..")
                    Toast.makeText(context, "error: 3 ...", Toast.LENGTH_LONG).show()
                    mUploadMessage!!.onReceiveValue(null)

                }
            }
        } catch (ex: Exception) {
            Log.v("error: 2", "camera not work:..")
            try {
                mUploadMessage!!.onReceiveValue(null)
            } catch (ex: java.lang.Exception) {

            }
        }
    }

    private fun getImageUri(inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            requireActivity().getContentResolver(),
            inImage,
            "IMG_" + Calendar.getInstance().getTime(),
            null
        )
        return Uri.parse(path)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123 || requestCode == PERMISSION_ALL) {
            if (grantResults.size > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                ImageCapture(requireActivity()!!, this@EffectsOnFaceFragment, false)
            } else {
                mUploadMessage!!.onReceiveValue(null)
                Toast.makeText(activity, "You denied the permission", Toast.LENGTH_LONG)
                    .show();
            }
        }

    }

    override fun onDestroyView() {
        Log.e("App 2020::", "distroy view")
        super.onDestroyView()
        web.clearHistory()
        web.clearCache(true)
        web.loadUrl("about:blank")
    }
}




