package com.vision2020.ui.settings.group
import BaseModel
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.vision2020.data.response.GroupMembers
import com.vision2020.data.response.StudentListing
import com.vision2020.repository.group.AddGroupRepository
import com.vision2020.repository.group.GroupRepository
import com.vision2020.ui.BaseViewModel
class AddGroupViewModel(application: Application): BaseViewModel(application = application) {
    private val addGroupRepository:AddGroupRepository = AddGroupRepository()
    private val groupRepository:GroupRepository = GroupRepository()
    private var addGroupLiveData: MutableLiveData<GroupMembers>?=null
    private var liveData:MutableLiveData<BaseModel>?=null
    private var mLiveData:MutableLiveData<StudentListing>?=null

     fun groupMembersReq(id:String){
         addGroupRepository.getGroupMembers(id){
             addGroupLiveData!!.value = it
         }
     }

    fun getGroupMembers():MutableLiveData<GroupMembers>{
        if(addGroupLiveData==null){
            addGroupLiveData = MutableLiveData()
        }
        return addGroupLiveData as MutableLiveData<GroupMembers>
    }

    fun addToGroup(groupId: String, leaderId: String, userId: ArrayList<String>):MutableLiveData<BaseModel> {
        addGroupRepository.addLeader(groupId,userId,leaderId){
            liveData!!.value =it
        }
        if(liveData==null){
         liveData = MutableLiveData()
        }
        return liveData as MutableLiveData<BaseModel>
    }

    // get student

    fun getStudent():MutableLiveData<StudentListing>{
        if(mLiveData==null){
            mLiveData = MutableLiveData()
        }
        groupRepository.getStudentList {
            mLiveData!!.value =it
        }
        return mLiveData as MutableLiveData<StudentListing>
    }

    fun deleteMembers(groupId:String,memberId:String):MutableLiveData<BaseModel>{
       addGroupRepository.deleteMembers(groupId,memberId){
           liveData!!.value = it
       }
        if(liveData==null){
            liveData = MutableLiveData()
        }

        return liveData as MutableLiveData<BaseModel>
    }

}