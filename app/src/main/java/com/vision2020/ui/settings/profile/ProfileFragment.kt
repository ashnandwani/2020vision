package com.vision2020.ui.teacher.profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.vision2020.AppApplication
import com.vision2020.R
import com.vision2020.data.request.EditProfileReq
import com.vision2020.data.response.ProfileData
import com.vision2020.data.response.ProfileResponse
import com.vision2020.data.response.UpdateProfileResponse
import com.vision2020.database.DatabaseClient
import com.vision2020.database.profile.ProfileModal
import com.vision2020.network.RetrofitClient
import com.vision2020.ui.MyProgressCameraActivity
import com.vision2020.ui.cameralayout.MyCamera
import com.vision2020.utils.*
import kotlinx.android.synthetic.main.activity_sign_up.imgViewCamera
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.net.URL
import java.util.regex.Matcher
import java.util.regex.Pattern


class ProfileFragment : Fragment(), ImageResponse {

    var filePath: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().getWindow()
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    fun insertDataInDB(dataModal: ProfileModal) {
        object : AsyncTask<Void?, Void?, Int?>() {
            @SuppressLint("WrongThread")
            override fun doInBackground(vararg params: Void?): Int? { //adding to database
                Log.d(
                    "doInBackground", "doInBackground: " + dataModal.workoutId.toInt()
                )

                var avaliData =
                    DatabaseClient.getInstance(activity!!.applicationContext).appDatabase
                        .orderDao()
                        .checkData(dataModal.workoutId.toInt())

                if (avaliData == 0) {
                    var avaliData =
                        DatabaseClient.getInstance(activity!!.applicationContext).appDatabase
                            .orderDao()
                            .insert(dataModal)
                } else {
                    var avaliData =
                        DatabaseClient.getInstance(activity!!.applicationContext).appDatabase
                            .orderDao()
                            .update(dataModal.data, dataModal.workoutId.toInt())
                }

                return avaliData
            }

            override fun onPostExecute(agentsCount: Int?) { //usersTextView.setText("Users \n\n " + users);
                Log.d("onPostExecute", "onPostExecute: ")
                return
            }
        }.execute()

    }

