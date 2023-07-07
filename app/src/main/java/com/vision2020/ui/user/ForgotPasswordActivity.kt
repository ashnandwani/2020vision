package com.vision2020.ui.user
import BaseActivity
import android.content.Context
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vision2020.R
import com.vision2020.utils.*
import com.vision2020.utils.AppConstant.CODE_SUCCESS
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : BaseActivity<UserViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_forgot_password
    override val viewModel: UserViewModel
        get() = ViewModelProvider(this).get(UserViewModel::class.java)
    override val context: Context
        get() = this@ForgotPasswordActivity
    override fun onCreate() {
     progress = context.progressDialog(getString(R.string.request))
     mViewModel!!.getUser().observe(this, Observer {
         progress!!.dismiss()
         if(it!=null){
              if(it.status_code==CODE_SUCCESS && it.data!=null){
                showToastMsg(getString(R.string.password_send),1)
                  finish()
              }else{
                  responseHandler(it.status_code,it.message)
              }
         }else{
             showToastMsg(getString(R.string.server_error),2)
         }
     })
    }

    override fun initListeners() {
        btnSubmit.setOnClickListener {
            when {
                edtTxtEmail.text!!.trim().isEmpty() -> {
                    showToastMsg(getString(R.string.error_empty_email),2)
                }
                !edtTxtEmail.validEmail() -> {
                    showToastMsg(getString(R.string.error_valid_email), 2)
                }
                else -> {
                    if(isAppConnected()){
                        progress!!.show()
                     mViewModel!!.forgetReq(edtTxtEmail.text.toString())
                    }
                }
            }
         }

    }
}
