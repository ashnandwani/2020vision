package com.vision2020.ui.distress


import android.app.Application
import androidx.lifecycle.MutableLiveData

import com.vision2020.data.response.DistressExpResponse

import com.vision2020.ui.BaseViewModel

class DistressViewModel(application: Application): BaseViewModel(application = application) {


    private var mProfileLiveData:MutableLiveData<DistressExpResponse>?=null
/*
    private  val mGroupRepository : GroupRepository = GroupRepository()
    private var mGroupLiveData : MutableLiveData<GroupListing>? = null


    fun getGroups():MutableLiveData<GroupListing>{
        if(mGroupLiveData==null){
            mGroupLiveData = MutableLiveData()
        }
        mGroupRepository.getGroupList {
            mGroupLiveData!!.value =it
        }
        return mGroupLiveData as MutableLiveData<GroupListing>
    }
*/

}