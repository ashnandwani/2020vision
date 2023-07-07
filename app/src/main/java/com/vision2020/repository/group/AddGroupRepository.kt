package com.vision2020.repository.group

import BaseModel
import com.vision2020.data.response.GroupMembers
import com.vision2020.network.RetrofitClient
import com.vision2020.utils.AppConstant.KEY_TOKEN
import com.vision2020.utils.getAppPref
import com.vision2020.utils.handleJson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddGroupRepository {

    fun getGroupMembers(id:String,returnValue: (GroupMembers) -> Unit){
        val token = getAppPref().getString(KEY_TOKEN)
        RetrofitClient.instance!!.getGroupMembers(token!!,id).enqueue(object :Callback<GroupMembers>{
            override fun onFailure(call: Call<GroupMembers>, t: Throwable) {
                returnValue(GroupMembers(t.message!!))
            }
            override fun onResponse(call: Call<GroupMembers>, response: Response<GroupMembers>) {
                when{
                    response.body() != null ->{
                        returnValue(response.body()!!)
                    }
                    response.errorBody() != null -> {
                        val (statusCode, message) = handleJson(response.errorBody()!!.string())
                        returnValue(GroupMembers(statusCode.toInt(), message))
                    }
                    else -> returnValue(GroupMembers(response.code(), response.message()))
                }
            }

        })
    }

    fun addLeader(groupId:String,userId:ArrayList<String>,leaderId:String,returnValue: (BaseModel) -> Unit){
        val id = userId.joinToString(",")
        val token = getAppPref().getString(KEY_TOKEN)
        RetrofitClient.instance!!.addLeaderToGroup(token!!,groupId,id,leaderId).enqueue(object :Callback<BaseModel>{
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
                        returnValue(GroupMembers(statusCode.toInt(), message))
                    }
                    else -> returnValue(GroupMembers(response.code(), response.message()))
                }
            }

        })
    }

    fun deleteMembers(groupId:String,id:String,returnValue: (BaseModel) -> Unit){
        val token = getAppPref().getString(KEY_TOKEN)
        RetrofitClient.instance!!.deleteMembers(token!!,groupId,id).enqueue(object :Callback<BaseModel>{
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
                        returnValue(GroupMembers(statusCode.toInt(), message))
                    }
                    else -> returnValue(GroupMembers(response.code(), response.message()))
                }
            }

        })
    }


}