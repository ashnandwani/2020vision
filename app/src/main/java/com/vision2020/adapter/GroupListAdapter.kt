package com.vision2020.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vision2020.R
import com.vision2020.data.response.GroupListing
import kotlinx.android.synthetic.main.item_layout_for_group.view.*
class GroupListAdapter(
    private var context: Context,
    private var groupList: ArrayList<GroupListing.Data>, private var onClick: OnItemNavigation
):RecyclerView.Adapter<GroupListAdapter.GroupViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_for_group,parent,false)
        return GroupViewHolder(view)
    }
    override fun getItemCount(): Int {
       return groupList.size
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        if(groupList.size>0){
            holder.groupId.text = groupList[position].id.toString()
            holder.groupName.text = groupList[position].group_name
            holder.leaderName.text = groupList[position].leader_name
           // holder.expStatus.text = groupList[position].status

            if(groupList[position].leader_name.isEmpty()){
                holder.addStudent.visibility = View.VISIBLE
                holder.viewGroup.visibility =View.GONE
                //holder.view.visibility =View.GONE
                holder.deleteGroup.visibility = View.GONE
            }else{
                holder.addStudent.visibility = View.GONE
                holder.viewGroup.visibility =View.VISIBLE
                //holder.view.visibility =View.VISIBLE
                holder.deleteGroup.visibility = View.VISIBLE
            }

            if(position % 2==1){
                holder.groupLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.colorGroupOdd))
            }else{
                holder.groupLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.colorGroupEven))
            }

            holder.viewGroup.setOnClickListener {
                onClick.onViewIemClick(position,groupList[position].id.toString(),holder.groupName.text.toString())
            }

            holder.deleteGroup.setOnClickListener {
               onClick.onDelete(position,groupList[position].id.toString())
            }

            holder.addStudent.setOnClickListener {
                onClick.onAddStu(groupList[position].id.toString(),holder.groupName.text.toString())
            }
        }
    }

    inner class GroupViewHolder(view: View): RecyclerView.ViewHolder(view){
         val groupLayout = view.layoutGroupRoot!!
         val groupId = view.tvGroupId!!
         val groupName = view.tvGroupName!!
         val leaderName = view.tvLeaderName!!
         val expStatus = view.tvGroupStatus!!
         val addStudent = view.tvAddStudent!!
         val viewGroup = view.tvViewGroup!!
         val deleteGroup = view.tvDeleteGroup!!
        // val view = view.view!!
    }
    interface  OnItemNavigation{
         fun onViewIemClick(pos: Int, toString: String, name: String)
         fun onAddStu(pos: String, name: String)
         fun onDelete(pos: Int, toString: String)
    }
}