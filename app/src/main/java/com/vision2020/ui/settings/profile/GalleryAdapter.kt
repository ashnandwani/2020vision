package com.vision2020.ui.settings.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.vision2020.R

class GalleryAdapter(context: Context) :
    RecyclerView.Adapter<GalleryAdapter.VHItem>() {
    private val activity: Activity
    private var itemView: View? = null
    var userData = ArrayList<String>()

    /* fun addItems(userData: ArrayList<String>) {
         this.userData = userData
         notifyDataSetChanged()
     }
 */

    private fun removeItem(position: Int) {
        // model.remove(position)
        notifyItemRemoved(position)
        // notifyItemRangeChanged(position, model.size())
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VHItem {

        itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gallery_image, parent, false)
        return VHItem(itemView)

    }

    @SuppressLint("WrongConstant")
    override fun onBindViewHolder(holder: VHItem, position: Int) {
        holder.root_layout.setOnClickListener({

        })
        holder.cross.setOnClickListener({
            removeItem(position)
        }

        )
    }


    override fun getItemCount(): Int {
        return 25
    }


    inner class VHItem(itemView: View?) :
        RecyclerView.ViewHolder(itemView!!) {
        val root_layout: RelativeLayout
        val cross: ImageView
        val img: ImageView
        /*   val day: EditText
          val week: EditText*/

        init {
            root_layout = itemView!!.findViewById(R.id.root_layout)
            cross = itemView!!.findViewById(R.id.cross)
            img = itemView!!.findViewById(R.id.img)
            /*week = itemView.findViewById(R.id.week)
            day = itemView.findViewById(R.id.day)*/
        }
    }

    init {
        activity = context as Activity
    }

}



