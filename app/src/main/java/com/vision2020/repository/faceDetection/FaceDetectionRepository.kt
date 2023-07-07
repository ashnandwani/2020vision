package com.vision2020.repository.faceDetection

import com.vision2020.data.response.DrugList
import com.vision2020.network.RetrofitClient
import com.vision2020.utils.handleJson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FaceDetectionRepository {
    fun getDrugList(returnValue: (DrugList) -> Unit){

        RetrofitClient.instance!!.getDrugList().enqueue(object :
            Callback<DrugList> {
            override fun onFailure(call: Call<DrugList>, t: Throwable) {
                returnValue(DrugList(t.message!!))
            }
            override fun onResponse(call: Call<DrugList>, response: Response<DrugList>) {
                when{
                    response.body() != null ->{
                        returnValue(response.body()!!)
                    }
                    response.errorBody() != null -> {
                        val (statusCode, message) = handleJson(response.errorBody()!!.string())
                        returnValue(DrugList(statusCode.toInt(), message))
                    }
                    else -> returnValue(DrugList(response.code(), response.message()))
                }
            }
        })
    }




}