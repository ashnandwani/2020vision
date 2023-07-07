package com.vision2020.repository.group
import BaseModel
import com.vision2020.data.response.GroupListing
import com.vision2020.data.response.StudentListing
import com.vision2020.network.RetrofitClient
import com.vision2020.utils.AppConstant.KEY_TOKEN
import com.vision2020.utils.getAppPref
import com.vision2020.utils.handleJson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class GroupRepository {
    fun getGroupList(returnValue: (GroupListing) -> Unit){

       RetrofitClient.instance!!.getListOfGroup(getAppPref().getString(KEY_TOKEN).toString(),"").enqueue(object : Callback<GroupListing> {
           override fun onFailure(call: Call<GroupListing>, t: Throwable) {
               returnValue(GroupListing(t.message!!))
           }
           override fun onResponse(call: Call<GroupListing>, response: Response<GroupListing>) {
               when{
                   response.body() != null ->{
                       returnValue(response.body()!!)
                   }
                   response.errorBody() != null -> {
                       val (statusCode, message) = handleJson(response.errorBody()!!.string())
                       returnValue(GroupListing(statusCode.toInt(), message))
                   }
                   else -> returnValue(GroupListing(response.code(), response.message()))
               }
           }
       })
    }

    fun getStudentList(returnValue: (StudentListing) -> Unit){
        RetrofitClient.instance!!.getStudents(getAppPref().getString(KEY_TOKEN).toString(),"1").enqueue(object : Callback<StudentListing> {
            override fun onFailure(call: Call<StudentListing>, t: Throwable) {
                returnValue(StudentListing(t.message!!))
            }
            override fun onResponse(call: Call<StudentListing>, response: Response<StudentListing>) {
                when{
                    response.body() != null ->{
                        returnValue(response.body()!!)
                    }
                    response.errorBody() != null -> {
                        val (statusCode, message) = handleJson(response.errorBody()!!.string())
                        returnValue(StudentListing(statusCode.toInt(), message))
                    }
                    else -> returnValue(StudentListing(response.code(), response.message()))
                }
            }
        })
    }

    fun createNewGroup(groupName:String,returnValue: (BaseModel) -> Unit){
       RetrofitClient.instance!!.createGroup(getAppPref().getString(KEY_TOKEN).toString(),groupName).enqueue(object :Callback<BaseModel>{
           override fun onFailure(call: Call<BaseModel>, t: Throwable) {
              returnValue(BaseModel(t.message!!))
           }

           override fun onResponse(call: Call<BaseModel>, response: Response<BaseModel>) {
               when{
                   response.body() != null ->{
                       returnValue(response.body()!!)
                   }
                   response.errorBody() != null -> {
                       val (statusCode, message) = handleJson(response.errorBody()!!.string())
                       returnValue(GroupListing(statusCode.toInt(), message))
                   }
                   else -> returnValue(GroupListing(response.code(), response.message()))
               }
           }

       })
    }


    fun deleteGroup(groupId:String,returnValue: (BaseModel) -> Unit){
        RetrofitClient.instance!!.deleteGroup(getAppPref().getString(KEY_TOKEN).toString(),groupId).enqueue(object :Callback<BaseModel>{
            override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                returnValue(BaseModel(t.message!!))
            }

            override fun onResponse(call: Call<BaseModel>, response: Response<BaseModel>) {
                when{
                    response.body() != null ->{
                        returnValue(response.body()!!)
                    }
                    response.errorBody() != null -> {
                        val (statusCode, message) = handleJson(response.errorBody()!!.string())
                        returnValue(GroupListing(statusCode.toInt(), message))
                    }
                    else -> returnValue(GroupListing(response.code(), response.message()))
                }
            }

        })
    }


    
}