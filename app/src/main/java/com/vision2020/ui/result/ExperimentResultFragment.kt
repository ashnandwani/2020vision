package com.vision2020.ui.result

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.vision2020.DialogTimeoutListener
import com.vision2020.R
import com.vision2020.adapter.DistressScenarioGridListAdapter
import com.vision2020.adapter.ExperimentAdapter
import com.vision2020.data.response.CalLoginDistressResponse
import com.vision2020.data.response.SemList
import com.vision2020.network.RetrofitClient
import com.vision2020.ui.settings.sem.SmoothScroller
import com.vision2020.ui.user.LoginActivity
import com.vision2020.utils.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.Period
import java.time.temporal.TemporalAdjusters
import java.util.*
import kotlin.collections.ArrayList

class ExperimentResultFragment : Fragment(), LogOutTimerUtil.LogOutListener,
    DialogTimeoutListener.Callback {

    lateinit var gridviewone: RecyclerView
    var currYear: Int = 0
    var tsem: Int = 0
    var tweek: ArrayList<Int> = ArrayList()
    var tdays: ArrayList<Int> = ArrayList()
    val itemList: MutableList<String> = ArrayList()
    var progress: android.app.AlertDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    @SuppressLint("ResourceType", "SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pre = view.findViewById(R.id.pre) as Button
        val next = view.findViewById(R.id.next) as Button
        val text = view.findViewById(R.id.text) as TextView
        gridviewone = view.findViewById(R.id.gridviewone) as RecyclerView
        progress = requireActivity().progressDialog(getString(R.string.request))
        pre.setOnClickListener(View.OnClickListener {
            currYear = currYear - 1
            Log.v("Nisha", "CurrYar1:::" + currYear)
            text.setText("Choose Week (" + currYear.toString() + ")")
            getData(currYear)
        })
        next.setOnClickListener(View.OnClickListener {
            currYear = currYear + 1
            Log.v("Nisha", "CurrYar2:::" + currYear)
            text.setText("Choose Week (" + currYear.toString() + ")")
            getData(currYear)
        })
        layoutManager = SmoothScroller(activity)
        gridviewone.layoutManager = layoutManager
        adapter = ExperimentAdapter(activity, this)
        gridviewone.adapter = adapter
        currYear = Calendar.getInstance().get(Calendar.YEAR)
        text.setText("Choose Week (" + currYear.toString() + ")")
        Log.v("Nisha", "CurrYar3:::" + currYear)
        getData(currYear)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getData(selectedDate: Int) {
        var obj = JsonObject()
        tsem = 0
        tweek.clear()
        tdays.clear()
        obj.addProperty("year", selectedDate.toString())
        obj.addProperty("authToken", getAppPref().getString(AppConstant.KEY_TOKEN))
        val json: String = Gson().toJson(obj)
        val jsonParser = JsonParser()
        val jsonObject = jsonParser.parse(json).asJsonObject
        progress!!.show()
        RetrofitClient.instance!!.getSemSON(jsonObject).enqueue(object : Callback<SemList> {
            override fun onFailure(call: Call<SemList>, t: Throwable) {
                progress!!.dismiss()
                activity?.showToastMsg(getString(R.string.server_error), 2)
            }

            override fun onResponse(call: Call<SemList>, response: Response<SemList>) {
                progress!!.dismiss()
                try {
                    if (response.body()!!.status_code.equals(AppConstant.CODE_SUCCESS)) {
                        if (response.body()!!.data!!.size > 0) {
                            for (i in 0 until response.body()!!.data!!.size) {
                                tsem = response.body()!!.data!!.get(i).noOfSemester
                                tweek.add(i, response.body()!!.data!!.get(i).noOfWeeks)
                                tdays.add(i, response.body()!!.data!!.get(i).noOfDays)
                            }

                            createJsonData(currYear)

                        } else {
                            createJsonData(currYear)
                        }
                    } else {
                        activity?.showToastMsg(response.body()!!.message, 2)
                    }
                }catch (ex:Exception){
                    activity?.showToastMsg(getString(R.string.server_error), 2)
                }
            }
        })
    }

    lateinit var layoutManager: SmoothScroller
    lateinit var adapter: ExperimentAdapter
    var headerdata = ArrayList<String>()
    var headerTopdata = ArrayList<ArrayList<ExperimentResultFragment.ResultData>>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun createJsonData(year: Int) {
        headerdata.clear()
        headerTopdata.clear()

        val now = LocalDate.of(year, Month.JANUARY, 1)
        var sunday = now.with(TemporalAdjusters.firstInMonth(DayOfWeek.SUNDAY))
        if (tsem != 0) {
            for (k in 0 until tsem) {
                headerdata.add(k, (k + 1).toString())
                var listdata = ArrayList<ExperimentResultFragment.ResultData>()
                for (i in 0 until tweek.get(k)) {
                    var mon = sunday.month.toString().substring(0, 3)
                    mon = mon.toCharArray()[0].toString().toUpperCase() + "" + mon.substring(
                        1,
                        mon.length
                    ).toLowerCase()

                    var rs = ResultData(
                        " Week of " + sunday.dayOfMonth + " " + mon + " " + sunday.year + " \n" + " (Week " + (i + 1) + ")",
                        sunday.monthValue.toString() + "/" + sunday.dayOfMonth.toString() + "/" + sunday.year.toString(),
                        (k + 1).toString() + "/" + (i + 1).toString() + "/" + tweek.get(k) + "/" + tdays.get(
                            k
                        )
                    )
                    sunday = sunday.plus(Period.ofDays(7))
                    listdata.add(rs)
                }
                headerTopdata.add(k, listdata)
            }
            Log.v("data", "obj::" + headerdata)
            Log.v("data", "list::" + headerTopdata.toString())
        } else {
            var listdata = ArrayList<ExperimentResultFragment.ResultData>()
            headerdata.add(0, "choose week")
            do {
                //for (k in 0 until tsem) {
                for (i in 0 until 1) {

                    var mon = sunday.month.toString().substring(0, 3)
                    mon = mon.toCharArray()[0].toString().toUpperCase() + "" + mon.substring(
                        1,
                        mon.length
                    )
                        .toLowerCase()

                    var rs = ResultData(
                        " Week of " + sunday.dayOfMonth + " " + mon + " " + sunday.year + " \n" + " (Week " + (i + 1) + ")",
                        sunday.monthValue.toString() + "/" + sunday.dayOfMonth.toString() + "/" + sunday.year.toString(),
                        "none"
                    )
                    sunday = sunday.plus(Period.ofDays(7))
                    listdata.add(rs)
                    // }
                }
            } while (sunday.year == year)

            headerTopdata.add(0, listdata)

        }

        adapter.addItems(headerdata, headerTopdata)
    }


    lateinit var appdialog: AlertDialog
    override fun doLogout() {
        Log.e("Main App", "Logout called");
        requireActivity()!!.runOnUiThread(Runnable {
            appdialog =
                AlertDialog.Builder(requireActivity()!!)
                    .setTitle("Session Timeout")
                    .setMessage("Due to inactivity, you will soon be logged out.")
                    .setPositiveButton(
                        "Extend Session"
                    ) { dialog, which ->
                        appdialog.dismiss()
                        LogOutTimerUtil.startLogoutTimer(requireActivity(), this)
                    }
                    .setNegativeButton(
                        "Log Out Now"
                    ) { dialog, which ->
                        appdialog.dismiss()
                        getAppPref().setBoolean(AppConstant.KEY_LOOGED, false)
                        val intent = Intent(requireActivity()!!, LoginActivity::class.java)
                        startActivity(intent)
                        requireActivity()!!.finishAffinity()
                    }
                    .create()
            val listener = DialogTimeoutListener(requireActivity()!!, this)
            appdialog.setOnShowListener(listener)
            appdialog.setOnDismissListener(listener)
            try {
                appdialog.show()
            } catch (ex: Exception) {

            }
        })
    }

    override fun res(type: String) {
        super.res(type)
        appdialog.dismiss()
        getAppPref().setBoolean(AppConstant.KEY_LOOGED, false)
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finishAffinity()
    }

    inner class ResultData {
        var itemList: String = ""
        var itemListTag: String = ""
        var itemDataTag: String = ""

        constructor(
            itemList: String, itemListTag: String, itemDataTag: String
        ) {
            this.itemDataTag = itemDataTag
            this.itemListTag = itemListTag
            this.itemList = itemList
        }

        override fun toString(): String {
            return "ResultData(itemList='$itemList', itemListTag='$itemListTag', itemDataTag='$itemDataTag')"
        }


    }

}

