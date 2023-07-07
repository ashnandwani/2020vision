package com.vision2020.ui.distress

import `in`.srain.cube.views.GridViewWithHeaderAndFooter
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.vision2020.R
import com.vision2020.adapter.DistressScenarioGridListAdapter
import com.vision2020.data.response.CalLoginDistressResponse
import com.vision2020.network.RetrofitClient
import com.vision2020.utils.AppConstant
import com.vision2020.utils.getAppPref
import com.vision2020.utils.showToastMsg
import androidx.navigation.fragment.findNavController
import com.vision2020.AppApplication
import com.vision2020.AppApplication.Companion.getInstance
import com.vision2020.AppApplication.Companion.getPref
import com.vision2020.data.response.DrugList
import com.vision2020.data.response.GroupList
import com.vision2020.utils.responseHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.TemporalAdjusters
import java.time.temporal.TemporalAdjusters.firstInMonth
import java.util.*
import kotlin.collections.ArrayList


class DistressScenarioGroupFragment : Fragment() {

    val itemList: MutableList<String> = ArrayList()
    val itemListTag: MutableList<String> = ArrayList()
    lateinit var gridView: GridViewWithHeaderAndFooter
    var currYear: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return inflater.inflate(R.layout.fragment_student_group, container, false)
    }


    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gridView = view.findViewById(R.id.gridview) as GridViewWithHeaderAndFooter

        val layoutInflater = LayoutInflater.from(activity?.applicationContext)
        val headerView: View = layoutInflater.inflate(R.layout.distress_grid_header, null)
        var text = headerView.findViewById(R.id.text) as TextView

        text.setText("Choose Group")
        gridView.addHeaderView(headerView)
        loadCalender()

        gridView.onItemClickListener = AdapterView.OnItemClickListener { parent, v, position, id ->
            //  DialogWithData().show(ft, DialogWithData.TAG)
            //(activity as DistressScenarioFragment?)?.showEditTextAlert()
            val selectedweek = itemList.get(position)


            var sw: String = selectedweek
            sw = sw.replace("\n", "").replace("\r", "")

            //Toast.makeText(activity, selectedweek, Toast.LENGTH_LONG).show()
            val separated: List<String> = sw.split(" ")



            getAppPref().setString("dselectedweek", sw)
            getAppPref().setString("dselDate", itemListTag.get(position))



            Log.v("dadss","asdasd"+itemListTag.get(position))
            Log.v("asdsad","asdasd"+itemListTag.get(position).split("??!")[0]+"::::::"+
                    itemListTag.get(position).split("??!")[1]+"::::::"+
                    itemListTag.get(position).split("??!")[2]+"::::::"+
                    itemListTag.get(position).split("??!")[3]+"::::::"
            )
            getAppPref().setString(
                "g_grp_name",
                itemListTag.get(position).split("??!")[1]
            )
            getAppPref().setString(
                "g_grp_id",
                itemListTag.get(position).split("??!")[0]
            )
            getAppPref().setInt(
                "g_narscan",
               Integer.parseInt( itemListTag.get(position).split("??!")[2])
            )
            getAppPref().setInt(
                "g_sub_passed",
                Integer.parseInt(  itemListTag.get(position).split("??!")[3])
            )
            findNavController().navigate(R.id.action_navigation_distress_to_distressScenarioStep3)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadCalender() {
        itemList.clear()
        itemListTag.clear()
        var token = getAppPref()!!.getString(AppConstant.KEY_TOKEN)
        if (token != null) {
            RetrofitClient.instance!!.getGroupList(token,getAppPref()!!.getString("selDateEXP")!!).enqueue(
                object : Callback<GroupList> {
                    override fun onFailure(call: Call<GroupList>, t: Throwable) {
                        Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
                    }

                    var i = 0
                    override fun onResponse(call: Call<GroupList>, response: Response<GroupList>) {
                        if (response.body()!!.status_code == AppConstant.CODE_SUCCESS) {
                            if (response.body()!!.data!!.size > 1) {
                                for (i in 0 until response.body()!!.data!!.size) {
                                    itemList.add(
                                       "Group Name " + " \n" + response.body()!!.data!!.get(
                                            i
                                        ).group_name + ""
                                    )
                                    itemListTag.add(
                                        response.body()!!.data!!.get(i).id.toString()
                                                + "??!" + response.body()!!.data!!.get(i).group_name +
                                                "??!" + response.body()!!.data!!.get(i).narcan + "??!" +
                                                response.body()!!.data!!.get(i).subjectpassed
                                    )
                                }
                                val adapter = activity?.applicationContext?.let {
                                    DistressScenarioGridListAdapter(
                                        it,
                                        R.layout.item_list_distressscenario,
                                        itemList, itemListTag
                                    )
                                }
                                gridView.adapter = adapter
                            } else {
                                getAppPref().setString(
                                    "g_grp_name",
                                    response.body()!!.data?.get(0)!!.group_name
                                )
                                getAppPref().setString(
                                    "g_grp_id",
                                    response.body()!!.data?.get(0)!!.id.toString()
                                )
                                getAppPref().setInt(
                                    "g_narscan",
                                    Integer.parseInt( response.body()!!.data?.get(0)!!.narcan)
                                )
                                getAppPref().setInt(
                                    "g_sub_passed",
                                    Integer.parseInt( response.body()!!.data?.get(0)!!.subjectpassed)
                                )
                                findNavController().navigate(R.id.action_navigation_distress_to_distressScenarioStep3)

                            }
                        } else {
                            activity!!.responseHandler(
                                response.body()!!.status_code,
                                response.body()!!.message
                            )
                            if (response.body()!!.message.startsWith("No records available")) {
                                findNavController().navigate(R.id.action_distressScenarioGroup_to_navigation_distress)
                            }
                        }
                    }
                })
        }
    }

}
