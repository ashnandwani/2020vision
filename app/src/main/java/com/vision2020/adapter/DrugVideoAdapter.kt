package com.vision2020.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vision2020.R
import com.vision2020.ui.faceDetection.EffectsOnFaceFragment
import kotlinx.android.synthetic.main.item_for_simple_text.view.*
import java.util.ArrayList
class DrugVideoAdapter(val mContext: Context, val list: ArrayList<String>, val mListener: EffectsOnFaceFragment
) : RecyclerView.Adapter<DrugVideoAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
      val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_for_simple_text,parent,false)
        return VideoViewHolder(mView)
    }
    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        var ar=list[position]
        var videodata=ar.split("::@")
        holder.pathText.text =videodata[0]
        holder.pathText.setOnClickListener {
            mListener.getPosition(videodata[1])
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }
    inner  class VideoViewHolder(view: View):RecyclerView.ViewHolder(view){
        val pathText = view.txtItem!!

    }
    interface ItemClick{
        fun getPosition(path:String)
    }
}