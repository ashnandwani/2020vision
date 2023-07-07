package com.vision2020.ui.settings.group
import BaseFragment
import BaseModel
import android.view.View
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.vision2020.R
import com.vision2020.adapter.AddStudentAdapter
import com.vision2020.data.response.StudentListing
import com.vision2020.utils.*
import com.vision2020.utils.AppConstant.CODE_SUCCESS
import com.vision2020.utils.AppConstant.ERROR
import com.vision2020.utils.AppConstant.SUCCESS
import kotlinx.android.synthetic.main.fragment_add_student.*
import kotlinx.android.synthetic.main.fragment_add_student.checkBoxAll
import kotlinx.android.synthetic.main.fragment_group_members.*
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class AddStudentFragment : BaseFragment<AddGroupViewModel>(),AddStudentAdapter.OnItemClick {
    val arg : AddStudentFragmentArgs by navArgs()
    private var leaderId:String =""
    private var list = arrayListOf<StudentListing.Data>()
    private var selectedList = arrayListOf<String>()
    private var isCheckAll:Boolean = false
    override val layoutId: Int
        get() = R.layout.fragment_add_student
    override val viewModel: AddGroupViewModel
        get() = ViewModelProvider(this).get(AddGroupViewModel::class.java)
    val args: AddStudentFragmentArgs by navArgs()
    override fun onCreateStuff() {
        progress = mContext.progressDialog(getString(R.string.request))
        if(mContext.isAppConnected()){
            progress!!.show()
            mViewModel!!.getStudent().observe(viewLifecycleOwner, get_stu)
        }
    }

    private fun setAdapter() {
       rvAddStuToGroup.layoutManager = LinearLayoutManager(mContext)
       rvAddStuToGroup.adapter = AddStudentAdapter(mContext,list,isCheckAll,this)
    }
    var sortClicked = 0
    override fun initListeners() {
        txtStdMembers.setText("Student List for "+args.name)
        sort.setOnClickListener {
            if (sortClicked == 0) {
                Collections.sort(list,
                        Comparator { lhs, rhs -> lhs.first_name.compareTo(rhs.first_name) })
                Collections.reverse(list)
                setAdapter()
                sortClicked = 1
            } else {
                Collections.sort(list,
                        Comparator { lhs, rhs -> lhs.first_name.compareTo(rhs.first_name) })
                setAdapter()
                sortClicked = 0
            }
        }
        checkBoxAll.setOnClickListener {
          if(checkBoxAll.isChecked){
              isCheckAll = true
              setAdapter()
              selectedList.clear()
              for(i in list){
                  selectedList.add(i.id.toString())
              }
          }else{
              isCheckAll = false
              setAdapter()
              selectedList.clear()
          }
        }
        btnAddStu.setOnClickListener {
            when {
                selectedList.isEmpty() -> {
                    mContext.showToastMsg(getString(R.string.error_select_student), ERROR)
                }
                selectedList.size > 1 &&  leaderId.trim().isEmpty() -> {
                    mContext.showToastMsg(getString(R.string.error_group_leader), ERROR)
                }
                selectedList.size==1 && leaderId.trim().isEmpty()->{
                 leaderId = selectedList.joinToString()
                 addMemberToGroup(selectedList,leaderId)
                 leaderId =""
                }
                else -> {
                    addMemberToGroup(selectedList,leaderId)
                    leaderId=""
                }
            }

        }
    }
    override fun getItemCheck(pos: String) {
         selectedList.add(pos)
    }
    override fun getItemUnCheck(student: String) {
           selectedList.remove(student)
    }
    override fun addMember(id:String) {
        val list = arrayListOf<String>()
        list.add(id)
        addMemberToGroup(list,id)
    }

    override fun onItemSelected(id: String) {
      leaderId = id
    }

    private fun addMemberToGroup(id: ArrayList<String>,leaderId:String) {
        if(mContext.isAppConnected()) {
            progress!!.show()
            mViewModel!!.addToGroup(arg.group, leaderId, id).observe(viewLifecycleOwner, add_mem)
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////
    var get_stu = object : Observer<StudentListing?> {
        override fun onChanged(@Nullable it: StudentListing?) {
            progress!!.hide()
            if(it!!.status_code == CODE_SUCCESS && it!!.data!=null){
                list = it.data!!
                setAdapter()
                stu_textView.visibility = View.GONE
                rvAddStuToGroup.visibility = View.VISIBLE
            }else if (it!!.status_code == AppConstant.ERROR) {
                activity?.logoutUser(activity!!, it!!.message)
            }else{
                stu_textView.visibility = View.VISIBLE
                rvAddStuToGroup.visibility = View.GONE
                stu_textView.setText(it.message)
               // mContext.responseHandler(it.status_code,it.message)
            }
        }
    }

    var add_mem = object : Observer<BaseModel?> {
        override fun onChanged(@Nullable it: BaseModel?) {
            progress!!.hide()
            selectedList.clear()
            if (it!!.status_code == CODE_SUCCESS) {
                mContext.showToastMsg(getString(R.string.student_added), SUCCESS)
                when {
                    isCheckAll -> {
                        requireActivity().onBackPressed()
                    }
                    else -> {
                        if(mContext.isAppConnected()){
                            progress!!.show()
                            mViewModel!!.getStudent().observe(viewLifecycleOwner, get_stu)
                        }
                    }
                }
            } else {
                mContext.responseHandler(it.status_code, getString(R.string.server_error))
            }
        }
    }

}
