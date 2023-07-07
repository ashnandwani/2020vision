package com.vision2020.adapter
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vision2020.R
import com.vision2020.data.response.GroupMembers
import kotlinx.android.synthetic.main.item_for_members.view.layoutForMember
import kotlinx.android.synthetic.main.item_for_view_delete_members.view.*

class ViewMembersAdapter(
    private var context: Context,
    private var membersList: ArrayList<GroupMembers.Data>,
    private var checkedAll: Boolean,private var mClick:OnItemClick
):RecyclerView.Adapter<ViewMembersAdapter.GroupViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_for_view_delete_members,parent,false)
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
            if (membersList[position].isStatus == 1)holder.status.text = "Active" else holder.status.text = "Deactive"
            if(checkedAll){
                holder.checkBox.isChecked =true
               holder.checkBox.isEnabled =false
            }else{
                holder.checkBox.isEnabled =true
                holder.checkBox.isChecked =false
            }

            if(position % 2==1){
                holder.layoutForMember.setBackgroundColor(ContextCompat.getColor(context,R.color.colorGroupEven))
            }else{
                holder.layoutForMember.setBackgroundColor(ContextCompat.getColor(context,R.color.colorGroupOdd))
            }

            holder.checkBox.setOnClickListener {
                val studentId =  membersList[position].student_id.toString()
                if(holder.checkBox.isChecked){
                    mClick.getItemCheck(studentId)
                }else{
                    mClick.getItemUnCheck(studentId)
                }
            }

            holder.delete.setOnClickListener {
               mClick.deleteMember(membersList[position].student_id.toString())
            }

        }

    }

    inner class GroupViewHolder(view: View): RecyclerView.ViewHolder(view){
         val checkBox = view.tvViewCheckBox!!
         val memberName = view.tvStuName!!
         val status = view.tvStuStatus!!
         val delete = view.tvStuActionDelete
         val add = view.btnAdd
         val layoutForMember = view.layoutForMember

    }
    interface  OnItemClick{
        fun getItemCheck(pos: String)
        fun getItemUnCheck(student: String)
        fun deleteMember(toString: String)
    }
}