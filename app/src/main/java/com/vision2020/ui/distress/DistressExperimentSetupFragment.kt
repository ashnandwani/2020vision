package com.vision2020.ui.distress


import BaseFragment
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.vision2020.AppApplication
import com.vision2020.R
import com.vision2020.data.response.*
import com.vision2020.database.DatabaseClient
import com.vision2020.database.distress.DistressModal
import com.vision2020.database.profile.ProfileModal
import com.vision2020.network.RetrofitClient
import com.vision2020.ui.settings.group.GroupViewModel
import com.vision2020.utils.*
import com.vision2020.utils.AppAlertDialog.GetClick
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class DistressExperimentSetupFragment : BaseFragment<DSViewModel>(), GetClick, OnTimeSetListener {
    // val arg: DistressExperimentSetupFragmentArgs by navArgs()
    private var groupViewModel: GroupViewModel? = null
    private var groupList = arrayListOf<GroupListing.Data>()

    override val layoutId: Int
        get() = R.layout.fragment_distress_experiment_setup
    override val viewModel: DSViewModel
        get() = ViewModelProvider(requireActivity()).get(DSViewModel::class.java)
    val groupRequest = ArrayList<String>()
    val idRequest = ArrayList<String>()
    val narRequest = ArrayList<String>()
    val subjectRequest = ArrayList<String>()


    private var group_id = arrayListOf<String>()

    var jo = JSONObject()
    var ja = JSONArray()
    var obj = JSONObject()

    var array = JSONArray()

    var mainObject = JSONObject() // Host object

    var requestObject = JSONObject()

    var jarray = JSONArray()

    var Objson = JSONArray()

    var grouplisting: ArrayList<GroupDataList> = ArrayList()
    private lateinit var expName: EditText
    private lateinit var expTime: EditText
    private lateinit var expLength: EditText
    private lateinit var vapeTime: EditText
    private lateinit var totalVTime: EditText
    private lateinit var expCycle: EditText
    private lateinit var waitTime: EditText
    private lateinit var drugType: Spinner
    private lateinit var expDate: Spinner
    private lateinit var drugCalc: EditText
    private lateinit var watts: EditText
    private lateinit var temprature: EditText
    private lateinit var drugConcetration: EditText
    private lateinit var lungs: EditText
    private lateinit var expEndTime: EditText
    private lateinit var llMain: LinearLayout
    private lateinit var editNarscan: EditText
    private lateinit var editId: TextView
    private lateinit var editSubject: EditText
    private lateinit var grouplist: Spinner
    private lateinit var sub: Button
    private lateinit var loadData: String
    var cal = Calendar.getInstance()

    // val list1 = JSONObject()
    var groupData: ArrayList<FetchByDateListing.Group>? = ArrayList()
    var grpData: ArrayList<GroupListing.Data>? = ArrayList()
    lateinit var btnSubmit: Button
    var fetchByDate = object : Observer<FetchByDateListing?> {
        override fun onChanged(@Nullable it: FetchByDateListing?) {
            if (it != null) {
                progress!!.dismiss()
                if (loadData.equals("loading")) {
                    if (it.status_code == AppConstant.CODE_SUCCESS) {
                        groupData = ArrayList()
                        //grpData = ArrayList()
                        var data = it.data
                        if (it.isExist.equals("1")) {
                            if (it.type.equals("ER")) {
                                AppAlertDialog().retriveData(activity, object : GetClick {
                                    override fun response(type: String) {
                                        if (type.equals("retrive")) {
                                            loadData(data)
                                        } else {
                                            clearData()
                                        }
                                    }
                                })
                            } else if (it.type.equals("DS")) {
                                loadData(data)
                            } else {
                                clearData()
                            }
                        }
                    } else {
                        clearData()
                    }
                }
            }
            loadData = "loaded"
        }
    }

    private fun clearData() {
        expName.setText("")
        expTime.setText("")
        expLength.setText("")
        vapeTime.setText("")
        totalVTime.setText("")
        expCycle.setText("")
        waitTime.setText("")
        drugConcetration.setText("")
        drugCalc.setText("")
        watts.setText("")
        temprature.setText("")
        lungs.setText("")
        expEndTime.setText("")
        drugType.setSelection(0)
        for (i in 0 until (grouplisting.size - 1)) {
            Log.v("Nisha", "count::" + llMain.getChildCount() + "::::" + grouplisting.size)
            llMain.removeViewAt(llMain.getChildCount() - 2)
        }
        groupData!!.clear()
        Objson = JSONArray()
        groupRequest.clear()
        narRequest.clear()
        subjectRequest.clear()
        createDyamicView(groupData)
    }

    private fun loadData(data: FetchByDateListing.Data?) {
        subjectRequest.clear()
        expName.setText(data!!.expName)
        expTime.setText(data!!.expTime)
        expLength.setText(data!!.expLength)
        vapeTime.setText(data!!.vapeTime)
        expCycle.setText(data!!.expCycle)
        waitTime.setText(data!!.waitTime)

        totalVTime.setText(data!!.totalVapes)
        //expDate.setText(data.expDate)
        if (data!!.drugCalc.isDigitsOnly()) {
            drugCalc.setText(data!!.drugCalc)
        } else {
            drugCalc.setText("")
        }
        if (data!!.drugConcetration.isDigitsOnly()) {
            drugConcetration.setText(data!!.drugConcetration)
        } else {
            drugConcetration.setText("")
        }
        if (data!!.temprature.isDigitsOnly()) {
            temprature.setText(data!!.temprature)
        } else {
            temprature.setText("")
        }
        if (data!!.watts.isDigitsOnly()) {
            watts.setText(data!!.watts)
        } else {
            watts.setText("")
        }
        if (data!!.lungs.isDigitsOnly()) {
            lungs.setText(data!!.lungs)
        } else {
            lungs.setText("")
        }

        /*   drugConcetration.setText(data!!.drugConcetration)
           watts.setText(data!!.watts)
           temprature.setText(data!!.temprature)
           lungs.setText(data!!.lungs)*/
        expEndTime.setText(data!!.expEndTime)
        if (dadapter.getPosition(data!!.drugType) > 0) {
            drugType.setSelection(dadapter.getPosition(data!!.drugType))
        } else {
            drugType.setSelection(0)
        }
        if (data.groups.size > 0)
            groupData = data.groups
        else
            groupData!!.clear()
        for (i in 0 until (grouplisting.size - 1)) {
            Log.v("Nisha", "count::" + llMain.getChildCount() + "::::" + grouplisting.size)
            llMain.removeViewAt(llMain.getChildCount() - 2)
        }

        createDyamicView(groupData)
    }

    fun addDate(date: String, i: Int): String? {
        val dt = date // Start date
        val sdf = SimpleDateFormat("dd/MM/yy")
        val c = Calendar.getInstance()
        try {
            c.time = sdf.parse(dt)
        } catch (e: ParseException) {
            //e.printStackTrace()
        }
        val sdf1 = SimpleDateFormat("MM-dd-yyyy")
        c.add(
            Calendar.DATE,
            i
        ) // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        val day = sdf1.format(c.time)
        return day
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val inputManager = view.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager?.hideSoftInputFromWindow(view.windowToken, 0)
        expName = view.findViewById<EditText>(R.id.expName)
        expDate = view.findViewById(R.id.expDate)
        expTime = view.findViewById<EditText>(R.id.expTime)
        expLength = view.findViewById<EditText>(R.id.expLength)
        vapeTime = view.findViewById<EditText>(R.id.vapeTime)
        totalVTime = view.findViewById<EditText>(R.id.totalVTime)
        expCycle = view.findViewById<EditText>(R.id.expCycle)
        waitTime = view.findViewById<EditText>(R.id.waitTime)
        drugType = view.findViewById<Spinner>(R.id.drugType)
        drugCalc = view.findViewById<EditText>(R.id.drugCalc)
        watts = view.findViewById<EditText>(R.id.watts)
        temprature = view.findViewById<EditText>(R.id.temprature)
        drugConcetration = view.findViewById<EditText>(R.id.drugConcetration)
        lungs = view.findViewById<EditText>(R.id.lungs)
        expEndTime = view.findViewById<EditText>(R.id.expEndTime)
        sub = view.findViewById(R.id.sub)
        expLength.setKeyListener(null)
        expLength.setOnClickListener(View.OnClickListener { v ->
            try {
                if (expTime.text.toString().isEmpty() || expEndTime.text.toString()
                        .isEmpty() || selectedDate.equals("Select Date")
                ) {
                    activity?.showToastMsg("Please enter start time , end time and date.", 2)
                } else {
                    val inputStart =
                        selectedDate + " " + expTime.text.toString().toUpperCase()
                    val inputStop =
                        selectedDate + " " + expEndTime.text.toString().toUpperCase()

                    val f: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                    val start: LocalTime = LocalTime.parse(inputStart, f)
                    val stop: LocalTime = LocalTime.parse(inputStop, f)
                    val d: Duration = Duration.between(start, stop)

                    val calendar = Calendar.getInstance()
                    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
                    val currentMinute = calendar.get(Calendar.MINUTE)
                    val currentSecond = calendar.get(Calendar.SECOND)
                    val timeP =
                        TimePickerDialog.newInstance(
                            OnTimeSetListener { view, hourOfDay, minute, seconds ->
                                when (v.getId()) {
                                    R.id.expLength -> expLength.setText(
                                        String.format("%02d", hourOfDay) +
                                                ":" + String.format("%02d", minute) +
                                                ":" + String.format("%02d", seconds)
                                    )
                                }
                            },
                            currentHour,
                            currentMinute,
                            currentSecond,
                            true
                        )

                    val sec: Long = d.seconds % 60
                    val minutes: Long = d.seconds % 3600 / 60
                    val hours: Long = d.seconds % 86400 / 3600

                    timeP.setMinTime(0, 0, 0)
                    timeP.setMaxTime(
                        Integer.parseInt(hours.toString()),
                        Integer.parseInt(minutes.toString()),
                        Integer.parseInt(sec.toString())
                    )
                    timeP.setAccentColor(Color.parseColor("#07a15b"))
                    timeP.show(requireActivity().fragmentManager, "picker")
                }
            } catch (ex: Exception) {
                activity?.showToastMsg("Please enter correct start time , end time and date.", 2)
            }
        })


        expTime.setKeyListener(null)
        expTime.setOnClickListener(View.OnClickListener { v ->
            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)
            val currentSecond = calendar.get(Calendar.SECOND)

            val timeP =
                TimePickerDialog.newInstance(
                    OnTimeSetListener { view, hourOfDay, minute, seconds ->
                        when (v.getId()) {
                            R.id.expTime -> expTime.setText(
                                String.format("%02d", hourOfDay) +
                                        ":" + String.format("%02d", minute) +
                                        ":" + String.format("%02d", seconds)
                            )
                        }
                    },
                    currentHour,
                    currentMinute,
                    currentSecond,
                    true
                )
            /* timeP.setMinTime(0, 0, 0)
             timeP.setMaxTime(24, 0, 0)  */
            timeP.setAccentColor(Color.parseColor("#07a15b"))
            timeP.show(requireActivity().fragmentManager, "picker")
        })
        waitTime.setKeyListener(null)
        waitTime.setOnClickListener(clickListener)
        expEndTime.setKeyListener(null)

        expEndTime.setOnClickListener(View.OnClickListener { v ->

            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)
            val currentSecond = calendar.get(Calendar.SECOND)
            val timeP =
                TimePickerDialog.newInstance(
                    OnTimeSetListener { view, hourOfDay, minute, seconds ->
                        when (v.getId()) {
                            R.id.expEndTime -> expEndTime.setText(
                                String.format("%02d", hourOfDay) +
                                        ":" + String.format("%02d", minute) +
                                        ":" + String.format("%02d", seconds)
                            )
                        }
                    },
                    currentHour,
                    currentMinute,
                    currentSecond,
                    true
                )
            /* timeP.setMinTime(0, 0, 0)
        timeP.setMaxTime(24, 0, 0)*/
            timeP.setAccentColor(Color.parseColor("#07a15b"))
            timeP.show(requireActivity().fragmentManager, "picker")

        })
        btnSubmit = Button(activity)
        btnSubmit.setText("Submit")

        //expCycle.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        //watts.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        //temprature.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        //drugConcetration.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        // lungs.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        val selectedweek = getAppPref().getString("dselectedweek")
        val dt = getAppPref().getString("dselDate")   // val dt = selDate // Start date
        var dateList = getWeek(getAppPref().getString("dselDate"))
        var start_date = dt?.let { addDate(it, 0) }

        val adapter: ArrayAdapter<String>? = activity?.let {
            ArrayAdapter<String>(
                it,
                android.R.layout.simple_spinner_dropdown_item,
                dateList
            )
        }
        expDate.setAdapter(adapter)
        selectedDate = "Select Date"
        //selectedDate= "12-01-2010"
        sub.setOnClickListener {
            click()
        }
        progress = requireActivity().progressDialog(getString(R.string.request))
        expDate.setOnItemSelectedListener(object :
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
                    if (!selectedDate.equals("Select Date")) {
                        loadData = "loading"
                        progress!!.show()
                        viewModel!!.checkData(start_date!!, selectedDate, activity)
                            .observe(viewLifecycleOwner, fetchByDate)
                    }
                } catch (ex: Exception) {
                    Log.v("Expermient  setup::", "err" + ex.message)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        })
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
                        drugList.clear()
                        drugType.setSelection(0)
                        if (response.body()!!.status_code == AppConstant.CODE_SUCCESS) {
                            grpData!!.clear()
                            groupData!!.clear()
                            grpData = response.body()!!.data
                            llMain = view!!.findViewById(R.id.parentLinear)
                            createDyamicView(groupData)
                            llMain.addView(btnSubmit)

                            btnSubmit.setOnClickListener {
                                click()
                            }
                            getDrugs()

                        } else if (response.body()!!.status_code == AppConstant.ERROR) {
                            activity?.logoutUser(activity!!, response.body()!!.message)
                        } else {
                            sub.visibility = View.VISIBLE
                            llMain = view!!.findViewById(R.id.parentLinear)
                            llMain.visibility = View.GONE
                            activity?.showToastMsg(response.body()!!.message, 2)
                            getDrugs()
                        }
                    }
                })
    }

    lateinit var dadapter: ArrayAdapter<String>
    private fun getDrugs() {
        RetrofitClient.instance!!.getDrugList().enqueue(
            object : Callback<DrugList> {
                override fun onFailure(call: Call<DrugList>, t: Throwable) {
                    Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<DrugList>, response: Response<DrugList>) {
                    try {
                        if (response.body()!!.status_code == AppConstant.CODE_SUCCESS) {
                            drugListName.add("Select Drug")
                            for (i in 0 until response.body()!!.data!!.size) {
                                val gname = response.body()!!.data!!.get(i).dgName
                                drugListName.add(gname)
                            }
                            dadapter = activity?.let {
                                ArrayAdapter(
                                    it,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    drugListName
                                )
                            }!!

                            drugType.setAdapter(dadapter)
                            drugType.setOnItemSelectedListener(object :
                                AdapterView.OnItemSelectedListener {
                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                }

                                @RequiresApi(Build.VERSION_CODES.N)
                                override fun onItemSelected(
                                    parent: AdapterView<*>,
                                    v: View,
                                    position: Int,
                                    id: Long
                                ) {
                                    try {
                                        selectedDrug = parent.selectedItem as String
                                    } catch (e: Exception) {
                                    }
                                }
                            })
                        } else {
                            mContext.responseHandler(
                                response.body()!!.status_code,
                                response.body()!!.message
                            )
                        }
                    } catch (ex: Exception) {
                        Log.v("Distress   Exception::", "err" + ex.message)
                        activity?.showToastMsg(getString(R.string.server_error), 2)
                    }
                }
            })
    }

    private var drugList = arrayListOf<DrugList.Data>()
    private var drugListName = arrayListOf<String>()
    private fun click() {
        var newoBj = JSONArray()
        for (j in 0 until Objson.length()) {
            if (!Objson.get(j).toString().equals("{}") && Objson.get(j)
                    .toString().contains(",")
            ) {
                var ar = Objson.get(j).toString().split(",")
                var checkCount = 0
                for (k in 0 until ar.size) {

                    try {
                        if (!ar.contains("\"id\"")) {
                            var arColon = ar.get(k).split(":")
                            if (arColon[1].equals("\"\"") || arColon[1].equals("\"\"}")) {
                                checkCount = checkCount + 1
                            }
                        }
                    } catch (e: Exception) {

                    }
                }
                if (checkCount != ar.size) {
                    newoBj.put(Objson[j])
                }
            }
        }

        when {
            selectedDate!!.trim().equals("Select Date") -> {
                activity?.showToastMsg(getString(R.string.error_expDate), 2)
            }
            expName.text!!.trim().isEmpty() -> {
                activity?.showToastMsg(getString(R.string.error_expName), 2)
            }
            expTime.text!!.trim().isEmpty() -> {
                activity?.showToastMsg(getString(R.string.error_expTime), 2)
            }
            expLength.text!!.trim().isEmpty() -> {
                activity?.showToastMsg(
                    getString(R.string.error_expLength),
                    2
                )
            }
            vapeTime.text!!.trim().isEmpty() -> {
                activity?.showToastMsg(
                    getString(R.string.error_vapeTime),
                    2
                )
            }
            totalVTime.text!!.trim().isEmpty() -> {
                activity?.showToastMsg(
                    getString(R.string.error_vapeTimeTotal),
                    2
                )
            }
            expCycle.text!!.trim().isEmpty() -> {
                activity?.showToastMsg(
                    getString(R.string.error_expCycle),
                    2
                )
            }
            waitTime.text!!.trim().isEmpty() -> {
                activity?.showToastMsg(
                    getString(R.string.error_waitTime),
                    2
                )
            }
            selectedDrug!!.trim().equals("Select Drug") -> {
                activity?.showToastMsg(getString(R.string.error_drugType), 2)
            }
            drugCalc.text!!.trim().isEmpty() -> {
                activity?.showToastMsg(
                    getString(R.string.error_drugCalc),
                    2
                )
            }
            Integer.parseInt(drugCalc.text.toString()) > 100000 -> {
                activity?.showToastMsg(
                    getString(R.string.error_drugCalc_limit),
                    2
                )
            }
            watts.text.toString().trim().isEmpty() -> {
                activity?.showToastMsg(getString(R.string.error_watts), 2)
            }
            Integer.parseInt(watts.text.toString()) > 200 -> {
                activity?.showToastMsg(
                    getString(R.string.error_watts_limit),
                    2
                )
            }
            temprature.text!!.trim().isEmpty() -> {
                activity?.showToastMsg(
                    getString(R.string.error_temprature),
                    2
                )
            }
            Integer.parseInt(temprature.text.toString()) < 100 || Integer.parseInt(temprature.text.toString()) > 1000 -> {
                activity?.showToastMsg(
                    getString(R.string.error_temprature_limit),
                    2
                )
            }
            drugConcetration.text!!.trim().isEmpty() -> {
                activity?.showToastMsg(
                    getString(R.string.error_drugConcetration),
                    2
                )
            }
            Integer.parseInt(drugConcetration.text.toString()) > 1000 -> {
                activity?.showToastMsg(
                    getString(R.string.error_drugConcetration_limit),
                    2
                )
            }
            lungs.text!!.trim().isEmpty() -> {
                activity?.showToastMsg(getString(R.string.error_lungs), 2)
            }
            Integer.parseInt(lungs.text.toString()) > 10000 -> {
                activity?.showToastMsg(
                    getString(R.string.error_lungs_limit),
                    2
                )
            }
            expEndTime.text!!.trim().isEmpty() -> {
                activity?.showToastMsg(
                    getString(R.string.error_expEndTime),
                    2
                )
            }
            groupRequest.isEmpty() && grpData!!.size > 0 -> {
                activity?.showToastMsg(
                    getString(R.string.error_groupList),
                    2
                )
            }
            narRequest.isEmpty() && grpData!!.size > 0 -> {
                activity?.showToastMsg(getString(R.string.error_narcan), 2)
            }
            subjectRequest.isEmpty() && grpData!!.size > 0 -> {
                activity?.showToastMsg(
                    getString(R.string.error_subjectpassed),
                    2
                )
            }
            else -> {
                if (requireActivity()!!.isAppConnected()) {
                    val jObj1 = JsonObject()

                    jObj1.addProperty(
                        "authToken",
                        getAppPref().getString(AppConstant.KEY_TOKEN)
                    )
                    // jObj1.addProperty("userid", "1");
                    jObj1.addProperty("expName", expName.getText().toString())
                    jObj1.addProperty("expDate", selectedDate)
                    jObj1.addProperty("expTime", expTime.getText().toString())
                    jObj1.addProperty("expLength", expLength.getText().toString())
                    jObj1.addProperty("vapeTime", vapeTime.getText().toString())
                    jObj1.addProperty("totalVapes", totalVTime.getText().toString())
                    jObj1.addProperty("expCycle", expCycle.getText().toString())
                    jObj1.addProperty("waitTime", waitTime.getText().toString())
                    jObj1.addProperty("drugType", selectedDrug)
                    jObj1.addProperty(
                        "drugCalc",
                        drugCalc.getText().toString()
                    )
                    jObj1.addProperty("watts", watts.getText().toString())
                    jObj1.addProperty(
                        "temprature",
                        temprature.getText().toString()
                    )
                    jObj1.addProperty(
                        "drugConcetration",
                        drugConcetration.getText().toString()
                    )
                    jObj1.addProperty("lungs", lungs.getText().toString())
                    jObj1.addProperty(
                        "expEndTime",
                        expEndTime.getText().toString()
                    )
                    jObj1.addProperty("groups", newoBj.toString())
                    val json: String = Gson().toJson(jObj1)
                    Log.d("Response::::", json)
                    val jsonParser = JsonParser()
                    val jsonObject = jsonParser.parse(json).asJsonObject
                    RetrofitClient.instance!!.postRawJSON(jsonObject)?.enqueue(
                        object : Callback<DistressExpResponse> {
                            override fun onFailure(
                                call: Call<DistressExpResponse>,
                                t: Throwable
                            ) {
                                t.message?.let { it1 ->
                                    activity?.showToastMsg(
                                        it1,
                                        2
                                    )
                                }

                            }

                            override fun onResponse(
                                call: Call<DistressExpResponse>,
                                response: Response<DistressExpResponse>
                            ) {
                                try {
                                    if (response.body()!!.status_code == AppConstant.CODE_SUCCESS) {
                                        val editor = AppApplication.getPref(
                                            AppApplication.getInstance()!!
                                        ).edit()
                                        editor!!.putString(
                                            "expid",
                                            response.body()!!.data.expId
                                        )
                                        editor!!.apply()
                                        findNavController().navigate(R.id.action_distressExperimentSetupFragment_to_distressScenarioStep3)
                                        activity?.showToastMsg(
                                            "Successfully experiment data saved",
                                            1
                                        )
                                        // Log.i("app:retro:service", "onResponse true ${response.body()!!.toString()}")
                                    } else {
                                        activity?.showToastMsg(
                                            response.body()!!.message,
                                            2
                                        )
                                    }
                                } catch (ex: Exception) {
                                    activity?.showToastMsg(getString(R.string.server_error), 2)
                                }

                            }
                        }
                    )
                } else {
                    var data = DistressExpData(
                        expName.getText().toString(),
                        selectedDate,
                        expTime.getText().toString(),
                        "",
                        expLength.getText().toString(),
                        vapeTime.text.toString(),
                        expCycle.text.toString(),
                        waitTime.text.toString(),
                        selectedDrug,
                        drugCalc.getText().toString(),
                        watts.getText().toString(),
                        temprature.getText().toString(),
                        drugConcetration.getText().toString(),
                        lungs.getText().toString(),
                        expEndTime.getText().toString()
                    )
                    var dataModal =
                        DistressModal()
                    var workoutDetails =
                        DistressModal.RoomConverter.saveList(data)
                    dataModal.data = workoutDetails
                    insertDataInDB(data, dataModal)
                }
            }
        }
    }

    fun insertDataInDB(data: DistressExpData, dataModal: DistressModal) {
        object : AsyncTask<Void?, Void?, Int?>() {
            @SuppressLint("WrongThread")
            override fun doInBackground(vararg params: Void?): Int? { //adding to database
                Log.d(
                    "doInBackground", "doInBackground: " + selectedDate
                )

                var avaliData =
                    DatabaseClient.getInstance(activity!!.applicationContext).appDatabase
                        .distressDao()
                        .checkData(selectedDate)

                if (avaliData == 0) {
                    var avaliData =
                        DatabaseClient.getInstance(activity!!.applicationContext).appDatabase
                            .distressDao()
                            .insert(dataModal)
                } else {
                    var avaliData =
                        DatabaseClient.getInstance(activity!!.applicationContext).appDatabase
                            .distressDao()
                            .update(dataModal.data, selectedDate)
                }

                return avaliData
            }

            override fun onPostExecute(agentsCount: Int?) { //usersTextView.setText("Users \n\n " + users);
                Log.d("onPostExecute", "onPostExecute: ")
                return
            }
        }.execute()

    }

    private fun createDyamicView(groupData: ArrayList<FetchByDateListing.Group>?) {
        groupRequest.clear()
        narRequest.clear()
        subjectRequest.clear()
        grouplisting.clear()
        Objson = JSONArray()
        /*for(i in 0 until Objson.length()) {
            Objson.remove(i)
        }*/
        grouplisting.add(GroupDataList("", "Select Group"))


        btnSubmit.setTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.colorWhite
            )
        )
        btnSubmit.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.colorGreen)))


        btnSubmit.typeface =
            activity?.let { ResourcesCompat.getFont(it, R.font.lato_regular) }
        btnSubmit.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            weight = 1.0f
            gravity = Gravity.RIGHT
            topMargin = 10
        }

        val jsonArray = JSONArray(grpData)
        var gData = grpData
        for (i in 0 until jsonArray.length()) {
            val gname = gData?.get(i)?.group_name
            val gid = gData?.get(i)?.id
            val list1 = JSONObject()
            if (gname != null) {
                if (gid != null) {
                    if (gid != null) {
                        grouplisting.add(GroupDataList(gid.toString(), gname))
                    }
                }
            }
            val view: View =
                LayoutInflater.from(activity)
                    .inflate(R.layout.distressexplinearlayout, null)
            if (i % 2 == 0) {
                view.setBackgroundResource(R.color.colorGroupEven)
            } else {
                view.setBackgroundResource(R.color.colorGroupOdd)
            }
            grouplist =
                view.findViewById<View>(R.id.lstGroup) as Spinner

            val adapter: ArrayAdapter<GroupDataList>? = activity?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_dropdown_item,
                    grouplisting
                )
            }
            grouplist.setAdapter(adapter)
            grouplist.setOnItemSelectedListener(object :
                AdapterView.OnItemSelectedListener {
                @RequiresApi(Build.VERSION_CODES.N)
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    v: View,
                    position: Int,
                    id: Long
                ) {
                    val grp: GroupDataList = parent.selectedItem as GroupDataList
                    var id = view.findViewById<View>(R.id.editId) as TextView
                    if (position != 0) {
                        if (groupRequest.contains(grp.id)) {
                            grouplist.setSelection(0)
                            activity?.showToastMsg("Already exist", 2)
                        } else {
                            groupRequest.add(grp?.id!!)
                            list1.put("group_id", grp.id)
                            list1.put("id", id.text.toString())
                        }
                    } else {
                        list1.remove("group_id")
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            })
            editNarscan = view.findViewById<View>(R.id.editNarscan1) as EditText
            editSubject =
                view.findViewById<View>(R.id.editSubjectPassed) as EditText
            editId = view.findViewById<View>(R.id.editId) as TextView
            if (groupData!!.size > 0 && groupData.size > i) {
                Log.v("Dtaaa Nisha", "...." + groupData!!.get(i).narcan)
                Log.v("Dtaaa Nisha", "...." + groupData!!.get(i).subjectpassed)
                Log.v("Dtaaa Nisha", "...." + groupData!!.size)

                editNarscan.setText(groupData!!.get(i).narcan)
                editSubject.setText(groupData!!.get(i).subjectpassed)
                editId.setText(groupData!!.get(i).id)
                for (j in 0 until gData!!.size) {
                    Log.v(
                        "Nisha",
                        "selected val::" + gData.get(j).id + ":::" + groupData?.get(i)?.group_id
                    )
                    if (gData.get(j).id.toString().equals(groupData?.get(i)?.group_id)) {
                        Log.v("Nisha", "selected val inside::" + j + ":::" + i)
                        grouplist.setSelection(j + 1)
                    }
                }
                list1.put("narcan", groupData!!.get(i).narcan)
                narRequest.add(groupData!!.get(i).narcan)
                list1.put("subjectpassed", groupData!!.get(i).subjectpassed)
                subjectRequest.add(groupData!!.get(i).subjectpassed)
                groupRequest.add(groupData!!.get(i).id)
                list1.put("group_id", groupData!!.get(i).group_id)
                list1.put("id", groupData!!.get(i).id)
                grouplist.setEnabled(false)
                grouplist.setClickable(false)
            }
            llMain.addView(view, (i + 1))
            tlist.add(editId)
            var position = llMain.indexOfChild(view)
            llMain.setTag(position);
            editSubject.addTextChangedListener(object : TextWatcher {
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
                    list1.put("subjectpassed", s.toString())
                    subjectRequest.add(s.toString())
                }
            })
            editNarscan.addTextChangedListener(object : TextWatcher {
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
                    list1.put("narcan", s.toString())
                    narRequest.add(s.toString())
                }
            })
            Objson.put(list1)
        }
    }


    //  var subjectTextWatcher: TextWatcher =
    var tlist: ArrayList<TextView> = ArrayList()
    val clickListener = View.OnClickListener { v ->
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        val currentSecond = calendar.get(Calendar.SECOND)
        val timePickerDialog =
            TimePickerDialog.newInstance(
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute, seconds ->
                    when (v.getId()) {
                        R.id.waitTime -> waitTime.setText(
                            String.format("%02d", hourOfDay) +
                                    ":" + String.format("%02d", minute) +
                                    ":" + String.format("%02d", seconds)
                        )
                    }
                },
                currentHour,
                currentMinute,
                currentSecond,
                true
            )
        timePickerDialog.setMinTime(0, 1, 0)
        timePickerDialog.setMaxTime(1, 0, 0)
        timePickerDialog.setAccentColor(Color.parseColor("#07a15b"))
        timePickerDialog.show(requireActivity().fragmentManager, "picker")
    }


    private fun getWeek(group: String?): ArrayList<String?> {
        var weekList = arrayListOf(
            "Select Date",
            group?.let { addDate(it, 0) },
            group?.let { addDate(it, 1) },
            group?.let { addDate(it, 2) },
            group?.let { addDate(it, 3) },
            group?.let { addDate(it, 4) },
            group?.let { addDate(it, 5) },
            group?.let { addDate(it, 6) })

        return weekList
    }

    var selectedDate: String = ""
    var selectedDrug: String = "Select Drug"
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_distress_experiment_setup,
            container,
            false
        )
    }

    override fun onCreateStuff() {
    }

    override fun initListeners() {
    }

    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute: Int, seconds: Int) {
        /* when (v.getId()) {
             R.id.expTime -> expTime.setText(
                 String.format("%02d", hourOfDay) +
                         ":" + String.format("%02d", minute) +
                         ":" + String.format("%02d", seconds)
             )
             R.id.expLength -> expLength.setText(
                 String.format("%02d", hourOfDay) +
                         ":" + String.format("%02d", minute) +
                         ":" + String.format("%02d", seconds)
             )
             R.id.waitTime -> waitTime.setText(
                 String.format("%02d", hourOfDay) +
                         ":" + String.format("%02d", minute) +
                         ":" + String.format("%02d", seconds)
             )
             R.id.expEndTime -> expEndTime.setText(
                 String.format("%02d", hourOfDay) +
                         ":" + String.format("%02d", minute) +
                         ":" + String.format("%02d", seconds)
             )
         }*/
    }
}




