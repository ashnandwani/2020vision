package com.vision2020.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.vision2020.R
import com.vision2020.data.response.CalLoginDistressResponse
import com.vision2020.network.RetrofitClient
import com.vision2020.ui.result.ExperimentResultFragment
import com.vision2020.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExperimentViewAdapter(
    val activity: Activity,
    val frag: Fragment,
    val listdata:ArrayList<ArrayList<ExperimentResultFragment.ResultData>>,
    val rView: RecyclerView,
    val vPos: Int,
    val text: String) :
    RecyclerView.Adapter<ExperimentViewAdapter.VHItem>() {
    private var itemView: View? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExperimentViewAdapter.VHItem {
        itemView = LayoutInflater.from(parent.context)
            .inflate( R.layout.item_list_distressscenario, parent, false)
        return VHItem(itemView)

    }

    @SuppressLint("WrongConstant")
    override fun onBindViewHolder(holder: VHItem, position: Int) {


        try {
            holder.textView!!.text = listdata.get(vPos).get(position).itemList
            holder.id!!.tag = listdata.get(vPos).get(position).itemListTag
            try {
                holder.id!!.text = listdata.get(vPos).get(position).itemDataTag
            } catch (ex: Exception) {

            }

            holder.root.setOnClickListener(View.OnClickListener {
                var txt= holder.id!!
                val selectedweek =  holder.textView!!.text.toString()
                var sw: String = selectedweek
                sw = sw.replace("\n", "").replace("\r", "")
                val separated: List<String> = sw.split(" ")
                if (isNetworkAvailable(activity) == true) {
                    getAppPref().setString("selectedweek", sw)
                    getAppPref().setString("selDate", holder.id!!.tag.toString())
                    getAppPref().setString("itemDataTag", txt.text.toString())
                    if (holder.id!!.text.toString().equals("none")) {
                       findNavController(frag).navigate(R.id.action_navigation_result_to_semster)
                    } else {
                        val builder =
                            AlertDialog.Builder(activity)
                        val dialog: AlertDialog = builder.create()
                        val inflater = activity.layoutInflater
                        val dialogView1: View =
                            inflater.inflate(R.layout.fragment_dialog_with_data, null)

                        val editText1 =
                            dialogView1.findViewById(R.id.etPassword) as EditText

                        editText1.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)

                        val button2 =
                            dialogView1.findViewById(R.id.btnSubmit) as Button


                        button2.setOnClickListener { // DO SOMETHINGS
                            editText1.clearFocus()
                            (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                                editText1.windowToken,
                                0
                            )
                            val progressDialog = ProgressDialog(activity)
                            // progressDialog.setTitle("Kotlin Progress Bar")
                            progressDialog.setMessage("Loading, please wait")
                            progressDialog.show()


                            //val email = getAppPref().getString(AppConstant.KEY_EMAIL)
                            val token = getAppPref().getString(AppConstant.KEY_TOKEN)

                            RetrofitClient.instance!!.userDistressLogin(
                                token!!, editText1.text.toString()

                            ).enqueue(object : Callback<CalLoginDistressResponse> {
                                override fun onFailure(
                                    call: Call<CalLoginDistressResponse>,
                                    t: Throwable
                                ) {
                                    activity?.showToastMsg(t.message!!, 2)
                                }

                                override fun onResponse(
                                    call: Call<CalLoginDistressResponse>,
                                    response: Response<CalLoginDistressResponse>
                                ) {
                                    if (response.body()!!.status_code.toInt() == AppConstant.CODE_SUCCESS) {
                                        activity?.showToastMsg("Login Successfully", 1)
                                        findNavController(frag)!!.navigate(R.id.action_navigation_result_to_navigation_simulation)
                                    } else {
                                        activity?.showToastMsg(response.body()!!.message, 2)
                                    }
                                    dialog.dismiss()
                                    progressDialog.dismiss()
                                }
                            })
                        }


                        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        // dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
                        dialog.setView(dialogView1)
                        dialog.show()
                    }
                } else {
                    activity!!.responseHandler(
                        AppConstant.ERROR,
                        activity.getString(R.string.internet_error)
                    )
                }
            })
        } catch (ex: Exception) {

        }


    }

    override fun getItemCount(): Int {
        return listdata.get(vPos).size
    }


    inner class VHItem(itemView: View?) :
        RecyclerView.ViewHolder(itemView!!) {
        val textView: TextView
        val id: TextView
        val root: LinearLayout

        init {
            textView = itemView!!.findViewById(R.id.textView)
            root = itemView!!.findViewById(R.id.root)
            id = itemView.findViewById(R.id.id)
        }
    }

}



