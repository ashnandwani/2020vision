package com.vision2020.ui.user

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.vision2020.data.request.LoginReq
import com.vision2020.data.request.SignUpReq
import com.vision2020.data.response.User
import com.vision2020.repository.user.UserRepository
import com.vision2020.ui.BaseViewModel

class UserViewModel(application: Application):BaseViewModel(application = application) {
    private  val mUserRepository : UserRepository = UserRepository()
    private var mUserLiveData : MutableLiveData<User>? = null


    fun loginUser(login: LoginReq) {
        mUserRepository.login(login){
           mUserLiveData!!.value = it
        }
    }

    fun signUpUser(filePath: String, signUp: SignUpReq, isStudent: Boolean){
       mUserRepository.signUp(filePath,signUp,isStudent){
        mUserLiveData!!.value =it
       }

    }

    fun forgetReq(email:String){
        mUserRepository.forgot(email){
            mUserLiveData!!.value =it
        }
    }

    fun getUser():MutableLiveData<User>{
        if(mUserLiveData==null){
            mUserLiveData = MutableLiveData()
        }
        return mUserLiveData as MutableLiveData<User>
    }

}