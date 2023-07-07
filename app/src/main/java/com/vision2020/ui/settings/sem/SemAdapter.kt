package com.vision2020.ui.settings.sem

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vision2020.R
import com.vision2020.ui.settings.sem.SemesterFragment.Companion.submit
import com.vision2020.ui.settings.sem.SemesterFragment.Companion.userData
import kotlinx.android.synthetic.main.fragment_create_group.*


class SemAdapter(var context: Context) :
    RecyclerView.Adapter<SemAdapter.VHItem>() {
    private val activity: Activity
    private var itemView: View? = null

    //var userData = HashMap<String, Int>()
    var dataitem = ArrayList<SemesterFragment.PoolLength>()
    fun addItems(userData: HashMap<String, Int>, dataitem: ArrayList<SemesterFragment.PoolLength>) {
        //this.userData = userData
        this.dataitem = dataitem
        notifyDataSetChanged()
    }

    fun clearItems() {
        for (i in 0 until dataitem.size) {
            notifyItemChanged(i)
            notifyItemRemoved(i);
            //this line below gives you the animation and also updates the
            //list items after the deleted item
            notifyItemRangeChanged(i, getItemCount());
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VHItem {

        itemView = LayoutInflater.from(activity)
            .inflate(R.layout.item_sem_list, parent, false)
        return VHItem(itemView)

    }

    @SuppressLint("WrongConstant")
    override fun onBindViewHolder(holder: VHItem, position: Int) {
        holder.sem.text = "Semester " + dataitem.get(position).id
        holder.sem.setTag(position.toString())
        if (!dataitem.get(Integer.parseInt(holder.sem.getTag().toString())).day.toString()
                .equals("0")
        )
            holder.day.setText(dataitem.get(position).day.toString())
        else if (dataitem.get(Integer.parseInt(holder.sem.getTag().toString())).day.toString()
                .equals("0")
        )
            holder.day.setText("")

        if (!dataitem.get(Integer.parseInt(holder.sem.getTag().toString())).week.toString()
                .equals("0")
        )
            holder.week.setText(dataitem.get(position).week.toString())
        else if (dataitem.get(Integer.parseInt(holder.sem.getTag().toString())).week.toString()
                .equals("0")
        )
            holder.week.setText("")

        holder.week.setOnTouchListener(OnTouchListener { arg0, arg1 ->
            submit.visibility = View.VISIBLE
            submit.isEnabled=true
            submit.setTextColor(ContextCompat.getColor(context,R.color.colorWhite))
            submit.setBackgroundColor(ContextCompat.getColor(context,R.color.colorBlue))
            false
        })

        holder.day.setOnTouchListener(OnTouchListener { arg0, arg1 ->
            submit.visibility = View.VISIBLE
            submit.isEnabled=true
            submit.setTextColor(ContextCompat.getColor(context,R.color.colorWhite))
            submit.setBackgroundColor(ContextCompat.getColor(context,R.color.colorBlue))
            false
        })
        holder.week.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (!s.toString().trim().equals("")) {
                    Log.v(
                        "position",
                        "data week if :" + holder.sem.text.toString()
                            .replace("Semester ", "") + "::" + s.toString().trim()
                    );
                    userData.put(
                        "week" + holder.sem.text.toString().replace("Semester ", "") + "week",
                        Integer.parseInt(s.toString())
                    )

                } else {
                    Log.v(
                        "position",
                        "data week else :" + holder.sem.text.toString()
                            .replace("Semester ", "") + "::" + s.toString().trim()
                    );

                    userData.put("0", 0)
                }
            }
        })
        holder.day.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {

                if (!s.toString().trim().equals("")) {
                    Log.v(
                        "position",
                        "data day if :" + holder.sem.text.toString()
                            .replace("Semester ", "") + "::" + s.toString().trim()
                    );
                    userData.put(
                        "week" + holder.sem.text.toString().replace("Semester ", "") + "day",
                        Integer.parseInt(s.toString())
                    )

                } else {
                    Log.v(
                        "position",
                        "data day else :" + holder.sem.text.toString()
                            .replace("Semester ", "") + "::" + s.toString().trim()
                    );
                    userData.put("0", 0)
                }
            }
        })
    }


    override fun getItemCount(): Int {
        return dataitem.size
    }


    inner class VHItem(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val sem: TextView
        val day: EditText
        val week: EditText

        init {
            sem = itemView!!.findViewById(R.id.sem)
            week = itemView.findViewById(R.id.week)
            day = itemView.findViewById(R.id.day)
        }
    }


    init {
        activity = context as Activity
    }

}



