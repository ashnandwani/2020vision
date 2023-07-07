package com.vision2020.ui.settings.group
import BaseModel
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.vision2020.data.Student
import com.vision2020.data.response.GroupListing
import com.vision2020.data.response.StudentListing
import com.vision2020.repository.group.GroupRepository
import com.vision2020.ui.BaseViewModel
class GroupViewModel(application: Application): BaseViewModel(application = application) {
    private  val mGroupRepository : GroupRepository = GroupRepository()
    private var mGroupLiveData : MutableLiveData<GroupListing>? = null
    private var mutableLiveData :MutableLiveData<BaseModel>?=null
    private var mStudentLiveData:MutableLiveData<StudentListing>?=null
    private var data=MutableLiveData<ArrayList<Student>>()

    fun getGroups():MutableLiveData<GroupListing>{
        if(mGroupLiveData==null){
            mGroupLiveData = MutableLiveData()
        }
        mGroupRepository.getGroupList {
            mGroupLiveData!!.value =it
        }
        return mGroupLiveData as MutableLiveData<GroupListing>
    }

    fun getStudent():MutableLiveData<StudentListing>{
        if(mStudentLiveData==null){
            mStudentLiveData = MutableLiveData()
        }
        mGroupRepository.getStudentList {
            mStudentLiveData!!.value =it
        }
        return mStudentLiveData as MutableLiveData<StudentListing>
    }


    fun createGroup(name:String):MutableLiveData<BaseModel>{
        if(mutableLiveData == null){
           mutableLiveData = MutableLiveData()
        }

        mGroupRepository.createNewGroup(name){
            mutableLiveData!!.value = it
        }

        return mutableLiveData as MutableLiveData<BaseModel>
    }


    fun setStudentData(selectedStudentList: ArrayList<Student>) {
        data.value = selectedStudentList
    }

   fun getStudentData():MutableLiveData<ArrayList<Student>> {
       if(data == null){
           data = MutableLiveData()
       }
     return data
   }

  fun deleteGroup(id:String):MutableLiveData<BaseModel>{
      mGroupRepository.deleteGroup(id){
        mutableLiveData!!.value = it
      }
      if(mutableLiveData == null){
         mutableLiveData = MutableLiveData()
      }
      return mutableLiveData as MutableLiveData<BaseModel>
  }
}