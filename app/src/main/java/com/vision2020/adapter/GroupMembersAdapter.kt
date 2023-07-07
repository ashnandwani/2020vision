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
import kotlinx.android.synthetic.main.item_for_members.view.*

class GroupMembersAdapter(
    private var context: Context,
    private var membersList: ArrayList<GroupMembers.Data>
) : RecyclerView.Adapter<GroupMembersAdapter.GroupViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_for_members, parent, false)
        return GroupViewHolder(view)
    }

    override fun getItemCount(): Int {
        return membersList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        if (membersList.size > 0) {
            val number = position + 1
            holder.memberName.text =
                number.toString() + "." + "       " + membersList[position].first_name
            // holder.leaderName.text =groupList[position].leader_name
            // holder.expStatus.text = groupList[position].status
            //if (membersList[position].isActive == 1)holder.tvStatusName.text = "Active" else holder.tvStatusName.text = "Deactive"
            if (position % 2 == 1) {
                holder.layoutForMember.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorGroupEven
                    )
                )
            } else {
                holder.layoutForMember.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorGroupOdd
                    )
                )
            }
        }
    }

    inner class GroupViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val memberName = view.tvMembersName!!
        val tvStatusName = view.tvStatusName!!
        val layoutForMember = view.layoutForMember
    }
}