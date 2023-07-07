package com.vision2020.ui.faceDetection

import BaseFragment
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.vision2020.R
import com.vision2020.utils.AppAlertDialog
import com.vision2020.utils.AppConstant
import com.vision2020.utils.AppConstant.KEY_AGE
import com.vision2020.utils.AppConstant.KEY_NAME
import com.vision2020.utils.AppConstant.KEY_PATH
import com.vision2020.utils.AppConstant.KEY_TYPE
import com.vision2020.utils.AppConstant.KEY_USER_TYPE
import com.vision2020.utils.showToastMsg
import kotlinx.android.synthetic.main.fragment_face.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*


class FaceReconFragment : BaseFragment<FaceViewModel>(), AppAlertDialog.GetClick {
    private var filePath:String =""
    private var fileType:String =""
    override val layoutId: Int
        get() = R.layout.fragment_face
    override val viewModel: FaceViewModel
        get() = ViewModelProvider(this).get(FaceViewModel::class.java)
    var myBitmap: Bitmap? = null
    var picUri: Uri? = null
    override fun onCreateStuff() {

    }

    override fun initListeners() {
        tvEffectOnFace.setOnClickListener {
            when {
                editTextName.text!!.trim().isEmpty() -> {
                    mContext.showToastMsg("Please Enter Name First!",AppConstant.ERROR)
                }
                editTextAge.text!!.trim().isEmpty() -> {
                    mContext.showToastMsg("Please Enter Age First!",AppConstant.ERROR)
                }
                filePath.isEmpty() -> {
                    mContext.showToastMsg("Please Set Photo First!",AppConstant.ERROR)
                }
               /* else -> {
                    val bundle = bundleOf(KEY_NAME to editTextName.text.toString(),KEY_AGE to editTextAge.text.toString(), KEY_PATH to filePath,
                        KEY_TYPE to fileType, KEY_USER_TYPE to byteArray)
                    findNavController().navigate(R.id.action_navigation_face_to_effectsOnFaceFragment,bundle)
                }*/
            }

        }
        tvEffectsOnBrain.setOnClickListener {

        }

        tvEffectsOnLungs.setOnClickListener {

        }

        imgViewCamera.setOnClickListener {
            AppAlertDialog().selectOptions(activity,object : AppAlertDialog.GetClick {
                override fun response(type: String) {
                    if (type.equals("camera")) {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(
                            intent, 1
                        )
                    } else if(type.equals("gallery")) {
                        val intent = Intent()
                        intent.type = "image/*"
                        intent.action = Intent.ACTION_GET_CONTENT
                        startActivityForResult(
                            Intent.createChooser(intent, "Select Picture"),
                            2
                        )
                    }else{

                    }
                }
            })


          // startActivityForResult(getPickImageChooserIntent(), 200)
          /*  imgPicker = ImagePicker()
            imgPicker.withFragment(this)
                .withCompression(false)
                .start()*/

          /*  val intent = Intent(context,ImageSelectActivity::class.java)
            intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true)
            intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true)
            intent.putExtra(ImageSelectActivity.FLAG_COMPRESS,false)
            startActivityForResult(intent, 1213)*/
            //mContext.navigateForCamera(requireActivity())
        }

    }
    private fun getCaptureImageOutputUri(): Uri? {
        var outputFileUri: Uri? = null
        val getImage: File? = mContext.externalCacheDir
        if (getImage != null) {
            outputFileUri =
                Uri.fromFile(File(getImage.path, "profile.png"))
        }
        return outputFileUri
    }

    fun getPickImageChooserIntent(): Intent? {

        // Determine Uri of camera image to save.
        val outputFileUri: Uri? = getCaptureImageOutputUri()
        val allIntents: MutableList<Intent> = ArrayList()
        val packageManager: PackageManager = mContext.packageManager

        // collect all camera intents
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val listCam =
            packageManager.queryIntentActivities(captureIntent, 0)
        for (res in listCam) {
            val intent = Intent(captureIntent)
            intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            intent.setPackage(res.activityInfo.packageName)
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
            }
            allIntents.add(intent)
        }

        // collect all gallery intents
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galleryIntent.type = "image/*"
        val listGallery =
            packageManager.queryIntentActivities(galleryIntent, 0)
        for (res in listGallery) {
            val intent = Intent(galleryIntent)
            intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            intent.setPackage(res.activityInfo.packageName)
            allIntents.add(intent)
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        var mainIntent: Intent? = allIntents[allIntents.size - 1]
        for (intent in allIntents) {
            if (intent.component!!.getClassName() == "com.android.documentsui.DocumentsActivity") {
                mainIntent = intent
                break
            }
        }
        allIntents.remove(mainIntent)

        // Create a chooser from the main intent
        val chooserIntent = Intent.createChooser(mainIntent, "Select source")

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toTypedArray())
        return chooserIntent
    }

    fun getPickImageResultUri(data: Intent?): Uri? {
        var isCamera = true
        if (data != null) {
            val action = data.action
            isCamera = action != null && action == MediaStore.ACTION_IMAGE_CAPTURE
        }
        return if (isCamera) getCaptureImageOutputUri() else data!!.data
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("pic_uri", picUri)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(savedInstanceState != null) {
            picUri = savedInstanceState!!.getParcelable<Uri>("pic_uri")
        }
    }

    lateinit var byteArray:ByteArray
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 1) {
            if (resultCode === Activity.RESULT_OK) {
                val stream = ByteArrayOutputStream()
                var bmp = data!!.getExtras()!!.get("data") as Bitmap
                bmp.compress(Bitmap.CompressFormat.PNG, 50, stream)
                byteArray = stream.toByteArray()
                // convert byte array to Bitmap
                val bitmap = BitmapFactory.decodeByteArray(
                    byteArray, 0,
                    byteArray.size
                )
                filePath = "camera"
                fileType = "camera"
                imgViewUserPic.setImageBitmap(bitmap)
            }
        }
        else if(requestCode == 2 ){
            if (resultCode === Activity.RESULT_OK) {
                try {
                    var imageUri = data!!.getData()
                    var imageStream = mContext.getContentResolver().openInputStream(imageUri!!)
                    var selectedImage = BitmapFactory.decodeStream(imageStream)
                    filePath = imageUri.toString()
                    fileType = "gallery"

                    imgViewUserPic.setImageBitmap(selectedImage)
                    val stream = ByteArrayOutputStream()
                    byteArray=stream.toByteArray()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(mContext, "You haven't picked Image", Toast.LENGTH_LONG).show()
            }
        }
    }

    @Throws(IOException::class)
    private fun rotateImageIfRequired(img: Bitmap, selectedImage: Uri): Bitmap? {
        val ei = ExifInterface(selectedImage.path!!)
        val orientation = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270)
            else -> img
        }
    }

    private fun rotateImage(img: Bitmap, degree: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedImg =
            Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 0) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    private class MyBrowser : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView,
            url: String
        ): Boolean {
            view.loadUrl(url)
            return true
        }
    }
}
