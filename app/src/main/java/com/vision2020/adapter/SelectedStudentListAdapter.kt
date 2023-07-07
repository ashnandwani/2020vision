package com.vision2020.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vision2020.R
import com.vision2020.data.Student
import kotlinx.android.synthetic.main.item_layout_for_add_group.view.*

class SelectedStudentListAdapter(
    private var context: Context,
    private var studentList: ArrayList<Student>,
    private val mClick:OnItemClick
): RecyclerView.Adapter<SelectedStudentListAdapter.StudentViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_for_add_group,parent,false)
        return  StudentViewHolder(view)
    }

    override fun getItemCount(): Int {
       return studentList.size
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        if(studentList.size>0){
            holder.stuName.text = studentList[position].StudentName
            holder.deleteItem.setOnClickListener {
                mClick.updateStatus(holder.adapterPosition)
            }
        }
    }

    inner class StudentViewHolder(view: View): RecyclerView.ViewHolder(view){
        val stuName = view.tvSelectedStudent
        val deleteItem = view.imgViewCross
    }
    interface  OnItemClick{
        fun updateStatus(pos: Int)
    }
}