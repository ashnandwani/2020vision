package com.vision2020.ui.settings.group

import BaseFragment
import BaseModel
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.vision2020.R
import com.vision2020.adapter.ViewMembersAdapter
import com.vision2020.data.response.GroupMembers
import com.vision2020.utils.AppConstant.CODE_SUCCESS
import com.vision2020.utils.AppConstant.ERROR
import com.vision2020.utils.isAppConnected
import com.vision2020.utils.progressDialog
import com.vision2020.utils.responseHandler
import com.vision2020.utils.showToastMsg
import kotlinx.android.synthetic.main.fragment_group_members.*
import kotlinx.android.synthetic.main.fragment_group_members.checkBoxAll
import kotlinx.android.synthetic.main.fragment_group_members.grp_textView
import java.util.*
import kotlin.Comparator


class GroupMembersFragment : BaseFragment<AddGroupViewModel>(), ViewMembersAdapter.OnItemClick {
    private var isCheckedAll = false
    private var list = arrayListOf<GroupMembers.Data>()
    private var stuList = arrayListOf<String>()
    val args: GroupMembersFragmentArgs by navArgs()
    override val layoutId: Int
        get() = R.layout.fragment_group_members
    override val viewModel: AddGroupViewModel
        get() = ViewModelProvider(this).get(AddGroupViewModel::class.java)

    override fun onCreateStuff() {
        progress = mContext.progressDialog(getString(R.string.request))
        if (mContext.isAppConnected()) {
            getStudent()
        }
        mViewModel!!.getGroupMembers().observe(viewLifecycleOwner, grp_mem)
    }

    private fun getStudent() {
        progress!!.show()
        mViewModel!!.groupMembersReq(args.id)
    }

    private fun setAdapter() {
        rvViewGroupMembers.layoutManager = LinearLayoutManager(mContext)
        rvViewGroupMembers.adapter = ViewMembersAdapter(mContext, list, isCheckedAll, this)

    }

    var sortClicked = 0;
    override fun initListeners() {
        txtGroupMembers.setText("Group Members of " + args.name)
        g_sort.setOnClickListener() {
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
            if (checkBoxAll.isChecked) {
                isCheckedAll = true
                setAdapter()
                stuList.clear()
                if (list.size > 0) {
                    for (i in list) {
                        stuList.add(i.student_id.toString())
                    }
                }

            } else {
                isCheckedAll = false
                setAdapter()
                stuList.clear()
            }
        }

        btnDeleteGroup.setOnClickListener {
            if (stuList.isEmpty()) {
                mContext.showToastMsg(getString(R.string.selected_no_member), ERROR)
            } else {
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
                    val txt =
                        dialogView.findViewById<View>(R.id.txt) as TextView
                    if(stuList.size>1){
                        txt.setText(getString(R.string.delete_students))
                    }else{
                        txt.setText(getString(R.string.delete_student))
                    }

                    btnYes.setOnClickListener {
                        if (dialogBuilder != null) {
                            dialogBuilder.dismiss()
                            if (mContext.isAppConnected()) {
                                reqDeleteMember(stuList.joinToString(","))
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
        }

        btnAddStudent.setOnClickListener {
            val action =
                GroupMembersFragmentDirections.actionGroupMembersFragmentToAddStudentFragment(
                    args.id,
                    args.name
                )
            findNavController().navigate(action)
        }
    }

    override fun getItemCheck(pos: String) {
        stuList.add(pos)
    }

    override fun getItemUnCheck(student: String) {
        stuList.remove(student)
    }

    override fun deleteMember(memId: String) {
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
            val txt =
                dialogView.findViewById<View>(R.id.txt) as TextView
            txt.setText(getString(R.string.delete_student))
            btnYes.setOnClickListener {
                if (dialogBuilder != null) {
                    dialogBuilder.dismiss()
                    if (mContext.isAppConnected()) {
                        reqDeleteMember(memId)
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

    private fun reqDeleteMember(id: String) {
        progress!!.show()
        mViewModel!!.deleteMembers(args.id, id).observe(viewLifecycleOwner, del_mem)
    }

    //....................................................................................................................
    var del_mem = object : Observer<BaseModel?> {
        override fun onChanged(@Nullable it: BaseModel?) {
            progress!!.dismiss()
            if (it!!.status_code == CODE_SUCCESS) {
                mContext.showToastMsg(it!!.message, ERROR)
                stuList.clear()
                if (isCheckedAll) {
                    requireActivity().onBackPressed()
                } else {
                    mViewModel!!.groupMembersReq(args.id)
                }
            } else {
                mContext.responseHandler(it.status_code, getString(R.string.server_error))
            }
        }
    }
    var grp_mem = object : Observer<GroupMembers?> {
        override fun onChanged(@Nullable it: GroupMembers?) {
            progress!!.dismiss()
            if (it != null) {
                if (it.status_code == CODE_SUCCESS && it.data != null) {
                    list = it.data!!
                    setAdapter()
                    btnAddStudent.visibility = View.VISIBLE
                    grp_textView.visibility = View.GONE
                    rvViewGroupMembers.visibility = View.VISIBLE
                } else {
                    //mContext.responseHandler(it.status_code, it.message)
                    grp_textView.visibility = View.VISIBLE
                    rvViewGroupMembers.visibility = View.GONE
                    grp_textView.setText(it.message)
                    btnAddStudent.visibility = View.GONE
                }
            } else {
                mContext.showToastMsg(getString(R.string.server_error), ERROR)
            }
        }
    }
}
