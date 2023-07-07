package com.vision2020.ui.user

import BaseActivity
import `in`.mayanknagwanshi.imagepicker.ImageSelectActivity
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vision2020.AppApplication
import com.vision2020.R
import com.vision2020.data.request.SignUpReq
import com.vision2020.ui.MyProgressCameraActivity
import com.vision2020.utils.*
import com.vision2020.utils.AppConstant.CAMERA_CODE
import com.vision2020.utils.AppConstant.CODE_SUCCESS
import com.vision2020.utils.AppConstant.VALUE_STUDENT
import com.vision2020.utils.AppConstant.VALUE_TEACHER
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.imgViewCamera
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.*

class SignUpActivity : BaseActivity<UserViewModel>(), ImageResponse {
    var filePath: String? = ""
    var isStudent: Boolean = true
    var userType: String = ""
    override val layoutId: Int
        get() = R.layout.activity_sign_up
    override val viewModel: UserViewModel
        get() = ViewModelProvider(this).get(UserViewModel::class.java)
    override val context: Context
        get() = this@SignUpActivity

    override fun onCreate() {
        progress = context.progressDialog(getString(R.string.login))
        filePath = " "
        if (rbStudent.isChecked) {
            userType = VALUE_STUDENT
            isStudent = true
            // layoutForTeacher.visibility =View.GONE
            // layoutStudent.visibility = View.VISIBLE
        }
        context.spanText(tvLogin, getString(R.string.already_have_account_login_here), 21, 26) {
            if (it) {
                navigateToLogin(this@SignUpActivity, LoginActivity::class.java)
                finish()
            }
        }
        mViewModel!!.getUser().observe(this, Observer {
            progress!!.dismiss()
            if (it != null) {
                if (it.status_code == CODE_SUCCESS && it.data != null) {
                    showToastMsg("Register Successfully.", 1)
                    navigateToLogin(this@SignUpActivity, LoginActivity::class.java)
                    finish()
                } else {
                    responseHandler(it.status_code, it.message)
                }
            } else {
                context.showToastMsg(getString(R.string.server_error), 2)
            }
        })

    }

    override fun initListeners() {
        imgViewCamera.setOnClickListener {
            /* val intent = Intent(context,ImageSelectActivity::class.java)
             intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true)
             intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true)
             startActivityForResult(intent, 1213)*/

            if (ContextCompat.checkSelfPermission(
                    this@SignUpActivity,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this@SignUpActivity,
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this@SignUpActivity,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openFileExplorer()
            } else {
                ActivityCompat.requestPermissions(
                    this@SignUpActivity, arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), 123
                )
            }

        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbStudent -> {
                    userType = VALUE_STUDENT
                    isStudent = true
                    //  layoutStudent.visibility = View.VISIBLE
                    // layoutForTeacher.visibility = View.GONE
                    tv_id.setText(R.string.student_id)
                    editTextStudentId.setHint(R.string.student_id)
                    imgViewProfile.visibility = View.VISIBLE
                    imgViewCamera.visibility = View.VISIBLE
                }
                R.id.rbTeacher -> {
                    userType = VALUE_TEACHER
                    isStudent = false
                    // layoutStudent.visibility = View.GONE
                    // layoutForTeacher.visibility = View.VISIBLE
                    tv_id.setText(R.string.teacher_id)
                    editTextStudentId.setHint(R.string.teacher_id)
                    imgViewProfile.visibility = View.INVISIBLE
                    imgViewCamera.visibility = View.INVISIBLE
                }
            }
        }

