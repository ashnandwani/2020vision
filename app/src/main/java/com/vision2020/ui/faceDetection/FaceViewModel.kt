package com.vision2020.ui.faceDetection

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.vision2020.data.response.DrugList
import com.vision2020.repository.faceDetection.FaceDetectionRepository
import com.vision2020.ui.BaseViewModel

class FaceViewModel(application: Application): BaseViewModel(application = application) {
    private val mRepository:FaceDetectionRepository = FaceDetectionRepository()
    private var mLiveData : MutableLiveData<DrugList>?=null

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