package com.vision2020.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.*
import com.vision2020.R
import com.vision2020.ui.result.ExperimentResultFragment


class ExperimentAdapter(context: FragmentActivity?, val frag: Fragment) :
    RecyclerView.Adapter<ExperimentAdapter.VHItem>() {
    private val activity: Activity
    private var itemView: View? = null
    var headerdata = ArrayList<String>()
    var listdata = ArrayList<ArrayList<ExperimentResultFragment.ResultData>>()
    fun addItems(
        headerdata: ArrayList<String>,
        listdata: ArrayList<ArrayList<ExperimentResultFragment.ResultData>>
    ) {
        this.headerdata = headerdata
        this.listdata = listdata
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExperimentAdapter.VHItem {
        itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.distress_grid_header, parent, false)
        return VHItem(itemView)

    }

    @SuppressLint("WrongConstant")
    override fun onBindViewHolder(holder: VHItem, position: Int) {
        try {
            if (headerdata.get(position).equals("choose week")) {
                holder.title_top.visibility=View.INVISIBLE
            } else {
                holder.title_top.visibility=View.VISIBLE
                holder.title_top.text = "Semester" + headerdata.get(position)
            }
            holder.seriesRecyclerview.adapter =
                ExperimentViewAdapter(
                    activity,
                    frag,
                    listdata,
                    holder.seriesRecyclerview,
                    position, headerdata.get(position)
                )

            holder.seriesRecyclerview.layoutManager =
                GridLayoutManager(activity, 5)
            val snapHelper: SnapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(holder.seriesRecyclerview)
        } catch (ex: Exception) {

        }


    }


    override fun getItemCount(): Int {
        return headerdata.size
    }


    inner class VHItem(itemView: View?) :
        RecyclerView.ViewHolder(itemView!!) {
        val title_top: TextView
        val seriesRecyclerview: RecyclerView

        init {
            title_top = itemView!!.findViewById(R.id.text)
            seriesRecyclerview = itemView.findViewById(R.id.gridview)
        }
    }

    init {
        activity = context as Activity
    }

}



