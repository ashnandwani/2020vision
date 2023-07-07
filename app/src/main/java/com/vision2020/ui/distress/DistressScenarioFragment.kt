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
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.vision2020.R
import com.vision2020.adapter.DistressScenarioGridListAdapter
import com.vision2020.data.response.CalLoginDistressResponse
import com.vision2020.network.RetrofitClient
import androidx.navigation.fragment.findNavController
import com.vision2020.AppApplication.Companion.getInstance
import com.vision2020.AppApplication.Companion.getPref
import com.vision2020.data.response.GroupListing
import com.vision2020.utils.*
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
import java.util.*
import kotlin.collections.ArrayList


class DistressScenarioFragment : Fragment() {

    val itemList: MutableList<String> = ArrayList()
    val itemListTag: MutableList<String> = ArrayList()
    lateinit var gridView: GridViewWithHeaderAndFooter
    var currYear: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return inflater.inflate(R.layout.fragment_distress_scenario, container, false)
    }


    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gridView = view.findViewById(R.id.gridview) as GridViewWithHeaderAndFooter
        val pre = view.findViewById(R.id.pre) as Button
        val next = view.findViewById(R.id.next) as Button
        val text = view.findViewById(R.id.text) as TextView
        pre.setOnClickListener(View.OnClickListener {
            currYear = currYear - 1
            Log.v("Nisha", "CurrYar1:::" + currYear)
            text.setText("Choose Week ("+currYear.toString()+")")
            loadCalender(currYear)
        })
        next.setOnClickListener(View.OnClickListener {
            currYear = currYear + 1
            Log.v("Nisha", "CurrYar2:::" + currYear)
            text.setText("Choose Week ("+currYear.toString()+")")
            loadCalender(currYear)
        })


        val layoutInflater = LayoutInflater.from(activity?.applicationContext)
        val headerView: View = layoutInflater.inflate(R.layout.distress_grid_header, null)
        var title = headerView!!.findViewById(R.id.text) as TextView
        title.visibility=View.INVISIBLE
        gridView.addHeaderView(headerView)
        currYear = Calendar.getInstance().get(Calendar.YEAR)
        Log.v("Nisha", "CurrYar3:::" + currYear)
        text.setText("Choose Week ("+currYear.toString()+")")
        loadCalender(Calendar.getInstance().get(Calendar.YEAR))

        gridView.onItemClickListener = AdapterView.OnItemClickListener { parent, v, position, id ->
            val selectedweek = itemList.get(position)
            var sw: String = selectedweek
            sw = sw.replace("\n", "").replace("\r", "")
            val separated: List<String> = sw.split(" ")
            if (isNetworkAvailable(requireActivity()) == true) {
                getAppPref().setString("dselectedweek", sw)
                getAppPref().setString("dselDate", itemListTag.get(position))

                if (getAppPref().getString(AppConstant.KEY_USER_TYPE) == "2") {
                    val token = getAppPref().getString(AppConstant.KEY_TOKEN)
                    RetrofitClient.instance!!.getListOfGroup(token!!, "")
                        .enqueue(
                            object : Callback<GroupListing> {
                                override fun onFailure(call: Call<GroupListing>, t: Throwable) {
                                    Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
                                }

                                @SuppressLint("ResourceType")
                                override fun onResponse(
                                    call: Call<GroupListing>,
                                    response: Response<GroupListing>
                                ) {
                                    if (response.body()!!.status_code == AppConstant.CODE_SUCCESS) {
                                        if (response.body()!!.data!!.size > 0) {
                                            showAlert(position,view)
                                        } else {
                                            activity?.showToastMsg(
                                                "No group available , Please add one to use the feature.",
                                                1
                                            )
                                        }
                                    } else {
                                        activity?.showToastMsg(
                                            "No group available , Please add one to use the feature.",
                                            1
                                        )
                                    }
                                }
                            })
                } else {
                    sw = sw.replace("Week of ", "")
                    var swp = sw.split(" (")
                    AppAlertDialog().selectDate(view, object : AppAlertDialog.GetClick {
                        override fun response(type: String) {
                            Log.v("Niha", "select date return:::" + type)
                            getAppPref().setString("selDateEXP", type)
                            findNavController().navigate(R.id.action_navigation_distress_to_distressScenarioGroup)
                        }
                    }, getActivity(), swp[0])
                }
            } else {
                requireActivity()!!.responseHandler(
                    AppConstant.ERROR,
                    getString(R.string.internet_error)
                )
            }
        }
    }

    fun showAlert(position: Int,v:View) {

        val builder =
            AlertDialog.Builder(v!!.context)
        val dialog: AlertDialog = builder.create()
        val inflater = layoutInflater
        val dialogView1: View =
            inflater.inflate(R.layout.fragment_dialog_with_data, null)

        val editText1 =
            dialogView1.findViewById(R.id.etPassword) as EditText

        editText1.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)

        val button2 =
            dialogView1.findViewById(R.id.btnSubmit) as Button


        button2.setOnClickListener { // DO SOMETHINGS

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
                    if (response.body()!!.status_code == AppConstant.CODE_SUCCESS) {
                        activity?.showToastMsg("Login Successfully", 1)
                        dialog.dismiss()

                        if (response.body()!!.data.user_type == "2") {
                            val editor = getPref(getInstance()!!).edit()
                            editor!!.putString(
                                "week",
                                ("week" + (position + 1).toString())
                            )
                            editor!!.apply()
                            findNavController().navigate(R.id.action_navigation_distress_to_distressExperimentSetupFragment)
                        } else {
                            findNavController().navigate(R.id.action_navigation_distress_to_distressScenarioGroup)
                        }

                    } else {
                        //progress!!.dismiss()
                        // mContext.showToastMsg(response.body()!!.message,1)
                        activity?.showToastMsg(response.body()!!.message, 2)
                        //Toast.makeText(mContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                }
            })
        }
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.setView(dialogView1)
        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadCalender(year: Int) {
        itemList.clear()
        itemListTag.clear()
        val now = LocalDate.of(year, Month.JANUARY, 1)
        // Find the first Sunday of the year
        // Find the first Sunday of the year
        var sunday = now.with(TemporalAdjusters.firstInMonth(DayOfWeek.SUNDAY))
        var i = 1
        var inc = 1
        do {
            // Loop to get every Sunday by adding Period.ofDays(7) the the current Sunday.
            //println(sunday.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)))

            sunday.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))

            var mon = sunday.month.toString().substring(0, 3)
            mon = mon.toCharArray()[0].toString().toUpperCase() + "" + mon.substring(1, mon.length)
                .toLowerCase()
            itemList.add("Week of " + sunday.dayOfMonth + " " + mon + " " + sunday.year + " \n" + "(Week " + i + ")")
            itemListTag.add(sunday.dayOfMonth.toString() + "/" + sunday.monthValue.toString() + "/" + sunday.year.toString())  //03/01/2021
            sunday = sunday.plus(Period.ofDays(7))
            i += inc
        } while (sunday.year == year)

        val adapter = activity?.applicationContext?.let {
            DistressScenarioGridListAdapter(
                it,
                R.layout.item_list_distressscenario,
                itemList, itemListTag
            )
        }
        gridView.adapter = adapter
    }


}
