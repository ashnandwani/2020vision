package com.vision2020.ui.settings.group

import `in`.srain.cube.views.GridViewWithHeaderAndFooter
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.vision2020.R
import com.vision2020.adapter.DistressScenarioGridListAdapter
import com.vision2020.network.RetrofitClient
import com.vision2020.utils.AppConstant
import com.vision2020.utils.getAppPref
import androidx.navigation.fragment.findNavController
import com.vision2020.data.response.GroupList
import com.vision2020.utils.responseHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList


class GroupStudentFragment : Fragment() {

    val itemList: MutableList<String> = ArrayList()
    val itemListTag: MutableList<String> = ArrayList()
    lateinit var gridView: GridViewWithHeaderAndFooter
    var currYear: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return inflater.inflate(R.layout.fragment_distress_scenario_group, container, false)
    }


    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gridView = view.findViewById(R.id.gridview) as GridViewWithHeaderAndFooter

        val layoutInflater = LayoutInflater.from(activity?.applicationContext)
        val headerView: View = layoutInflater.inflate(R.layout.distress_grid_header, null)
        var text = headerView.findViewById(R.id.text) as TextView

        text.setText("Groups")
        gridView.addHeaderView(headerView)
        getData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getData() {
        var token = getAppPref()!!.getString(AppConstant.KEY_TOKEN)
        if (token != null) {
            RetrofitClient.instance!!.getAllGroupList(token).enqueue(
                object : Callback<GroupList> {
                    override fun onFailure(call: Call<GroupList>, t: Throwable) {
                        Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
                        activity!!.responseHandler(AppConstant.ERROR, getString(R.string.internet_error))
                    }

                    var i = 0
                    override fun onResponse(call: Call<GroupList>, response: Response<GroupList>) {
                        if (response.body()!!.status_code == AppConstant.CODE_SUCCESS) {
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
                            activity!!.responseHandler(
                                response.body()!!.status_code,
                                response.body()!!.message
                            )
                            if (response.body()!!.message.startsWith("No records available")) {
                                try {
                                    findNavController().navigate(R.id.action_distressScenarioGroup_to_navigation_distress)
                                }catch (ex:Exception){
                                    activity!!.responseHandler(AppConstant.ERROR, "You are not associated with any group.")
                                }
                            }
                        }
                    }
                })
        }
    }

}
