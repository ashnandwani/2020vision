package com.vision2020.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vision2020.R
import com.vision2020.data.response.GroupListing
import com.vision2020.ui.views.dialog.ItemCallBack
import kotlinx.android.synthetic.main.item_options.view.*
import kotlin.collections.ArrayList
class OptionAdapter(
    private var mData: ArrayList<GroupListing.Data>,
    private var mClick: ItemCallBack.IndexClick
) : RecyclerView.Adapter<OptionAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_options, parent, false)
        return ViewHolder(v)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtOption.text = mData[position].group_name
        holder.llOption.setOnClickListener {
            mClick.clickIndex(position)
        }
    }
    override fun getItemCount(): Int {
        return mData.size
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val llOption = itemView.llOption!!
        val txtOption = itemView.txtOption!!
    }
}