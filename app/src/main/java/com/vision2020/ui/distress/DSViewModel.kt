package com.vision2020.ui.distress

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.vision2020.data.response.DrugList
import com.vision2020.data.response.FetchByDateListing
import com.vision2020.repository.ds.DSRepository
import com.vision2020.repository.faceDetection.FaceDetectionRepository
import com.vision2020.ui.BaseViewModel

class DSViewModel(application: Application) : BaseViewModel(application = application) {
    private val verifyDSDataList: DSRepository = DSRepository()
    private val mRepository: FaceDetectionRepository = FaceDetectionRepository()
    private var mutableLiveData: MutableLiveData<FetchByDateListing>? = null
    private var mLiveData: MutableLiveData<DrugList>? = null
    fun checkData(
        startDate: String,
        expDate: String,
        activity: FragmentActivity?
    ): MutableLiveData<FetchByDateListing> {
        if (mutableLiveData == null) {
            mutableLiveData = MutableLiveData()
        }

        verifyDSDataList.verifyDSDataList(startDate, expDate,activity) {
            mutableLiveData!!.value = it
        }

        return mutableLiveData as MutableLiveData<FetchByDateListing>
    }

    fun drugList():MutableLiveData<DrugList>{
        if(mLiveData==null){
            mLiveData = MutableLiveData()
        }
        mRepository.getDrugList(){
            mLiveData!!.value = it
        }
        return mLiveData as MutableLiveData<DrugList>
    }

}