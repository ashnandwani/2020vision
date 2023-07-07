package com.vision2020.ui.settings.profile



import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.vision2020.data.response.ProfileResponse

import com.vision2020.ui.BaseViewModel

class ProfileViewModel(application: Application): BaseViewModel(application = application) {


    private var mProfileLiveData:MutableLiveData<ProfileResponse>?=null
    //  private var data=MutableLiveData<ArrayList<Student>>()



    /*  fun viewFullProfile():MutableLiveData<Profile>{
          if(mProfileLiveData==null){
              mProfileLiveData = MutableLiveData()
          }
          mProfileRepository.viewProfile {
              mProfileLiveData!!.value =it
          }
          return mProfileLiveData as MutableLiveData<Profile>
      }
  */

    /* fun viewFullProfile():MutableLiveData<Profile>{
         if(mProfileLiveData==null){
             mProfileLiveData = MutableLiveData()
         }
         mProfileRepository.viewProfile {
             mProfileLiveData!!.value =it
         }
         return mProfileLiveData as MutableLiveData<Profile>
     }
 */


}