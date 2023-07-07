package com.vision2020.adapter
import android.annotation.SuppressLint
import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vision2020.R
import com.vision2020.data.response.StudentListing
import com.vision2020.ui.settings.group.AddStudentFragment
import kotlinx.android.synthetic.main.item_for_add_student.view.*
import kotlinx.android.synthetic.main.item_for_members.view.layoutForMember
import kotlinx.android.synthetic.main.item_for_view_delete_members.view.btnAdd
import kotlinx.android.synthetic.main.item_for_view_delete_members.view.tvStuName
import kotlinx.android.synthetic.main.item_for_view_delete_members.view.tvStuStatus
import kotlinx.android.synthetic.main.item_for_view_delete_members.view.tvViewCheckBox

class AddStudentAdapter(
    private var context: Context,
    private var membersList: ArrayList<StudentListing.Data>,
    private var checkedAll: Boolean, private var mClick: AddStudentFragment
):RecyclerView.Adapter<AddStudentAdapter.GroupViewHolder>(){
    private val array: SparseBooleanArray = SparseBooleanArray()
    private var selected: Int = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_for_add_student,parent,false)
        return GroupViewHolder(view)
    }
    override fun getItemCount(): Int {
       return membersList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        if(membersList.size>0){
            holder.memberName.text = membersList[position].first_name
            //holder.leaderName.text =groupList[position].leader_name
            if (membersList[position].isActive == 1)holder.status.text = "Active" else holder.status.text = "Deactive"
            if(position % 2==1){
                holder.layoutForMember.setBackgroundColor(ContextCompat.getColor(context,R.color.colorGroupEven))
            }else{
                holder.layoutForMember.setBackgroundColor(ContextCompat.getColor(context,R.color.colorGroupOdd))
            }
            if(checkedAll){
                holder.checkBox.isChecked =true
                holder.checkBox.isEnabled =false
                holder.checkBox.isChecked = array.get(position, true)
            }else{
                holder.checkBox.isEnabled =true
                holder.checkBox.isChecked =false
                holder.checkBox.isChecked = array.get(position, false)
            }

            holder.radio.isChecked = selected == position

            holder.checkBox.setOnClickListener {
                if (!array.get(position, false)) {
                    holder.checkBox.isChecked = true
                    array.put(position, true)
                }
                else  {
                    holder.checkBox.isChecked = false
                    array.put(position, false)
                }
                val studentId =  membersList[position].id.toString()
                if(holder.checkBox.isChecked){
                    mClick.getItemCheck(studentId)
                }else{
                    mClick.getItemUnCheck(studentId)
                }
            }

            holder.add.setOnClickListener {
                mClick.addMember(membersList[position].id.toString())
            }

            holder.radio.setOnClickListener {
                selected = position
                mClick.onItemSelected(membersList[position].id.toString())
                notifyDataSetChanged()
            }
        }

    }

    inner class GroupViewHolder(view: View): RecyclerView.ViewHolder(view){
         val checkBox = view.tvViewCheckBox!!
         val memberName = view.tvStuName!!
         val status = view.tvStuStatus!!
          val  radio = view.rbGroupLeader!!
         val add = view.btnAdd
         val layoutForMember = view.layoutForMember

    }
    interface  OnItemClick{
        fun getItemCheck(pos: String)
        fun getItemUnCheck(student: String)
        fun addMember(id: String)
        fun onItemSelected(id : String)
    }
}