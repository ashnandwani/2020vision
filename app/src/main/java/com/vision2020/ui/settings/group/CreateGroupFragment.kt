package com.vision2020.ui.settings.group

import BaseFragment
import BaseModel
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vision2020.R
import com.vision2020.adapter.GroupListAdapter
import com.vision2020.adapter.StudentListAdapter
import com.vision2020.data.Student
import com.vision2020.data.response.GroupListing
import com.vision2020.data.response.StudentListing
import com.vision2020.utils.*
import com.vision2020.utils.AppConstant.CODE_SUCCESS
import com.vision2020.utils.AppConstant.ERROR
import kotlinx.android.synthetic.main.fragment_create_group.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.*
import kotlin.Comparator


class CreateGroupFragment : BaseFragment<GroupViewModel>(), StudentListAdapter.OnItemClick,
    GroupListAdapter.OnItemNavigation {
    private val activityScope = CoroutineScope(Dispatchers.Main)
    private var isSelectAllChecked: Boolean = false
    var groupList = arrayListOf<GroupListing.Data>()
    var studentList = arrayListOf<StudentListing.Data>()
    var selectedStudentList = arrayListOf<Student>()
    override val layoutId: Int
        get() = R.layout.fragment_create_group
    override val viewModel: GroupViewModel
        get() = ViewModelProvider(requireActivity()).get(GroupViewModel::class.java)
    var value = 0
    var sortClicked = 0
    var s_sortClicked = 0

    companion object {
        var save_token = ""
    }

    override fun initListeners() {
        sort.setOnClickListener {
            if (sortClicked == 0) {
                Collections.sort(groupList,
                    Comparator { lhs, rhs -> lhs.group_name.compareTo(rhs.group_name) })
                Collections.reverse(groupList)
                setGroupAdapter()
                sortClicked = 1
            } else {
                Collections.sort(groupList,
                    Comparator { lhs, rhs -> lhs.group_name.compareTo(rhs.group_name) })
                setGroupAdapter()
                sortClicked = 0
            }
        }
        s_sort.setOnClickListener() {
            if (s_sortClicked == 0) {
                Collections.sort(studentList,
                    Comparator { lhs, rhs -> lhs.first_name.compareTo(rhs.first_name) })
                Collections.reverse(studentList)
                setStudentAdapter()
                s_sortClicked = 1
            } else {
                Collections.sort(studentList,
                    Comparator { lhs, rhs -> lhs.first_name.compareTo(rhs.first_name) })
                setStudentAdapter()
                s_sortClicked = 0
            }
        }
        btnCreateGroup.setOnClickListener {
            if (editTextGroupName.text!!.trim().isEmpty()) {
                mContext.showToastMsg(getString(R.string.error_group_name), 2)
            } else {
                if (mContext.isAppConnected()) {
                    progress!!.show()
                    save_token = "create"
                    mViewModel!!.createGroup(editTextGroupName.text.toString())
                        .observe(viewLifecycleOwner, grp_create)
                }
            }
        }

        btnAddToGroup.setOnClickListener {
            findNavController().navigate(R.id.action_createGroupFragment_to_addGroupFragment)
            mViewModel!!.setStudentData(selectedStudentList)
            editTextGroupName.setText("")
        }
        checkBoxAll.setOnClickListener {
            if (checkBoxAll.isChecked) {
                isSelectAllChecked = true
                setStudentAdapter()
                if (studentList.size > 0) {
                    btnAddToGroup.isEnabled = true
                    btnAddToGroup.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite))
                    btnAddToGroup.setBackgroundColor(
                        ContextCompat.getColor(
                            mContext,
                            R.color.colorBlue
                        )
                    )
                    for (i in studentList) {
                        val student = Student(i.first_name, i.id.toString(), isSelectAllChecked)
                        if (!selectedStudentList.contains(student)) {
                            selectedStudentList.add(student)
                        }
                    }
                }
            } else {
                selectedStudentList.clear()
                isSelectAllChecked = false
                setStudentAdapter()
                btnAddToGroup.isEnabled = false
                btnAddToGroup.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack))
                btnAddToGroup.setBackgroundColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.colorFadeGray
                    )
                )
            }
        }
    }

    override fun onDelete(pos: Int, id: String) {
        val view = view
        if (view != null) {
            val dialogBuilder =
                activity?.let { AlertDialog.Builder(it).create() }
            val inflater = layoutInflater
            val dialogView: View =
                inflater.inflate(R.layout.groupdeletepopup, null)
            val btnYes =
                dialogView.findViewById<View>(R.id.btnYes) as Button
            val btnNo =
                dialogView.findViewById<View>(R.id.btnNo) as Button
            btnYes.setOnClickListener {
                if (dialogBuilder != null) {
                    dialogBuilder.dismiss()
                    if (mContext.isAppConnected()) {
                        progress!!.show()
                        save_token = "delete"
                        mViewModel!!.deleteGroup(id).observe(viewLifecycleOwner, grp_delete)
                    }
                }
            }
            btnNo.setOnClickListener {
                dialogBuilder!!.dismiss()
            }
            if (dialogBuilder != null) {
                dialogBuilder.getWindow()
                    ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialogBuilder.setView(dialogView)
                dialogBuilder.show()
                dialogBuilder.setCanceledOnTouchOutside(true)
            }
        }

    }

    override fun onCreateStuff() {
        activity?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )

        progress = mContext.progressDialog(getString(R.string.request))
        if (mContext.isAppConnected()) {
            progress!!.show()
            ui_type = "not"
            save_token = "add"
            mViewModel!!.getGroups().observe(this, staff_create)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )
    }

    private fun getGroups(type: String) {
        save_token = "add"
        mViewModel!!.getGroups().observe(this, staff_create_new)
    }

    //.........................................................................................................

    var staff_create_new = object : Observer<GroupListing?> {
        override fun onChanged(@Nullable it: GroupListing?) {
            addGroupData(it, "", "")
        }
    }

    var staff_create = object : Observer<GroupListing?> {
        override fun onChanged(@Nullable it: GroupListing?) {
            progress!!.dismiss()
            addGroupData(it, "get", "")
        }
    }


    var ui_type = ""
    var grp_delete = object : Observer<BaseModel?> {
        override fun onChanged(@Nullable it: BaseModel?) {
            progress!!.dismiss()
            if (it?.status_code == CODE_SUCCESS) {
                //mContext.showToastMsg("Group Deleted Successfully.", ERROR)
                if (save_token.equals("delete")) {
                    Log.v("Nisha check", "grp delete")
                    /* val toast: Toast = Toast.makeText(mContext, "Group Deleted Successfully.", Toast.LENGTH_SHORT)
                     val view = toast.view
                     view.background.setColorFilter(ContextCompat.getColor(mContext, R.color.colorGreen), PorterDuff.Mode.SRC_IN)
                     val text = view.findViewById<TextView>(message)
                     text.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite))
                     toast.show()*/
                    mContext.showToastMsg("Group Deleted Successfully.", ERROR)
                    ui_type = "not"
                    getGroups("")
                }
            } else {
                //Toast.makeText(mContext,it!!.message,Toast.LENGTH_SHORT).show()
                //  mContext.responseHandler(it!!.status_code, it!!.message)
            }

        }
    }
    var grp_create = object : Observer<BaseModel?> {
        override fun onChanged(@Nullable it: BaseModel?) {
            progress!!.dismiss()
            editTextGroupName.setText("")
            if (it != null) {
                if (it.status_code == CODE_SUCCESS) {
                    if (save_token.equals("create")) {
                        Log.v("Nisha check", "grp create")
                        mContext.showToastMsg(getString(R.string.msg_group_created), 1)
                        ui_type = "std"
                        getGroups("")
                    }
                } else if (it!!.status_code == AppConstant.ERROR) {
                    activity?.logoutUser(activity!!, it!!.message)
                } else {
                    if (save_token.equals("create")) {
                        mContext.responseHandler(it.status_code, it.message)
                    }
                }
            }
        }
    }

    fun addGroupData(it: GroupListing?, type: String, a_type: String) {
        progress!!.dismiss()
        if (it != null) {
            if (it.status_code == CODE_SUCCESS && it.data != null) {
                //  groupList.clear()
                groupList = it.data!!
                if (!groupList.isEmpty())
                    setGroupAdapter()
                grp_text.visibility = View.GONE
                rvGroupList.visibility = View.VISIBLE
            } else if (it!!.status_code == AppConstant.ERROR) {
                activity?.logoutUser(mContext as Activity, it!!.message)
            } else {
                // mContext.responseHandler(it.status_code, it.message)
                grp_text.setText(it.message)
                grp_text.visibility = View.VISIBLE
                rvGroupList.visibility = View.INVISIBLE
            }
        } else {
            mContext.showToastMsg(getString(R.string.server_error), 2)
        }
        if (ui_type.equals("not")) {
            getStudents("")
        }
    }

    private fun getStudents(type: String) {
        mViewModel!!.getStudent().observe(this, Observer {
            getStuData(it, type)
        })
    }

    fun getStuData(it: StudentListing, a_type: String) {
        if (it != null) {
            if (it.status_code == CODE_SUCCESS && it.data != null) {
                studentList = it.data!!
                if (a_type.equals("asc")) {
                    Collections.sort(studentList,
                        Comparator { lhs, rhs -> lhs.first_name.compareTo(rhs.first_name) })
                } else if (a_type.equals("dsc")) {
                    Collections.sort(studentList,
                        Comparator { lhs, rhs -> lhs.first_name.compareTo(rhs.first_name) })
                    Collections.reverse(groupList)
                }
                setStudentAdapter()
                std_textView.setText(it.message)
                std_textView.visibility = View.GONE
                //rvStudentListing.visibility = View.VISIBLE

            } else if (it!!.status_code == AppConstant.ERROR) {
                activity?.logoutUser(mContext as Activity, it!!.message)
            } else {
                studentList.clear()
                //mContext.responseHandler(it.status_code, it.message)
                std_textView.setText(it.message)
                std_textView.visibility = View.VISIBLE
                //rvStudentListing.visibility = View.GONE
            }
        } else {
            mContext.showToastMsg(getString(R.string.server_error), 2)
        }
    }


    private fun setGroupAdapter() {
        if (groupList.isEmpty()) {
            mContext.showToastMsg(getString(R.string.no_group_found), 2)
        } else {
            rvGroupList.layoutManager = LinearLayoutManager(mContext)
            rvGroupList.adapter = GroupListAdapter(mContext, groupList, this)
            //rvGroupList.adapter =  StudentListAdapter(mContext, studentList, isSelectAllChecked, this)
        }
    }

    private fun setStudentAdapter() {
        rvStudentListing.layoutManager = LinearLayoutManager(mContext)
        rvStudentListing.adapter =
            StudentListAdapter(mContext, studentList, isSelectAllChecked, this)
    }


    override fun onItemChecked(student: Student, size: SparseBooleanArray) {
        selectedStudentList.add(student)
        btnAddToGroup.isEnabled = true
        btnAddToGroup.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite))
        btnAddToGroup.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBlue))
    }

    var sel_size = 0
    override fun onItemUnchecked(student: Student, ar: SparseBooleanArray) {
        sel_size = 0
        for (i in 0 until ar.size()) {
            if (ar.valueAt(i) == true)
                sel_size++
        }
        selectedStudentList.remove(student)
        if (sel_size == 0) {
            btnAddToGroup.isEnabled = false
            btnAddToGroup.setBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.colorFadeGray
                )
            )
            btnAddToGroup.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack))
        }
    }

    override fun onResume() {
        super.onResume()
        checkBoxAll.isChecked = false
        isSelectAllChecked = false
        selectedStudentList.clear()
    }

    override fun onViewIemClick(pos: Int, id: String, name: String) {
        val action =
            CreateGroupFragmentDirections.actionCreateGroupFragmentToGroupMembersFragment(id, name)
        findNavController().navigate(action)
    }

    override fun onAddStu(id: String, name: String) {
        val action =
            CreateGroupFragmentDirections.actionCreateGroupFragmentToAddStudentFragment(id, name)
        findNavController().navigate(action)
    }
}
