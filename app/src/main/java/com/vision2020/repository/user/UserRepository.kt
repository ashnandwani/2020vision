package com.vision2020.repository.user
import android.text.TextUtils
import com.vision2020.data.request.LoginReq
import com.vision2020.data.request.SignUpReq
import com.vision2020.data.response.User
import com.vision2020.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.vision2020.utils.handleJson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UserRepository {
    fun login(loginModel : LoginReq,returnValue: (User) -> Unit){
        RetrofitClient.instance!!.userLogin(loginModel).enqueue(object  : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                returnValue(User(t.message!!))
            }
            override fun onResponse(call: Call<User>, response: Response<User>) {
                when{
                    response.body() != null ->{
                        returnValue(response.body()!!)
                    }
                    response.errorBody() != null -> {
                        val (statusCode, message) = handleJson(response.errorBody()!!.string())
                        returnValue(User(statusCode.toInt(), message))
                    }
                    else -> returnValue(User(response.code(), response.message()))
                }
            }

        })
    }

    fun forgot(email : String,returnValue: (User) -> Unit){
        RetrofitClient.instance!!.userForgot(email).enqueue(object  : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                returnValue(User(t.message!!))
            }
            override fun onResponse(call: Call<User>, response: Response<User>) {
                when{
                    response.body() != null ->{
                        returnValue(response.body()!!)
                    }
                    response.errorBody() != null -> {
                        val (statusCode, message) = handleJson(response.errorBody()!!.string())
                        returnValue(User(statusCode.toInt(), message))
                    }
                    else -> returnValue(User(response.code(), response.message()))
                }
            }

        })
    }


    fun signUp(filePath:String, signUp:SignUpReq,isStudent:Boolean, returnValue: (User) -> Unit){
        RetrofitClient.instance!!.userSignUp(createBuilder(filePath,signUp,isStudent)).enqueue(object  : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                returnValue(User(t.message!!))
            }
            override fun onResponse(call: Call<User>, response: Response<User>) {
                when{
                    response.body() != null ->{
                        returnValue(response.body()!!)
                    }
                    response.errorBody() != null -> {
                        val (statusCode, message) = handleJson(response.errorBody()!!.string())
                        returnValue(User(statusCode.toInt(), message))
                    }
                    else -> returnValue(User(response.code(), response.message()))
                }
            }

        })

    }

    private fun createBuilder(filePath: String, signUp: SignUpReq,isStudentSelected: Boolean
    ): RequestBody {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("user_type",signUp.user_type)
        builder.addFormDataPart("email",signUp.email)
        builder.addFormDataPart("password",signUp.password)
        builder.addFormDataPart("first_name",signUp.first_name)
        builder.addFormDataPart("last_name",signUp.last_name)
        builder.addFormDataPart("school_id",signUp.school_id)
        builder.addFormDataPart("class_id",signUp.class_id)
        builder.addFormDataPart("phone",signUp.phone)
        builder.addFormDataPart("student_id",signUp.student_id)
        builder.addFormDataPart("address",signUp.address)
        // Images
        if(!TextUtils.isEmpty(filePath)){
            val file = File(filePath)
            if (file.exists()) {
                if(isStudentSelected){
                    builder.addFormDataPart("photo",file.name, file.asRequestBody("image/jpg".toMediaTypeOrNull()) )
                }else{
                    builder.addFormDataPart("photo","", file.asRequestBody("image/jpg".toMediaTypeOrNull()) )
                }

            }
        }
        return builder.build()
    }

}