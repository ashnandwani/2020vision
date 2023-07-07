package com.vision2020.ui.distress

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.vision2020.data.response.DistressExpResponse

import com.vision2020.ui.BaseViewModel

class DistressExperimentSetupViewModel(application: Application): BaseViewModel(application = application) {


    private var mExpSetupData: MutableLiveData<DistressExpResponse>?=null

}