    lateinit var tvEmail: TextView
    lateinit var textCNView: TextView
    lateinit var tvFirstName: TextView
    lateinit var tvLastName: TextView
    lateinit var tvPhone: TextView
    lateinit var tvAddress: TextView
    lateinit var tvUserType: TextView
    lateinit var tvUserName: TextView
    lateinit var imageViewProfile: ImageView
    lateinit var btnEditProfile: Button
    lateinit var btnSaveProfile: Button
    lateinit var editFirstName: EditText
    lateinit var editLastName: EditText
    lateinit var editPhone: EditText
    lateinit var editAddress: EditText
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvEmail = view.findViewById(R.id.tvEmail)
        textCNView = view.findViewById(R.id.textCNView)
        tvFirstName = view.findViewById(R.id.tvfirstName)
        tvLastName = view.findViewById(R.id.tvLastName)
        tvPhone = view.findViewById(R.id.tvPhone)
        tvAddress = view.findViewById(R.id.tvAddress)
        tvUserType = view.findViewById(R.id.tvUserType)
        tvUserName = view.findViewById(R.id.tvUserName)
        imageViewProfile = view.findViewById(R.id.imgViewUserPic)
        btnEditProfile = view.findViewById(R.id.btnEditProfile)
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile)

        editFirstName = view.findViewById(R.id.editfirstName)
        editLastName = view.findViewById(R.id.editLastName)
        editPhone = view.findViewById(R.id.editPhone)
        editAddress = view.findViewById(R.id.editAddress)

        lateinit var dataModal: ProfileModal
        val token = getAppPref().getString(AppConstant.KEY_TOKEN)

        if (requireActivity()!!.isAppConnected()) {
            RetrofitClient.instance!!.getUserData(token!!)
                .enqueue(object : Callback<ProfileResponse> {
                    override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                        try {
                            activity!!.showToastMsg(getString(R.string.internet_error), 2)
                        }catch (ex:Exception){

                        }
                    }

                    override fun onResponse(
                        call: Call<ProfileResponse>,
                        response: Response<ProfileResponse>
                    ) {
                        Log.d("Response::::", response.body()!!.status_code.toString())
                        if (response.body()!!.status_code.toInt() == AppConstant.CODE_SUCCESS) {
                            dataModal =
                                ProfileModal()
                            var workoutDetails =
                                ProfileModal.RoomConverter.saveList(response.body()!!.data)
                            dataModal.data = workoutDetails
                            dataModal.workoutId = response.body()!!.data.id
                            //insertDataInDB(dataModal)
                            showData(response.body()!!.data, "add")
                        } else if (response.body()!!.status_code == AppConstant.ERROR) {
                            activity?.logoutUser(activity!!, response.body()!!.message)
                        } else {
                            activity!!.showToastMsg(response.body()!!.message, 1)
                        }
                    }
                })
        } else {
            /*object : AsyncTask<Void?, Void?, ProfileModal?>() {
                @SuppressLint("WrongThread")
                override fun doInBackground(vararg params: Void?): ProfileModal? { //adding to database
                    var profileData =
                        DatabaseClient.getInstance(activity!!.applicationContext).appDatabase
                            .orderDao().profileData
                    return profileData
                }

                override fun onPostExecute(agentsCount: ProfileModal?) {
                    var profileData =
                        ProfileModal.RoomConverter.restoreList(agentsCount!!.data)
                    dataModal = ProfileModal()
                    dataModal.data = agentsCount!!.data
                    dataModal.workoutId = profileData.id
                    showData(profileData, "add")
                    return
                }
            }.execute()*/
        }


        btnEditProfile.setOnClickListener {
            btnSaveProfile.visibility = View.VISIBLE
            btnEditProfile.visibility = View.INVISIBLE
            tvFirstName.visibility = View.INVISIBLE
            editFirstName.visibility = View.VISIBLE
            tvAddress.visibility = View.INVISIBLE
            editAddress.visibility = View.VISIBLE
            tvLastName.visibility = View.INVISIBLE
            editLastName.visibility = View.VISIBLE
            tvPhone.visibility = View.INVISIBLE
            editPhone.visibility = View.VISIBLE
            imgViewCamera.visibility = View.VISIBLE
        }

        btnSaveProfile.setOnClickListener {
            val pattern =
                "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$"
            var m: Matcher
            val r: Pattern = Pattern.compile(pattern)
            m = r.matcher(editPhone.getText().toString().trim())
            val maxLength = 10
            val FilterArray = arrayOfNulls<InputFilter>(1)
            FilterArray[0] = LengthFilter(maxLength)
            editPhone.setFilters(FilterArray)
            when {
                editFirstName.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg(getString(R.string.error_first_name), 2)
                }
                editAddress.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg(getString(R.string.error_address), 2)
                }
                editLastName.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg(getString(R.string.error_last_name), 2)
                }
                editPhone.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg(getString(R.string.error_phone), 2)
                }
                editPhone.getText().toString().length < 10 || editPhone.getText()
                    .toString().length > 10 -> {
                    activity?.showToastMsg(getString(R.string.error_phone_length), 2)
                }
                else -> {
                    if (requireActivity()!!.isAppConnected()) {
                        var body = createBuilder(
                            filePath!!,
                            EditProfileReq(
                                token!!,
                                editFirstName.text.toString(),
                                editLastName.text.toString(),
                                editAddress.text.toString(),
                                editPhone.text.toString()
                            )
                        )
                        RetrofitClient.instance!!.userEditProfile(body)
                            .enqueue(object : Callback<UpdateProfileResponse> {
                                override fun onFailure(
                                    call: Call<UpdateProfileResponse>,
                                    t: Throwable
                                ) {
                                    Toast.makeText(
                                        activity,
                                        t.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                                override fun onResponse(
                                    call: Call<UpdateProfileResponse>,
                                    response: Response<UpdateProfileResponse>
                                ) {
                                    if (response.body()!!.status_code == AppConstant.CODE_SUCCESS) {
                                        try {
                                            showData(response.body()!!.data, "edit")
                                        } catch (ex: Exception) {
                                        }
                                    } else if (response.body()!!.status_code == AppConstant.ERROR) {
                                        activity?.logoutUser(activity!!, response.body()!!.message)
                                    } else {
                                        Toast.makeText(
                                            activity,
                                            response.body()!!.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            })
                    } else {
                        var type: Int = 0
                        if (tvUserType.text.toString().equals("Student")) {
                            type = 1
                        }
                        if (tvUserType.text.toString().equals("Teacher")) {
                            type = 2
                        }
                        var data = ProfileData(
                            dataModal.workoutId,
                            type,
                            tvEmail.text.toString(),
                            editPhone.text.toString(),
                            editFirstName.text.toString(),
                            editLastName.text.toString(),
                            editAddress.text.toString(),
                            filePath!!
                        )
                        var workoutDetails =
                            ProfileModal.RoomConverter.saveList(data)
                        dataModal.data = workoutDetails
                        //insertDataInDB(dataModal)
                        showData(data, "edit")
                    }
                }
            }
        }



        imgViewCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    requireActivity(),
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    requireActivity(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openFileExplorer()
            } else {
                requestPermissions(
                    arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), 123
                )
            }
        }
    }

    private fun showData(data: ProfileData, type: String) {
        if (data.user_type == 1) {
            tvUserType.setText("Student")
        }
        if (data.user_type == 2) {
            tvUserType.setText("Teacher")
        }
        //Toast.makeText(requireActivity()!!,"data:"+data.photo,Toast.LENGTH_LONG).show()
        try {
            DownloadImageTask(imageViewProfile).execute(data.photo)
        } catch (ex: java.lang.Exception) {

        }
        if(data.email!=null) {
            tvEmail.setText(data.email)
        }
        textCNView.setText(data.first_name + " " + data.last_name)
        if(data.email!=null) {
            tvUserName.setText(data.email)
        }
        tvFirstName.setText(data.first_name)
        tvLastName.setText(data.last_name)
        tvAddress.setText(data.address)
        tvPhone.setText(data.phone)
        editFirstName.setText(data.first_name)
        editLastName.setText(data.last_name)
        editAddress.setText(data.address)
        editPhone.setText(data.phone)
        imgViewCamera.visibility = View.GONE
        if (type.equals("edit")) {
            btnSaveProfile.visibility = View.INVISIBLE
            btnEditProfile.visibility = View.VISIBLE

            tvFirstName.visibility = View.VISIBLE
            editFirstName.visibility = View.INVISIBLE

            tvAddress.visibility = View.VISIBLE
            editAddress.visibility = View.INVISIBLE

            tvLastName.visibility = View.VISIBLE
            editLastName.visibility = View.INVISIBLE

            tvPhone.visibility = View.VISIBLE
            editPhone.visibility = View.INVISIBLE
        }
    }

    private fun openFileExplorer() {
        if (!hasPermissions(
                requireActivity()!!,
                *PERMISSIONS
            )
        ) {
            ActivityCompat.requestPermissions(requireActivity()!!, PERMISSIONS, PERMISSION_ALL)
        } else ImageCapture(requireActivity()!!, this@ProfileFragment, false)
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
                ImageCapture(requireActivity()!!, this@ProfileFragment, false)
            } else {
                Toast.makeText(activity, "Oops you just denied the permission", Toast.LENGTH_LONG)
                    .show();
            }
        }

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
                ) { dialog, which -> dialog.cancel() }
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
            openCameraActivity()
        } else {
            openGallery()
        }
    }

    private var mTempFile: File? = null
    private val TEMP_PHOTO_FILE_NAME =
        System.currentTimeMillis().toString()

    private fun openCameraActivity() {
        val intent = Intent(requireActivity()!!, MyCamera::class.java)

        startActivityForResult(
            intent,
            CAMERA_REQUEST
        )
        
        
      /*  val intent = Intent(requireActivity()!!, MyProgressCameraActivity::class.java)
        intent.putExtra("whichScreen", "MyProfile")
        startActivityForResult(
            intent,
            CAMERA_REQUEST
        )*/
    }

    var bitmap: Bitmap? = null

    fun openGallery() {

//        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
//        getIntent.type = "image/*"
//
//
//        val pickIntent = Intent(
//            Intent.ACTION_PICK,
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//        )
//        pickIntent.type = "image/*"
//
//        val chooserIntent = Intent.createChooser(getIntent, "Select Image")
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))
//
//        startActivityForResult(chooserIntent, GALLERY_PICTURE)

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

    private fun createBuilder(filePath: String, editProfile: EditProfileReq): RequestBody {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("tokenId", editProfile.tokenId)
        builder.addFormDataPart("first_name", editProfile.first_name)
        builder.addFormDataPart("last_name", editProfile.last_name)
        builder.addFormDataPart("phone", editProfile.phone)
        builder.addFormDataPart("address", editProfile.address)
        // Images
        if (!TextUtils.isEmpty(filePath)) {
            val file = File(filePath)
            if (file.exists()) {
                builder.addFormDataPart(
                    "photo",
                    file.name,
                    file.asRequestBody("image/jpg".toMediaTypeOrNull())
                )
            }
        }
        return builder.build()
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
                        //val mImageUri: Uri = result.getUri()
                        bitmap = MediaStore.Images.Media.getBitmap(
                            requireActivity()!!.getContentResolver(),
                            mImageUri
                        )
                        imgViewUserPic.setImageBitmap(bitmap)
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
                        requireActivity()!!.getContentResolver(),
                        mImageUri
                    )
                    imgViewUserPic.setImageBitmap(bitmap)
                    val mFile = File(mImageUri.path)
                    filePath = mFile.absolutePath
                    //uploadPic()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        } /*else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result: CropImage.ActivityResult = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                try {
                    //val mImageUri: Uri = result.getUri()
                    bitmap = MediaStore.Images.Media.getBitmap(
                        requireActivity()!!.getContentResolver(),
                        result.getUri()
                    )
                    imgViewUserPic.setImageBitmap(bitmap)
                    val mFile = File(result.getUri().path)
                    filePath = mFile.absolutePath
                    //uploadPic()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            }
        }*/ else {

        }
    }

    var mIcon11: Bitmap? = null

   inner class DownloadImageTask(val bmImage: ImageView) : AsyncTask<String, Void, Bitmap>() {

        override fun onPostExecute(result: Bitmap?) {
            bmImage.setImageBitmap(result)
        }

        override fun doInBackground(vararg p: String?): Bitmap? {
            var urldisplay: String = p[0].toString()
            try {
                var ip = URL(urldisplay).openStream()
                mIcon11 = BitmapFactory.decodeStream(ip)
            } catch (e: Exception) {
                Log.e("Error", e.message!!)
                e.printStackTrace();
            }
            return mIcon11
        }
    }


}
