package com.vision2020.ui.settings

import BaseFragment
import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.provider.BaseColumns
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.vision2020.R
import com.vision2020.data.response.GroupList
import com.vision2020.network.RetrofitClient
import com.vision2020.ui.BaseViewModel
import com.vision2020.ui.user.LoginActivity
import com.vision2020.utils.AppConstant
import com.vision2020.utils.getAppPref
import com.vision2020.utils.responseHandler
import kotlinx.android.synthetic.main.fragment_settings.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class SettingsFragment : BaseFragment<BaseViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_settings
    override val viewModel: BaseViewModel
        get() = ViewModelProvider(this).get(BaseViewModel::class.java)

    override fun onCreateStuff() {
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initListeners() {
        if (getAppPref().getString(AppConstant.KEY_USER_TYPE) == "2") {
            teacher.visibility = View.VISIBLE
            stu.visibility=View.GONE
        } else {
            teacher.visibility = View.GONE
            stu.visibility=View.VISIBLE
        }

        btnGroup.setOnClickListener {
            // if(findNavController().getCurrentDestination().getAction(R.id.action_navigation_setting_to_createGroupFragment) != null) {

            if (getAppPref().getString(AppConstant.KEY_USER_TYPE) == "2") {
                findNavController().navigate(R.id.action_navigation_setting_to_createGroupFragment)
            } else {
                getData()
            }
            //}
        }

        btnProfile.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_setting_to_profileFragment)
        }
        gallery.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_setting_to_galleryFragment)
        }

        /*btnLockUnlock.setOnClickListener {
            //  findNavController().navigate(R.id.action_navigation_setting_to_tabletLock)
        }*/

        btnSem.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_setting_to_semester)
        }
        btnGallery.setOnClickListener {
            val builder =
                AlertDialog.Builder(requireContext())
            val dialog: AlertDialog = builder.create()
            val inflater = layoutInflater
            val dialogView1: View =
                inflater.inflate(R.layout.fragment_dialog_validate, null)

            val button2 = dialogView1.findViewById(R.id.btnSubmit) as Button
            val btnCancel = dialogView1.findViewById(R.id.btnCancel) as Button
            val txt = dialogView1.findViewById(R.id.txt) as TextView

            txt.setText("By clicking confirm your previous saved images from gallery will be deleted.")

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }


            button2.setOnClickListener {
                dialog.dismiss()
                deleteFileFromMediaStore()
            }
            dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            // dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            dialog.setView(dialogView1)
            dialog.show()
        }

        btnLogout.setOnClickListener {
            getAppPref().setBoolean(AppConstant.KEY_LOOGED, false)
            val intent = Intent(mContext, LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finishAffinity()
        }

        btnLogoutOne.setOnClickListener {
            getAppPref().setBoolean(AppConstant.KEY_LOOGED, false)
            val intent = Intent(mContext, LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finishAffinity()
        }


    }

    fun deleteFileFromMediaStore() {
        val mediaStoreIds: MutableList<Long> = ArrayList()
        val c: Cursor? = requireActivity()!!.getContentResolver().query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(BaseColumns._ID),
            null,
            null,
            null
        )
        if (c != null) {
            var id = c.getColumnIndexOrThrow(BaseColumns._ID)
            c.moveToFirst()
            while (!c.isAfterLast()) {
                val mediaStoreId: Long = c.getLong(id)
                mediaStoreIds.add(mediaStoreId)
                c.moveToNext()
            }
            c.close()
        }
        for(i in 0 until mediaStoreIds.size) {
            requireActivity()!!.getContentResolver().delete(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                BaseColumns._ID + "=?",
                arrayOf(mediaStoreIds.get(i).toString())
            )
        }
        Toast.makeText(activity,getString(R.string.gallery_del), Toast.LENGTH_LONG).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getData() {
        var token = getAppPref()!!.getString(AppConstant.KEY_TOKEN)
        if (token != null) {
            RetrofitClient.instance!!.getAllGroupList(token).enqueue(
                object : Callback<GroupList> {
                    override fun onFailure(call: Call<GroupList>, t: Throwable) {
                        Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
                        activity!!.responseHandler(
                            AppConstant.ERROR,
                            getString(R.string.internet_error)
                        )
                    }

                    var i   = 0
                    override fun onResponse(call: Call<GroupList>, response: Response<GroupList>) {
                        if (response.body()!!.status_code == AppConstant.CODE_SUCCESS) {
                            findNavController().navigate(R.id.action_navigation_setting_to_listGroupFragment)
                        } else {
                            activity!!.responseHandler(
                                response.body()!!.status_code,
                                response.body()!!.message
                            )
                            if (response.body()!!.message.startsWith("No records available")) {
                                activity!!.responseHandler(
                                    AppConstant.ERROR,
                                    "You are not associated with any group."
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     )
                            }
                        }
                    }
                })
        }
    }
}
