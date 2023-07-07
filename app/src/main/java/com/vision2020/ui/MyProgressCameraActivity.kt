package com.vision2020.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Point
import android.hardware.Camera
import android.hardware.Camera.CameraInfo
import android.hardware.Camera.PictureCallback
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.exifinterface.media.ExifInterface
import com.vision2020.R
import java.io.*
import java.sql.Timestamp
import java.util.*

class MyProgressCameraActivity : Activity() {
    private var hasHardKeyMenus = false
    private var file: String? = null
    private var cameraID = 0
    lateinit var camera: Camera
    private var display = null
    var width = 0
    var height = 0
    private var cameraConfigured = false
    private var inPreview = false
    private lateinit var capturedImageData: ByteArray
    private var pictureFile: File? = null
    private var enablePreview = false

    //String whichScreen = "";
    var fileName: String? = null
    lateinit var previewHolder: SurfaceHolder
    lateinit var camera_flip: ImageView
    lateinit var close_camera_btn: ImageView
    lateinit var image_capture_btn: ImageView
    lateinit var camera_back: ImageView
    lateinit var camera_tick: ImageView
    lateinit var camera_preview_Image: ImageView
    lateinit var camera_footer: LinearLayout
    lateinit var camera_header: RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myprogress_camera)
        var previewView = findViewById(R.id.camera_surfce_view) as SurfaceView

        previewHolder = previewView!!.getHolder()
        camera_flip = findViewById<View>(R.id.camera_flip) as ImageView
        image_capture_btn = findViewById<View>(R.id.image_capture_btn) as ImageView
        close_camera_btn = findViewById<View>(R.id.close_camera_btn) as ImageView
        camera_back = findViewById<View>(R.id.camera_back) as ImageView
        camera_tick = findViewById<View>(R.id.camera_tick) as ImageView
        camera_preview_Image = findViewById<View>(R.id.camera_preview_Image) as ImageView
        camera_footer = findViewById<View>(R.id.camera_footer) as LinearLayout
        camera_header = findViewById<View>(R.id.camera_header) as RelativeLayout
        hasHardKeyMenus = ViewConfiguration.get(applicationContext).hasPermanentMenuKey()
        var displayMetrics =  DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        setMyPreviewSize(displayMetrics!!.widthPixels,displayMetrics!!.heightPixels,previewView)
        previewHolder!!.addCallback(surfaceCallback)
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        var va = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        width = va.defaultDisplay.width
        height = va.defaultDisplay.height
        file = (Environment.getExternalStorageDirectory().path + "/vision/media/images/")
        val path = File(file)
        if (!path.exists()) {
            path.mkdirs()
        }
        setListeners()
        openCamera()
    }


    private fun setMyPreviewSize(width: Int, height: Int,previewView:SurfaceView) {
        // Get the set dimensions
        val newProportion = width.toFloat() / height.toFloat()

        // Get the width of the screen
        val screenWidth = windowManager.defaultDisplay.width
        val screenHeight = windowManager.defaultDisplay.height
        val screenProportion =
            screenWidth.toFloat() / screenHeight.toFloat()

        // Get the SurfaceView layout parameters
        val lp: ViewGroup.LayoutParams = previewView.getLayoutParams()
        if (newProportion > screenProportion) {
            lp.width = screenWidth
            lp.height = (screenWidth.toFloat() / newProportion).toInt()
        } else {
            lp.width = (newProportion * screenHeight.toFloat()).toInt()
            lp.height = screenHeight
        }
        // Commit the layout parameters
        previewView.setLayoutParams(lp)
    }

    fun setListeners() {
        image_capture_btn.setOnClickListener(View.OnClickListener {
            isRotated = false
            takePicture()
        })
        camera_flip.setOnClickListener(View.OnClickListener { switchCamera() })
        close_camera_btn.setOnClickListener(View.OnClickListener { onBackPressed() })
        camera_back.setOnClickListener(View.OnClickListener {
            camera!!.startPreview()
            enablePreview = false
            disablePreview()
        })
        camera_tick.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            intent.putExtra("path", pictureFile!!.absolutePath)
            setResult(RESULT_OK, intent)
            finish()
        })
    }

    public override fun onResume() {
        super.onResume()
    }

    private fun openCamera() {
        cameraID = findFrontFacingCamera()
        camera = Camera.open(cameraID)
        try {
            if (camera != null && previewHolder!!.surface != null) {
                camera!!.setPreviewDisplay(previewHolder)
                camera!!.setDisplayOrientation(cameraDisplayOrientation)
                if (!cameraConfigured) {
                    val parameters = camera!!.parameters
                    val size: Camera.Size = getOptimalPreviewSize(
                        parameters.supportedPreviewSizes,
                        width, height
                    )
                    if (size != null) {
                        parameters.setPreviewSize(size.width, size.height)
                        if (cameraID == CameraInfo.CAMERA_FACING_BACK) {
                            val pm = packageManager
                            if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) && pm.hasSystemFeature(
                                    PackageManager.FEATURE_CAMERA_AUTOFOCUS
                                )
                            ) {
                                parameters.focusMode =
                                    Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
                            }
                        }
                        camera!!.parameters = parameters
                        camera!!.setDisplayOrientation(cameraDisplayOrientation)
                        cameraConfigured = true
                    }
                }
            }
            startPreview()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //enable preview
        previewEnable()
    }

    private fun findFrontFacingCamera(): Int {
        var cameraId = -1
        // Search for the front facing camera
        val numberOfCameras = Camera.getNumberOfCameras()
        for (i in 0 until numberOfCameras) {
            val info = CameraInfo()
            Camera.getCameraInfo(i, info)
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                Log.d("Camera:::", "Camera found")
                cameraId = i
                break
            }
        }
        return cameraId
    }

    public override fun onPause() {
        if (inPreview) {
            camera!!.stopPreview()
        }
        camera!!.release()
        inPreview = false
        super.onPause()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    /*
     * take picture
     * */
    fun takePicture() {
        val parameters = camera!!.parameters
        if (cameraID == CameraInfo.CAMERA_FACING_BACK) {
            val pm = packageManager
            if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) && pm.hasSystemFeature(
                    PackageManager.FEATURE_CAMERA_AUTOFOCUS
                )
            ) {
                parameters.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
            }
        }
        camera!!.parameters = parameters
        try {
            camera!!.takePicture(null, null, mPicture)
        }catch (ex: Exception){

        }
    }

    @Throws(Throwable::class)
    private fun initPreview(width: Int, height: Int) {
        if (camera != null && previewHolder!!.surface != null) {
            camera!!.setPreviewDisplay(previewHolder)
            if (!cameraConfigured) {
                val parameters = camera!!.parameters
                val size: Camera.Size =
                    getBestPreviewSize(width, height, parameters)
                if (size != null) {
                    parameters.setPreviewSize(size.width, size.height)
                    if (cameraID == CameraInfo.CAMERA_FACING_BACK) {
                        val pm = packageManager
                        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) && pm.hasSystemFeature(
                                PackageManager.FEATURE_CAMERA_AUTOFOCUS
                            )
                        ) {
                            parameters.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
                        }
                    }
                    camera!!.parameters = parameters
                    camera!!.setDisplayOrientation(cameraDisplayOrientation)
                    cameraConfigured = true
                }
            }
        }
    }

    fun getBestPreviewSize(
        width: Int,
        height: Int,
        parameters: Camera.Parameters
    ): Camera.Size {
        var result: Camera.Size? = null
        for (size in parameters.supportedPreviewSizes) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size
                } else {
                    val resultArea = result.width * result.height
                    val newArea = size.width * size.height
                    if (newArea > resultArea) {
                        result = size
                    }
                }
            }
        }
        return result!!
    }

    private fun startPreview() {
        Log.d(TAG, "start preview called")
        if (cameraConfigured && camera != null) {
            Log.d(TAG, "stuff works")
            camera!!.startPreview()
            inPreview = true
        }
    }

    var surfaceCallback: SurfaceHolder.Callback = object : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            // no-op -- wait until surfaceChanged()
        }

        override fun surfaceChanged(
            holder: SurfaceHolder,
            format: Int,
            width: Int,
            height: Int
        ) {
            try {
                initPreview(width, height)
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
            startPreview()
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            // no-op
        }
    }// back-facing

    // compensate the mirror
    val cameraDisplayOrientation: Int
        get() {
            val info = CameraInfo()
            Camera.getCameraInfo(cameraID, info)
            val rotation = windowManager.defaultDisplay.rotation
            var degrees = 0
            when (rotation) {
                Surface.ROTATION_0 -> degrees = 0
                Surface.ROTATION_90 -> degrees = 90
                Surface.ROTATION_180 -> degrees = 180
                Surface.ROTATION_270 -> degrees = 270
            }
            var result: Int
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                result = (info.orientation + degrees) % 360
                result = (360 - result) % 360 // compensate the mirror
            } else {  // back-facing
                result = (info.orientation - degrees + 360) % 360
            }
            return result
        }

    fun switchCamera() {
        Log.d(TAG, "switch called")
        try {
            if (!hasHardKeyMenus) {
                if (inPreview) {
                    camera!!.stopPreview()
                }
                camera!!.release()
                cameraID = (cameraID + 1) % 2
                camera = Camera.open(cameraID)
                if (camera != null && previewHolder!!.surface != null) {
                    camera!!.setPreviewDisplay(previewHolder)
                    val parameters = camera!!.parameters
                    val size: Camera.Size = getOptimalPreviewSize(
                        parameters.supportedPreviewSizes,
                        width,
                        height
                    )
                    if (size != null) {
                        parameters.setPreviewSize(size.width, size.height)
                        if (cameraID == CameraInfo.CAMERA_FACING_BACK) {
                            val pm = packageManager
                            if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) && pm.hasSystemFeature(
                                    PackageManager.FEATURE_CAMERA_AUTOFOCUS
                                )
                            ) {
                                parameters.focusMode =
                                    Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
                            }
                        }
                        camera!!.parameters = parameters
                        camera!!.setDisplayOrientation(cameraDisplayOrientation)
                        cameraConfigured = true
                    }
                }
                camera.startPreview()
            } else {
                if (inPreview) {
                    camera!!.stopPreview()
                }
                camera!!.release()
                cameraID = (cameraID + 1) % 2
                camera = Camera.open(cameraID)
                camera.setDisplayOrientation(cameraDisplayOrientation)
                try {
                    camera.setPreviewDisplay(previewHolder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                camera.startPreview()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getOptimalPreviewSize(
        sizes: List<Camera.Size>?,
        w: Int,
        h: Int
    ): Camera.Size {
        val ASPECT_TOLERANCE = 0.1
        val targetRatio = h.toDouble() / w
        if (sizes == null) return null!!
        var optimalSize: Camera.Size? = null
        var minDiff = Double.MAX_VALUE
        for (size in sizes) {
            val ratio = size.width.toDouble() / size.height
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue
            if (Math.abs(size.height - h) < minDiff) {
                optimalSize = size
                minDiff = Math.abs(size.height - h).toDouble()
            }
        }
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE
            for (size in sizes) {
                if (Math.abs(size.height - h) < minDiff) {
                    optimalSize = size
                    minDiff = Math.abs(size.height - h).toDouble()
                }
            }
        }
        return optimalSize!!
    }

    var mPicture = PictureCallback { data, camera ->
        capturedImageData = data
        camera.stopPreview()
        SaveImageInLocalStorage().execute()
    }

    public override fun onDestroy() {
        super.onDestroy()
    }

    private fun createFileName(): String {
        val date = Date()
        val fName =
            (Timestamp(date.time).time / 1000).toString()
        fileName = fName
        return fName
    }

    fun onAnalyticsSuccess() {}
    fun onAnalyticsFailure(erro: String?) {}
    private inner class SaveImageInLocalStorage :
        AsyncTask<Void?, Void?, Void?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            //progress!!.show()
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            //progress!!.dismiss()
            enablePreview = true

            showPreview()
        }

        override fun doInBackground(vararg params: Void?): Void? {
            pictureFile = File(file, System.currentTimeMillis().toString())
            if (pictureFile!!.exists()) {
                pictureFile!!.delete()
            }
            try {
                //progress!!.dismiss()
                var realImage =
                    BitmapFactory.decodeByteArray(capturedImageData, 0, capturedImageData.size)
                val info = CameraInfo()
                Camera.getCameraInfo(cameraID, info)
                //val bitmap: Bitmap? = rotate(realImage, 270)
                var cameraFace = -1
                if (cameraID == CameraInfo.CAMERA_FACING_FRONT) {
                    cameraFace = CameraInfo.CAMERA_FACING_FRONT
                    val matrix = Matrix()
                    matrix.preScale(-1.0f, 1.0f)
                    realImage = Bitmap.createBitmap(
                        realImage!!,
                        0,
                        0,
                        realImage.width,
                        realImage.height,
                        matrix,
                        true
                    )
                } else if (cameraID == CameraInfo.CAMERA_FACING_BACK) {
                    cameraFace = CameraInfo.CAMERA_FACING_BACK

//                    realImage = Bitmap.createBitmap(realImage, 0, 0, realImage.getWidth(), realImage.getHeight(), mtx, true);
                }
                val fos = FileOutputStream(pictureFile)
                realImage!!.compress(Bitmap.CompressFormat.JPEG, 80, fos)
                fos.close()

                //MOVE TO NEXT FRAGMENT
                //showPreview(cameraFace);
            } catch (e: FileNotFoundException) {
                Log.d("Info", "File not found: " + e.message)
                //progress!!.dismiss()
            } catch (e: IOException) {
                Log.d("TAG", "Error accessing file: " + e.message)
                // progress!!.dismiss()
            }
            return null
        }
    }

    var isRotated = false
    fun rotate(bitmap: Bitmap, degree: Int): Bitmap? {
        val w = bitmap.width
        val h = bitmap.height
        val mtx = Matrix()
        //mtx.postRotate(degree);
        mtx.setRotate(360f)
        Log.w("TAG", "-- isRotated1::" + isRotated)
        if (isRotated == false) {
            isRotated = true
            return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true)
        } else {
            return bitmap
        }
    }

    @Throws(IOException::class)
    private fun rotateImageIfRequired(selectedImage: String): Bitmap? {
        val mat = Matrix()
        try {
            val f = File(selectedImage)
            val exif =
                ExifInterface(f.path)
            val orientation: Int = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            var angle = 360f

            Log.w("TAG", "-- rotation::" + angle)
            Log.w("TAG", "-- isRotated::" + isRotated)
            if (isRotated == false) {
                isRotated = true
                mat.postRotate(angle)
                val options = BitmapFactory.Options()
                options.inSampleSize = 2
                val bmp = BitmapFactory.decodeStream(
                    FileInputStream(f),
                    null, options
                )
                return Bitmap.createBitmap(
                    bmp!!, 0, 0, bmp!!.width,
                    bmp.height, mat, true
                )
            } else {
                mat.postRotate(0f)
                val options = BitmapFactory.Options()
                options.inSampleSize = 2
                val bmp = BitmapFactory.decodeStream(
                    FileInputStream(f),
                    null, options
                )
                return Bitmap.createBitmap(
                    bmp!!, 0, 0, bmp!!.width,
                    bmp!!.height, mat, true
                )
            }

        } catch (e: IOException) {
            Log.w("TAG", "-- Error in setting image")
            return null
        } catch (oom: OutOfMemoryError) {
            Log.w("TAG", "-- OOM Error in setting image")
            return null
        }

    }

    /*
     * preview method
     * */
    fun previewEnable() {
        if (enablePreview) {
            showPreview()
        } else {
            disablePreview()
        }
    }

    /*
     * show preview
     * */
    fun showPreview() {
        val intent = Intent()
        intent.putExtra("path", pictureFile!!.absolutePath)
        setResult(RESULT_OK, intent)
        finish()
       /* camera_preview_Image.setVisibility(View.VISIBLE)
        camera_footer.setVisibility(View.GONE)
        camera_header.setVisibility(View.VISIBLE)

        //camera_preview_Image.setImageBitmap(bitmap)
        camera_preview_Image.setImageBitmap(BitmapFactory.decodeFile(pictureFile!!.absolutePath))*/
    }

    /*
     * disable preview
     * */
    fun disablePreview() {
        camera_preview_Image.setVisibility(View.GONE)
        camera_footer.setVisibility(View.VISIBLE)
        camera_header.setVisibility(View.GONE)
    }

    companion object {
        val TAG = MyProgressCameraActivity::class.java.simpleName
    }
}