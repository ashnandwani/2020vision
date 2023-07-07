package com.vision2020.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.vision2020.R


internal class DistressScenarioGridListAdapter internal constructor(
    context: Context, private val resource: Int,
    private val itemList: MutableList<String>,
    private val itemListTag: MutableList<String>

) : ArrayAdapter<DistressScenarioGridListAdapter.ItemHolder>(context, resource) {
    lateinit var itemDataTag: MutableList<String>

    constructor(
        context: Context, resource: Int,
        itemList: MutableList<String>,
        itemListTag: MutableList<String>,
        itemDataTag: MutableList<String>
    ) : this(context, resource, itemList, itemListTag) {
        this.itemDataTag = itemDataTag
    }

    override fun getCount(): Int {
        return if (this.itemList != null) this.itemList.size else 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val holder: ItemHolder
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, null)
            holder = ItemHolder()
            holder.name = convertView!!.findViewById(R.id.textView)
            holder.id = convertView!!.findViewById(R.id.id)


            // holder.icon = convertView.findViewById(R.id.icon)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ItemHolder
        }

        holder.name!!.text = this.itemList!![position]
        holder.name!!.tag = this.itemListTag!![position]
        try {
            holder.id!!.text = this.itemDataTag!![position]
        } catch (ex: Exception) {

        }

        return convertView
    }

    internal class ItemHolder {
        var name: TextView? = null
        var id: TextView? = null
        // var icon: ImageView? = null
    }
}