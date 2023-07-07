package com.vision2020.adapter

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vision2020.R
import com.vision2020.data.Student
import com.vision2020.data.response.StudentListing
import kotlinx.android.synthetic.main.item_layout_for_school.view.*

class StudentListAdapter(
    private var context: Context,
    private var studentList: ArrayList<StudentListing.Data>,
    private var isSelectAll: Boolean,
    private val mClick:OnItemClick
): RecyclerView.Adapter<StudentListAdapter.StudentViewHolder>()  {
    private val array: SparseBooleanArray = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_for_school,parent,false)
        return  StudentViewHolder(view)
    }

    override fun getItemCount(): Int {
       return studentList.size
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        if(studentList.size>0){
            holder.stuName.text = studentList[position].first_name
            if (studentList[position].isActive == 1)holder.stuStatus.text = "Active" else holder.stuStatus.text = "Deactive"
            if(position % 2==1){
                holder.layoutStudent.setBackgroundColor(ContextCompat.getColor(context,R.color.colorGroupOdd))
            }else{
                holder.layoutStudent.setBackgroundColor(ContextCompat.getColor(context,R.color.colorGroupEven))
            }
            when {
                isSelectAll -> {
                    holder.cbStudent.isChecked = isSelectAll
                    holder.cbStudent.isEnabled = false
                    holder.cbStudent.isChecked = array.get(position, true)
                }
                else -> {
                    holder.cbStudent.isEnabled = true
                    holder.cbStudent.isChecked = array.get(position, false)
                }
            }
        }
        holder.cbStudent.setOnCheckedChangeListener(null)
        holder.cbStudent.setOnClickListener {
            if (!array.get(position, false)) {
                holder.cbStudent.isChecked = true
                array.put(position, true)
            }
            else  {
                holder.cbStudent.isChecked = false
                array.put(position, false)
            }
            val student = Student(studentList[position].first_name,studentList[position].id.toString(),true)
            if(holder.cbStudent.isChecked){
                mClick.onItemChecked(student,array)
            }else{
                mClick.onItemUnchecked(student,array)
            }

        }
    }

    inner class StudentViewHolder(view: View): RecyclerView.ViewHolder(view){
        val cbStudent = view.tvStuCheckBox!!
        val stuName = view.tvStuName!!
        val stuStatus = view.tvStuStatus!!
        val layoutStudent = view.layoutStudentRoot!!
    }
    interface  OnItemClick{
        fun onItemChecked(student: Student, size: SparseBooleanArray)

        fun onItemUnchecked(student: Student, size: SparseBooleanArray)
    }


}