        btnRegister.setOnClickListener {
            when {
                editTextFirstName.text!!.trim().isEmpty() -> {
                    showToastMsg(getString(R.string.error_first_name), 2)
                }
                editTextLastName.text!!.trim().isEmpty() -> {
                    showToastMsg(getString(R.string.error_last_name), 2)
                }
                editTextStudentId.text!!.trim().isEmpty() -> {
                    showToastMsg(getString(R.string.error_student_id), 2)
                }
                editTextClassId.text!!.trim().isEmpty() -> {
                    showToastMsg(getString(R.string.error_class_id), 2)
                }
                editTextSchoolId.text!!.trim().isEmpty() -> {
                    showToastMsg(getString(R.string.error_school_id), 2)
                }
                editTextPhone.text!!.trim().isEmpty() -> {
                    showToastMsg(getString(R.string.error_phone), 2)
                }
                editTextEmailAddress.text!!.trim().isEmpty() -> {
                    showToastMsg(getString(R.string.error_empty_email), 2)
                }
                !editTextEmailAddress.validEmail() -> {
                    showToastMsg(getString(R.string.error_valid_email), 2)
                }
                editTextPassword.text!!.trim().isEmpty() -> {
                    showToastMsg(getString(R.string.error_empty_password), 2)
                }
                editTextPassword.text!!.length < 8 -> {
                    showToastMsg(getString(R.string.error_less_password), 2)
                }
                editTextRetypePassword.text!!.trim().isEmpty() -> {
                    showToastMsg(getString(R.string.error_retype_pass), 2)
                }
                editTextRetypePassword.text.toString() != editTextPassword.text.toString() -> {
                    showToastMsg(getString(R.string.error_pass_not_match), 2)
                }
                editTextAddress.text!!.trim().isEmpty() -> {
                    showToastMsg(getString(R.string.error_address), 2)
                }
                else -> {
                    if (isAppConnected()) {
                        progress!!.show()
                        val signUp = SignUpReq(
                            userType,
                            editTextFirstName.text.toString(),
                            editTextLastName.text.toString(),
                            editTextSchoolId.text.toString(),
                            editTextStudentId.text.toString(),
                            editTextClassId.text.toString(),
                            editTextPhone.text.toString(),
                            editTextEmailAddress.text.toString(),
                            editTextPassword.text.toString(),
                            editTextAddress.text.toString()
                        )
                        mViewModel!!.signUpUser(filePath!!, signUp, isStudent)
                    }
                }
            }
        }
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
        if (requestCode == GALLERY_PICTURE) {
            if (resultCode != 0) {
                filePath = ""
                try {
                    val inputStream = contentResolver.openInputStream(
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
                        //val mImageUri: Uri = result.getUri()
                        bitmap = MediaStore.Images.Media.getBitmap(
                           getContentResolver(),
                            mImageUri
                        )
                        imgViewProfile.setImageBitmap(bitmap)
                        val mFile = File(mImageUri.path)
                        filePath = mFile.absolutePath
                        //uploadPic()
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        } else if (requestCode == CAMERA_REQUEST) {
            if (resultCode != 0) {
                filePath = ""
                val bmOptions = BitmapFactory.Options()
                bmOptions.inSampleSize = 1
                val selectedImagePath = data!!.getStringExtra("path")
                mTempFile = File(selectedImagePath)
                val mImageUri = Uri.fromFile(mTempFile)
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(
                       getContentResolver(),
                        mImageUri
                    )
                    imgViewProfile.setImageBitmap(bitmap)
                    val mFile = File(mImageUri.path)
                    filePath = mFile.absolutePath
                    //uploadPic()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun openFileExplorer() {
        if (!hasPermissions(
                this@SignUpActivity!!,
                *PERMISSIONS
            )
        ) {
            requestPermissions(this@SignUpActivity!!, PERMISSIONS, PERMISSION_ALL)
        } else ImageCapture(this@SignUpActivity!!, this@SignUpActivity, false)
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
                ImageCapture(this@SignUpActivity!!, this@SignUpActivity, false)
            } else {
                Toast.makeText(
                    this@SignUpActivity,
                    "Oops you just denied the permission",
                    Toast.LENGTH_LONG
                )
                    .show();
            }
        }

    }

    fun askPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this@SignUpActivity!!,
                permissionsRequired.get(0)
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this@SignUpActivity!!,
                permissionsRequired.get(1)
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this@SignUpActivity!!,
                permissionsRequired.get(2)
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@SignUpActivity!!,
                    permissionsRequired.get(0)
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    this@SignUpActivity!!,
                    permissionsRequired.get(1)
                )
                || ActivityCompat.shouldShowRequestPermissionRationale(
                    this@SignUpActivity!!,
                    permissionsRequired.get(2)
                )
            ) {
                val builder = AlertDialog.Builder(this@SignUpActivity!!)
                builder.setTitle("Need Multiple Permissions")
                builder.setMessage("This app needs Camera and storage permissions.")
                builder.setPositiveButton(
                    "Grant"
                ) { dialog, which ->
                    dialog.cancel()
                    requestPermissions(
                        this@SignUpActivity!!,
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
                val builder = AlertDialog.Builder(this@SignUpActivity!!)
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
                        this@SignUpActivity!!.getPackageName(),
                        null
                    )
                    intent.data = uri
                    startActivityForResult(
                        intent, REQUEST_PERMISSION_SETTING
                    )
                    Toast.makeText(
                        this@SignUpActivity!!,
                        "Go to Permissions to Grant Camera and storage",
                        Toast.LENGTH_LONG
                    )
                }
                builder.setNegativeButton(
                    "Cancel"
                ) { dialog, which -> dialog.cancel() }
                builder.show()
            } else {
                requestPermissions(
                    this@SignUpActivity!!,
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
        val intent = Intent(this@SignUpActivity!!, MyProgressCameraActivity::class.java)
        intent.putExtra("whichScreen", "MyProfile")
        startActivityForResult(
            intent,
            CAMERA_REQUEST
        )
    }

    var bitmap: Bitmap? = null

    fun openGallery() {
//        val pictureActionIntent = Intent(Intent.ACTION_PICK)
//        pictureActionIntent.type = "image/*"
//        startActivityForResult(
//            pictureActionIntent,
//            GALLERY_PICTURE
//        )

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


    }


}
