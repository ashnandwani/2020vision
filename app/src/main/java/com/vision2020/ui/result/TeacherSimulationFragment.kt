package com.vision2020.ui.result

import BaseModel
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.vision2020.R
import com.vision2020.data.response.SimulationList
import com.vision2020.network.RetrofitClient
import com.vision2020.utils.AppConstant
import com.vision2020.utils.getAppPref
import com.vision2020.utils.progressDialog
import com.vision2020.utils.showToastMsg
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class TeacherSimulationFragment : Fragment() {
    lateinit var week_low: EditText
    lateinit var week_med: EditText
    lateinit var week_high: EditText
    lateinit var real_yr_low: EditText
    lateinit var real_yr_med: EditText
    lateinit var real_yr_high: EditText
    lateinit var real_wk_low: TextView
    lateinit var real_wk_med: TextView
    lateinit var real_wk_high: TextView
    lateinit var real_class_high: TextView
    lateinit var real_class_med: TextView
    lateinit var real_class_low: TextView
    lateinit var max_week: TextView
    lateinit var total_sumulation_yr: TextView
    lateinit var total_sumulation_wk: TextView
    lateinit var class_low: TextView
    lateinit var class_med: TextView
    lateinit var class_high: TextView
    lateinit var day_id: TextView
    lateinit var submit: Button
    lateinit var btn: Button
    var progress: android.app.AlertDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_exp_result_simulation, container, false)
    }

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         btn = view.findViewById(R.id.calculate) as Button
        submit = view.findViewById(R.id.submit) as Button
        val sem_id = view.findViewById(R.id.sem_id) as TextView
        val week_id = view.findViewById(R.id.week_id) as TextView
        progress = requireActivity().progressDialog(getString(R.string.request))
        day_id = view.findViewById(R.id.day_id) as TextView
        class_low = view.findViewById(R.id.class_low) as TextView
        class_med = view.findViewById(R.id.class_med) as TextView
        class_high = view.findViewById(R.id.class_high) as TextView

        submit.visibility = View.GONE

        week_low = view.findViewById(R.id.week_low) as EditText
        week_med = view.findViewById(R.id.week_med) as EditText
        week_high = view.findViewById(R.id.week_high) as EditText


        real_yr_low = view.findViewById(R.id.real_yr_low) as EditText
        real_yr_med = view.findViewById(R.id.real_yr_med) as EditText
        real_yr_high = view.findViewById(R.id.real_yr_high) as EditText

        real_yr_low.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                if (MotionEvent.ACTION_UP === event.getAction()) {
                    real_wk_low.setText("")
                    real_wk_med.setText("")
                    real_wk_high.setText("")
                    real_class_high.setText("")
                    real_class_med.setText("")
                    real_class_low.setText("")
                    total_sumulation_yr.setText("")
                    total_sumulation_wk.setText("")


                    class_low.setText("")
                    class_med.setText("")
                    class_high.setText("")
                    submit.visibility = View.GONE
                    btn.visibility = View.VISIBLE
                }
                return false // return is important...
            }
        })
        real_yr_med.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                if (MotionEvent.ACTION_UP === event.getAction()) {
                    real_wk_low.setText("")
                    real_wk_med.setText("")
                    real_wk_high.setText("")
                    real_class_high.setText("")
                    real_class_med.setText("")
                    real_class_low.setText("")
                    total_sumulation_yr.setText("")
                    total_sumulation_wk.setText("")

                    submit.visibility = View.GONE
                    btn.visibility = View.VISIBLE
                    class_low.setText("")
                    class_med.setText("")
                    class_high.setText("")
                }
                return false // return is important...
            }
        })
        real_yr_high.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                if (MotionEvent.ACTION_UP === event.getAction()) {
                    real_wk_low.setText("")
                    real_wk_med.setText("")
                    real_wk_high.setText("")
                    real_class_high.setText("")
                    real_class_med.setText("")
                    real_class_low.setText("")
                    total_sumulation_yr.setText("")
                    total_sumulation_wk.setText("")

                    submit.visibility = View.GONE
                    btn.visibility = View.VISIBLE
                    class_low.setText("")
                    class_med.setText("")
                    class_high.setText("")
                }
                return false // return is important...
            }
        })


        week_low.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                if (MotionEvent.ACTION_UP === event.getAction()) {
                    real_wk_low.setText("")
                    real_wk_med.setText("")
                    real_wk_high.setText("")
                    real_class_high.setText("")
                    real_class_med.setText("")
                    real_class_low.setText("")
                    total_sumulation_yr.setText("")
                    total_sumulation_wk.setText("")

                    submit.visibility = View.GONE
                    btn.visibility = View.VISIBLE
                    class_low.setText("")
                    class_med.setText("")
                    class_high.setText("")
                }
                return false // return is important...
            }
        })

        week_med.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                if (MotionEvent.ACTION_UP === event.getAction()) {
                    real_wk_low.setText("")
                    real_wk_med.setText("")
                    real_wk_high.setText("")
                    real_class_high.setText("")
                    real_class_med.setText("")
                    real_class_low.setText("")
                    total_sumulation_yr.setText("")
                    total_sumulation_wk.setText("")
                    submit.visibility = View.GONE
                    btn.visibility = View.VISIBLE

                    class_low.setText("")
                    class_med.setText("")
                    class_high.setText("")
                }
                return false // return is important...
            }
        })

        week_high.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                if (MotionEvent.ACTION_UP === event.getAction()) {
                    real_wk_low.setText("")
                    real_wk_med.setText("")
                    real_wk_high.setText("")
                    real_class_high.setText("")
                    real_class_med.setText("")
                    real_class_low.setText("")
                    total_sumulation_yr.setText("")
                    total_sumulation_wk.setText("")
                    submit.visibility = View.GONE
                    btn.visibility = View.VISIBLE

                    class_low.setText("")
                    class_med.setText("")
                    class_high.setText("")
                }
                return false // return is important...
            }
        })




        real_wk_low = view.findViewById(R.id.real_wk_low) as TextView
        real_wk_med = view.findViewById(R.id.real_wk_med) as TextView
        real_wk_high = view.findViewById(R.id.real_wk_high) as TextView
        real_class_high = view.findViewById(R.id.real_class_high) as TextView
        real_class_med = view.findViewById(R.id.real_class_med) as TextView
        real_class_low = view.findViewById(R.id.real_class_low) as TextView
        max_week = view.findViewById(R.id.max_week) as TextView
        total_sumulation_yr = view.findViewById(R.id.total_sumulation_yr) as TextView
        total_sumulation_wk = view.findViewById(R.id.total_sumulation_wk) as TextView
        val selectedweek = getAppPref().getString("itemDataTag")!!.split("/")
        getData(selectedweek[0], selectedweek[2], selectedweek[3])

        sem_id.setText(selectedweek[0])
        week_id.setText(selectedweek[2])
        day_id.setText(selectedweek[3])

        getAppPref().setString("sem", selectedweek[0].toString())
        max_week.setText(week_id.text.toString())
        submit.setOnClickListener(View.OnClickListener {
            getAppPref().setString("simulationYear", total_sumulation_yr.text.toString())
            findNavController().navigate(R.id.experimentResultStep2)
        })

        btn.setOnClickListener(View.OnClickListener {
            when {
                week_low.text.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Real Week Low Dosage", 2)
                }
                week_med.text.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Real Week Medium Dosage", 2)
                }
                week_high.text.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Real Week High Dosage", 2)
                }
                week_low.text.toString().trim().equals("0") -> {
                    activity?.showToastMsg("Please Enter Real Week Low Dosage greater than 0", 2)
                }
                week_med.text.toString().trim().equals("0") -> {
                    activity?.showToastMsg("Please Enter Real Week Medium Dosage greater than 0", 2)
                }
                week_high.text.toString().trim().equals("0") -> {
                    activity?.showToastMsg("Please Enter Real Week High Dosage greater than 0", 2)
                }
                (Integer.parseInt(
                    week_low.text.trim().toString()
                ) + Integer.parseInt(
                    week_med.text.trim().toString()
                ) + Integer.parseInt(
                    week_high.text.trim().toString()
                ) > Integer.parseInt(max_week.text.trim().toString())) ||
                        (Integer.parseInt(
                            week_low.text.trim().toString()
                        ) + Integer.parseInt(
                            week_med.text.trim().toString()
                        ) + Integer.parseInt(
                            week_high.text.trim().toString()
                        ) < Integer.parseInt(max_week.text.trim().toString())) -> {
                    activity?.showToastMsg(
                        "Please Enter Sum of weeks equal to real time total duration Year.",
                        2
                    )
                }
                real_yr_low.text.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Simulation Year Low Dosage", 2)
                }
                real_yr_med.text.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Simulation Year Medium Dosage", 2)
                }
                real_yr_high.text.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Simulation Year High Dosage", 2)
                }

                real_yr_low.text.toString().trim().equals("0") -> {
                    activity?.showToastMsg("Please Enter Simulation Year Low Dosage greater than 0", 2)
                }
                real_yr_med.text.toString().trim().equals("0")-> {
                    activity?.showToastMsg("Please Enter Simulation Year Medium Dosage greater than 0", 2)
                }
                real_yr_high.text.toString().trim().equals("0")-> {
                    activity?.showToastMsg("Please Enter Simulation Year High Dosage greater than 0", 2)
                }
                else -> {
                    submit.visibility = View.VISIBLE
                    btn.visibility = View.GONE
                    calculate()
                }
            }
        })

        submit.setOnClickListener(View.OnClickListener {
            getAppPref().setString("simulationYear", total_sumulation_yr.text.toString())
            when {
                class_low.text.trim().isEmpty() -> {
                    activity?.showToastMsg(
                        "Please fill data.",
                        2
                    )
                }
                else -> {
                    var obj = JsonObject()
                    obj.addProperty("authToken", getAppPref().getString(AppConstant.KEY_TOKEN))
                    obj.addProperty(
                        "givenDate",
                        addDate(getAppPref().getString("selDate").toString())
                    )
                    obj.addProperty("givenSemester", sem_id.text.toString())
                    obj.addProperty("givenWeek", week_id.text.toString())
                    obj.addProperty("selectedWeek", selectedweek[1])
                    obj.addProperty("givenDay", day_id.text.toString())
                    obj.addProperty("lowDosageWeek", week_low.text.toString())
                    obj.addProperty("lowDosageClass", class_low.text.toString())
                    obj.addProperty("midDosageWeek", week_med.text.toString())
                    obj.addProperty("midDosageClass", class_med.text.toString())
                    obj.addProperty("highDosageWeek", week_high.text.toString())
                    obj.addProperty("highDosageClass", class_high.text.toString())
                    obj.addProperty("simLowDosageYear", real_yr_low.text.toString())
                    obj.addProperty("simLowDosageWeek", real_wk_low.text.toString())
                    obj.addProperty("simLowDosageClass", real_class_low.text.toString())
                    obj.addProperty("simMidDosageYear", real_yr_med.text.toString())
                    obj.addProperty("simMidDosageWeek", real_wk_med.text.toString())
                    obj.addProperty("simMidDosageClass", real_class_med.text.toString())
                    obj.addProperty("simHighDosageYear", real_yr_high.text.toString())
                    obj.addProperty("simHighDosageWeek", real_wk_high.text.toString())
                    obj.addProperty("simHighDosageClass", real_class_high.text.toString())

                    val json: String = Gson().toJson(obj)
                    Log.d("Response::::", json)
                    val jsonParser = JsonParser()
                    progress!!.show()
                    val jsonObject = jsonParser.parse(json).asJsonObject
                    RetrofitClient.instance!!.postSimulationSON(jsonObject).enqueue(object :
                        Callback<BaseModel> {
                        override fun onFailure(call: Call<BaseModel>, t: Throwable) {
                            progress!!.dismiss()
                            activity?.showToastMsg(getString(R.string.server_error), 2)
                        }

                        override fun onResponse(
                            call: Call<BaseModel>,
                            response: Response<BaseModel>
                        ) {
                            progress!!.dismiss()
                            if (response.body()!!.status_code.equals(AppConstant.CODE_SUCCESS)) {
                                findNavController().navigate(R.id.experimentResultStep2)
                            } else {
                                activity?.showToastMsg(response.body()!!.message, 2)
                            }
                        }
                    })

                }
            }
        })

    }

    private fun calculate() {
        val tdur = Integer.parseInt(real_yr_low.text.toString()) +
                Integer.parseInt(real_yr_med.text.toString()) +
                Integer.parseInt(real_yr_high.text.toString())
        total_sumulation_yr.setText(tdur.toString())
        total_sumulation_wk.setText(tdur.toString())
        class_low.setText(
            ((Integer.parseInt(day_id.text.toString())).toFloat() * (Integer.parseInt(
                week_low.text.toString()
            )).toFloat()).toInt().toString()
        )
        class_med.setText(
            ((Integer.parseInt(day_id.text.toString())).toFloat() * (Integer.parseInt(
                week_med.text.toString()
            )).toFloat()).toInt().toString()
        )
        class_high.setText(
            ((Integer.parseInt(day_id.text.toString())).toFloat() * (Integer.parseInt(
                week_high.text.toString()
            )).toFloat()).toInt().toString()
        )

        real_wk_low.setText((Integer.parseInt(real_yr_low.text.toString()) * 52).toString())
        real_wk_med.setText((Integer.parseInt(real_yr_med.text.toString()) * 52).toString())
        real_wk_high.setText((Integer.parseInt(real_yr_high.text.toString()) * 52).toString())

        var high = Math.round(
            (((Integer.parseInt(real_wk_high.text.toString())).toFloat() / (class_high.text.toString()
                .toFloat())).toDouble()
                    ) * 10.0
        ) / 10.0
        var med = Math.round(
            ((Integer.parseInt(real_wk_med.text.toString())
                .toFloat() / (class_med.text.toString().toFloat())).toDouble()) * 10.0
        ) / 10.0
        var low = Math.round(
            ((Integer.parseInt(real_wk_low.text.toString())
                .toFloat() / (class_low.text.toString().toFloat())).toDouble()) * 10.0
        ) / 10.0


        real_class_high.setText(high.toString())
        real_class_med.setText(med.toString())
        real_class_low.setText(low.toString())

    }

    private fun getData(sem: String, week: String, day: String) {
        var obj = JsonObject()
        obj.addProperty("givenDate", addDate(getAppPref().getString("selDate").toString()))
        obj.addProperty("givenSemester", sem)
        obj.addProperty("givenWeek", week)
        obj.addProperty("givenDay", day)
        obj.addProperty("authToken", getAppPref().getString(AppConstant.KEY_TOKEN))
        val json: String = Gson().toJson(obj)
        val jsonParser = JsonParser()
        val jsonObject = jsonParser.parse(json).asJsonObject
        progress!!.show()
        RetrofitClient.instance!!.getSimulationSON(jsonObject)
            .enqueue(object : Callback<SimulationList> {
                override fun onFailure(call: Call<SimulationList>, t: Throwable) {
                    progress!!.dismiss()
                    activity?.showToastMsg(getString(R.string.server_error), 2)
                }

                override fun onResponse(
                    call: Call<SimulationList>,
                    response: Response<SimulationList>
                ) {
                    submit.visibility = View.VISIBLE
                    btn.visibility = View.GONE
                    progress!!.dismiss()
                    if (response.body()!!.status_code.equals(AppConstant.CODE_SUCCESS)) {
                        week_low.setText(response.body()!!.data!!.lowDosageWeek.toString())
                        week_med.setText(response.body()!!.data!!.midDosageWeek.toString())
                        week_high.setText(response.body()!!.data!!.highDosageWeek.toString())
                        real_yr_low.setText(response.body()!!.data!!.simLowDosageYear.toString())
                        real_yr_med.setText(response.body()!!.data!!.simMidDosageYear.toString())
                        real_yr_high.setText(response.body()!!.data!!.simHighDosageYear.toString())
                        calculate()
                    } else {
                        //activity?.showToastMsg(response.body()!!.message, 2)
                    }
                }
            })
    }

    fun addDate(date: String): String {  //4/7/2021
        val dt = date // Start date
        val sdf = SimpleDateFormat("MM/dd/yyyy")
        val c = Calendar.getInstance()
        try {
            c.time = sdf.parse(dt)
        } catch (e: ParseException) {
            //e.printStackTrace()
        }
        val sdf1 = SimpleDateFormat("MM-dd-yyyy")
        c.add(
            Calendar.DATE,
            0
        ) // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        val day = sdf1.format(c.time)
        return day
    }

    /*  lateinit var week_low:EditText
      lateinit var week_med:EditText
      lateinit var week_high:EditText
      lateinit var real_yr_low:EditText
      lateinit var real_yr_med:EditText
      lateinit var real_yr_high:EditText*/
}



