package com.vision2020.ui.user
import BaseActivity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vision2020.R
import com.vision2020.data.request.LoginReq
import com.vision2020.ui.MainActivity
import com.vision2020.utils.*
import com.vision2020.utils.AppConstant.CODE_SUCCESS
import com.vision2020.utils.AppConstant.KEY_EMAIL
import com.vision2020.utils.AppConstant.KEY_ID
import com.vision2020.utils.AppConstant.KEY_PASSWORD
import com.vision2020.utils.AppConstant.KEY_REMEMBER_ME
import com.vision2020.utils.AppConstant.KEY_TOKEN
import com.vision2020.utils.AppConstant.KEY_USER_TYPE
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.btnLogin

class LoginActivity : BaseActivity<UserViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_login
    override val viewModel: UserViewModel
        get() = ViewModelProvider(this).get(UserViewModel::class.java)
    override val context: Context
        get() = this@LoginActivity

    override fun onCreate() {
        progress = context.progressDialog(getString(R.string.login))
        if( getAppPref().getBoolean(AppConstant.KEY_LOOGED,false)==true){
            val intent = Intent(mContext,MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }else {
            if (getAppPref().getBoolean(KEY_REMEMBER_ME, false)) {
                edtTxtEmail.setText(getAppPref().getString(KEY_EMAIL))
                edtTxtPassword.setText(getAppPref().getString(KEY_PASSWORD))
                checkboxRemember.isChecked = true
            }

            context.spanText(
                tvSignUp,
                getString(R.string.don_t_have_an_account_sign_up_here),
                22,
                30
            ) {
                if (it) {
                    navigateToSignUp(this@LoginActivity, SignUpActivity::class.java)
                }
            }

            mViewModel!!.getUser().observe(this, Observer {
                progress!!.dismiss()
                if (it != null) {
                    if (it.status_code == CODE_SUCCESS && it.data != null) {
                        context.showToastMsg(getString(R.string.login_sucess), 1)
                        getAppPref().setString(KEY_TOKEN, it.data!!.user_access_token_id)
                        getAppPref().setString(KEY_ID, it.data!!.id.toString())
                        getAppPref().setString(KEY_USER_TYPE, it.data!!.user_type.toString())
                        getAppPref().setBoolean(AppConstant.KEY_LOOGED, true)
                        navigateToHome(this@LoginActivity, MainActivity::class.java)
                        finishAffinity()
                    } else {
                        responseHandler(it.status_code, it.message)
                    }
                } else {
                    context.showToastMsg(getString(R.string.server_error), 2)
                }
            })
       }
    }

    private fun holdAppData() {
         if(checkboxRemember.isChecked){
               getAppPref().setBoolean(KEY_REMEMBER_ME,true)
               getAppPref().setString(KEY_EMAIL,edtTxtEmail.text.toString())
               getAppPref().setString(KEY_PASSWORD,edtTxtPassword.text.toString())
            }else{
                getAppPref().setBoolean(KEY_REMEMBER_ME,false)
                getAppPref().setString(KEY_EMAIL,"")
                getAppPref().setString(KEY_PASSWORD,"")
            }
    }

    override fun initListeners() {
        btnLogin.setOnClickListener {
            when{
                edtTxtEmail.text!!.trim().isEmpty()->{
                    showToastMsg(getString(R.string.error_empty_email),2)
                }
                !edtTxtEmail.validEmail()->{
                    showToastMsg(getString(R.string.error_valid_email),2)
                }
                edtTxtPassword.text!!.trim().isEmpty()->{
                    showToastMsg(getString(R.string.error_empty_password),2)
                }
                else -> {
                    if(context.isAppConnected()){
                        progress!!.show()
                        val login = LoginReq(edtTxtEmail.text.toString(),edtTxtPassword.text.toString(),"Android","")
                        mViewModel!!.loginUser(login)
                        holdAppData()
                    }
                }
            }
        }
        tvForgotPassword.setOnClickListener{
            //throw RuntimeException("Test Crash")
              val intent = Intent(this@LoginActivity,ForgotPasswordActivity::class.java)
              startActivity(intent)
        }
    }
}
