package com.vision2020.repository.ds
import androidx.fragment.app.FragmentActivity
import com.vision2020.R
import com.vision2020.data.response.FetchByDateListing
import com.vision2020.network.RetrofitClient
import com.vision2020.utils.AppConstant.KEY_TOKEN
import com.vision2020.utils.getAppPref
import com.vision2020.utils.handleJson
import com.vision2020.utils.showToastMsg
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DSRepository {
    fun verifyDSDataList(startDate:String,expDate:String,
                         activity: FragmentActivity?, returnValue: (FetchByDateListing) -> Unit){

       RetrofitClient.instance!!.verifyDSDataList(getAppPref().getString(KEY_TOKEN).toString(),startDate,expDate).enqueue(object : Callback<FetchByDateListing> {
           override fun onFailure(call: Call<FetchByDateListing>, t: Throwable) {
               returnValue(FetchByDateListing(t.message!!))
               if(!t.message.toString().startsWith("java")) {
                   activity?.showToastMsg(activity.getString(R.string.server_error), 2)
               }

           }
           override fun onResponse(call: Call<FetchByDateListing>, response: Response<FetchByDateListing>) {
               when{
                   response.body() != null ->{
                       returnValue(response.body()!!)
                   }
                   response.errorBody() != null -> {
                       val (statusCode, message) = handleJson(response.errorBody()!!.string())
                       returnValue(FetchByDateListing(statusCode.toInt(), message))
                   }
                   else -> returnValue(FetchByDateListing(response.code(), response.message()))
               }
           }
       })
    }
}