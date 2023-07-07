package com.vision2020.ui.settings.sem

import BaseModel
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.vision2020.R
import com.vision2020.data.response.SemList
import com.vision2020.network.RetrofitClient
import com.vision2020.utils.AppConstant
import com.vision2020.utils.getAppPref
import com.vision2020.utils.progressDialog
import com.vision2020.utils.showToastMsg
import kotlinx.android.synthetic.main.fragment_create_group.*
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
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class SemesterFragment : Fragment() {
    companion object {
        var userData = HashMap<String, Int>()
        var isUpdate = false
        var isData = false
        lateinit var submit: Button
    }

    lateinit var sem_list: RecyclerView
    lateinit var no_of_sem: EditText
    var no_sem: String = ""

    lateinit var layoutForSem: LinearLayout
    lateinit var adapter: SemAdapter
    lateinit var layoutManager: SmoothScroller

    lateinit var sesDate: Spinner
    var progress: android.app.AlertDialog? = null
    var selectedDate: String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_setting_semester, container, false)
    }


    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sem_list = view.findViewById(R.id.sem_list) as RecyclerView
        sesDate = view.findViewById(R.id.sesDate) as Spinner
        no_of_sem = view.findViewById(R.id.no_of_sem) as EditText
        layoutForSem = view.findViewById(R.id.layoutForSem) as LinearLayout
        submit = view.findViewById(R.id.submit) as Button
        layoutManager = SmoothScroller(activity)
        progress = requireActivity().progressDialog(getString(R.string.request))
        sesDate.selectedItem
        sem_list.layoutManager = layoutManager
        adapter = SemAdapter(requireActivity()!!)
        sem_list.adapter = adapter
        var add = ArrayList<PoolLength>()
        sesDate.setSelection(6)
        sesDate.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                try {
                    selectedDate = parent!!.selectedItem as String
                    if (!selectedDate.equals("Select Session")) {
                        var obj = JsonObject()
                        obj.addProperty("year", selectedDate)
                        obj.addProperty("authToken", getAppPref().getString(AppConstant.KEY_TOKEN))
                        val json: String = Gson().toJson(obj)
                        val jsonParser = JsonParser()
                        val jsonObject = jsonParser.parse(json).asJsonObject
                        progress!!.show()
                        RetrofitClient.instance!!.getSemSON(jsonObject)
                            .enqueue(object : Callback<SemList> {
                                override fun onFailure(call: Call<SemList>, t: Throwable) {
                                    progress!!.dismiss()
                                    activity?.showToastMsg(getString(R.string.server_error), 2)
                                }

                                override fun onResponse(
                                    call: Call<SemList>,
                                    response: Response<SemList>
                                ) {
                                    progress!!.dismiss()
                                    if (response.body()!!.status_code.equals(AppConstant.CODE_SUCCESS)) {
                                        if (response.body()!!.data!!.size > 0) {
                                            isUpdate = true
                                            layoutForSem.visibility = View.VISIBLE
                                            submit.visibility=View.VISIBLE
                                            submit.isEnabled=false
                                            submit.setTextColor(ContextCompat.getColor(activity!!,R.color.colorBlack))
                                            submit.setBackgroundColor(ContextCompat.getColor(activity!!,R.color.colorFadeGray))

                                            no_of_sem.setText(response.body()!!.data!!.size.toString())
                                            no_sem = response.body()!!.data!!.size.toString()
                                            add.clear()
                                            userData.clear()
                                            for (i in 0 until response.body()!!.data!!.size) {
                                                var obj = PoolLength(
                                                    (i + 1),
                                                    response.body()!!.data!!.get(i).noOfDays,
                                                    response.body()!!.data!!.get(i).noOfWeeks
                                                )
                                                userData.put(
                                                    "week" + (i + 1) + "week",
                                                    response.body()!!.data!!.get(i).noOfWeeks
                                                )
                                                userData.put(
                                                    "week" + (i + 1) + "day",
                                                    response.body()!!.data!!.get(i).noOfDays
                                                )
                                                add.add(obj)
                                            }
                                            adapter.addItems(userData, add)
                                        } else {
                                            isUpdate = false
                                            if (!no_sem.equals("")) {
                                                for (i in 0 until Integer.parseInt(no_sem)) {
                                                    var obj = PoolLength(
                                                        (i + 1), 0,
                                                        0
                                                    )
                                                    add.add(obj)
                                                }
                                                adapter.addItems(userData, add)
                                            }
                                            no_of_sem.setText("")
                                            layoutForSem.visibility=View.GONE
                                            submit.visibility=View.GONE
                                            no_sem = ""
                                            add.clear()
                                            userData.clear()
                                            adapter.clearItems()
                                            adapter.notifyDataSetChanged()
                                        }
                                    } else {
                                        activity?.showToastMsg(response.body()!!.message, 2)
                                    }
                                }
                            })
                    }
                } catch (ex: Exception) {
                    Log.v("Expermient  setup::", "err" + ex.message)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        })


        no_of_sem.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }
            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (!no_of_sem.text.toString().trim().equals("")) {
                    layoutForSem.visibility = View.VISIBLE
                    submit.visibility = View.VISIBLE
                    submit.isEnabled=false
                    submit.setTextColor(ContextCompat.getColor(activity!!,R.color.colorBlack))
                    submit.setBackgroundColor(ContextCompat.getColor(activity!!,R.color.colorFadeGray))
                    no_sem = no_of_sem.text.toString()
                    add.clear()
                    for (i in 0 until Integer.parseInt(no_sem)) {
                        var obj = PoolLength((i + 1))
                        add.add(obj)
                    }
                    adapter.addItems(userData, add)
                } else {
                    add.clear()
                    userData.clear()
                    if (!no_sem.equals("")) {
                        for (i in 0 until Integer.parseInt(no_sem)) {
                            var obj = PoolLength(
                                (i + 1), 0,
                                0
                            )
                            add.add(obj)
                        }
                        no_sem = ""
                        layoutForSem.visibility=View.GONE
                        submit.visibility=View.GONE
                        adapter.addItems(userData, add)
                    }
                }
            }
        })

        submit.setOnClickListener(View.OnClickListener {

            var ar = JSONArray()
            var tweek: Int = 0
            Log.v("data::", "dasdsadsa" + userData.toString())
            for (i in 0 until add.size) {
                var ob = JSONObject()
                if (userData.get("week" + add.get(i).id + "week") != null) {
                    ob.put("noOfWeeks", userData.get("week" + add.get(i).id + "week")!!)
                    tweek = tweek + Integer.parseInt(
                        userData.get("week" + add.get(i).id + "week").toString()
                    )
                } else {
                    ob.put("noOfWeeks", "")
                }
                if (userData.get("week" + add.get(i).id + "day") != null) {
                    ob.put("noOfDays", userData.get("week" + add.get(i).id + "day")!!)
                } else {
                    ob.put("noOfDays", "")
                }

                ar.put(ob)
            }

            if (tweek == getTotalWeeksInYear()) {
                if (isUpdate == true) {
                    val builder =
                        AlertDialog.Builder(view.context)
                    val dialog: AlertDialog = builder.create()
                    val inflater = layoutInflater
                    val dialogView1: View =
                        inflater.inflate(R.layout.fragment_dialog_validate, null)

                    val button2 = dialogView1.findViewById(R.id.btnSubmit) as Button
                    val btnCancel = dialogView1.findViewById(R.id.btnCancel) as Button
                    btnCancel.setOnClickListener {
                        dialog.dismiss()
                    }


                    button2.setOnClickListener {
                        dialog.dismiss()
                        updateData(ar)
                    }


                    dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    // dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
                    dialog.setView(dialogView1)
                    dialog.show()
                } else {
                    updateData(ar)
                }
            } else {
                activity?.showToastMsg(
                    "Sum of weeks should be equal to " + getTotalWeeksInYear().toString(),
                    2
                )
            }

        })
    }

    private fun updateData(ar: JSONArray) {
        var obj = JsonObject()
        obj.addProperty("semesterData", ar.toString().replace("\"\"", ""))
        obj.addProperty("year", sesDate.selectedItem.toString())
        obj.addProperty("noOfSemester", no_sem)
        obj.addProperty("authToken", getAppPref().getString(AppConstant.KEY_TOKEN))

        val json: String = Gson().toJson(obj)
        Log.d("Response::::", json)
        val jsonParser = JsonParser()
        val jsonObject = jsonParser.parse(json).asJsonObject
        progress!!.show()
        RetrofitClient.instance!!.postSemSON(jsonObject)
            .enqueue(object : Callback<BaseModel> {
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
                        activity?.showToastMsg("Data Saved.", 1)
                    } else {
                        activity?.showToastMsg(response.body()!!.message, 2)
                    }
                }
            })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTotalWeeksInYear(): Int {
        val now = LocalDate.of(Integer.parseInt(selectedDate), Month.JANUARY, 1)
        var sunday = now.with(TemporalAdjusters.firstInMonth(DayOfWeek.SUNDAY))
        var i = 0
        do {
            i++
            sunday = sunday.plus(Period.ofDays(7))
        } while (sunday.year == Integer.parseInt(selectedDate))
        return i
    }


    inner class PoolLength {
        var id: Int = 0
        var day: Int = 0
        var week: Int = 0
        var server_id: Int = 0

        constructor(
            id: Int
        ) {
            this.id = id
        }

        constructor(
            id: Int,
            day: Int,
            week: Int
        ) {
            this.id = id
            this.day = day
            this.week = week
        }

    }
}
