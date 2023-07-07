package com.vision2020.ui.result

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.vision2020.R
import com.vision2020.data.response.CalLoginDistressResponse
import com.vision2020.data.response.DrugList
import com.vision2020.data.response.ExpResultEditResponse
import com.vision2020.data.response.ExpResultResponse
import com.vision2020.network.RetrofitClient
import com.vision2020.utils.*
import com.vision2020.utils.AppAlertDialog.GetResult
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.fragment_experiment_result_step2.*
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


class ExperimentResultStep2 : Fragment(), GetResult {

    var rejultObj1 = JsonObject()
    var experimentId = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_experiment_result_step2, container, false)
    }

    var hdate0: String? = ""
    var hdate1: String? = ""
    var hdate2: String? = ""
    var hdate3: String? = ""
    var hdate4: String? = ""
    var hdate5: String? = ""
    var hdate6: String? = ""
    var hdate7: String? = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calculateResult.setEnabled(false)
        val inputManager = view.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager?.hideSoftInputFromWindow(view.windowToken, 0)
        val selectedweek = getAppPref().getString("selectedweek")
        val selDate = getAppPref().getString("selDate")
        val dt = selDate // Start date
        val hday1 = 1
        val hday2 = 2
        val hday3 = 3
        val hday4 = 4
        val hday5 = 5
        val hday6 = 6
        val hday7 = 7
        hdate0 = dt?.let { addDate(it, 0) }
        hdate1 = dt?.let { addDate(it, 0) }
        hdate2 = dt?.let { addDate(it, 1) }
        hdate3 = dt?.let { addDate(it, 2) }
        hdate4 = dt?.let { addDate(it, 3) }
        hdate5 = dt?.let { addDate(it, 4) }
        hdate6 = dt?.let { addDate(it, 5) }
        hdate7 = dt?.let { addDate(it, 6) }

        var day0 = view.findViewById<TextView>(R.id.textView21)
        day0.setText(selectedweek)

        val expDate1 = view.findViewById<EditText>(R.id.expDate1)

        val expDate2 = view.findViewById<EditText>(R.id.expDate2)
        val expDate3 = view.findViewById<EditText>(R.id.expDate3)
        val expDate4 = view.findViewById<EditText>(R.id.expDate4)
        val expDate5 = view.findViewById<EditText>(R.id.expDate5)
        val expDate6 = view.findViewById<EditText>(R.id.expDate6)
        val expDate7 = view.findViewById<EditText>(R.id.expDate7)

        expDate1.setText(hdate1)
        expDate2.setText(hdate2)
        expDate3.setText(hdate3)
        expDate4.setText(hdate4)
        expDate5.setText(hdate5)
        expDate6.setText(hdate6)
        expDate7.setText(hdate7)


        val expTime1 = view.findViewById<EditText>(R.id.expTime1)
        val expTime2 = view.findViewById<EditText>(R.id.expTime2)
        val expTime3 = view.findViewById<EditText>(R.id.expTime3)
        val expTime4 = view.findViewById<EditText>(R.id.expTime4)
        val expTime5 = view.findViewById<EditText>(R.id.expTime5)
        val expTime6 = view.findViewById<EditText>(R.id.expTime6)


        val expLength1 = view.findViewById<EditText>(R.id.expLength1)
        val expLength2 = view.findViewById<EditText>(R.id.expLength2)
        val expLength3 = view.findViewById<EditText>(R.id.expLength3)
        val expLength4 = view.findViewById<EditText>(R.id.expLength4)
        val expLength5 = view.findViewById<EditText>(R.id.expLength5)
        val expLength6 = view.findViewById<EditText>(R.id.expLength6)
        val expLength7 = view.findViewById<EditText>(R.id.expLength7)


        val waitTime1 = view.findViewById<EditText>(R.id.waitTime1)
        val waitTime2 = view.findViewById<EditText>(R.id.waitTime2)
        val waitTime3 = view.findViewById<EditText>(R.id.waitTime3)
        val waitTime4 = view.findViewById<EditText>(R.id.waitTime4)
        val waitTime5 = view.findViewById<EditText>(R.id.waitTime5)
        val waitTime6 = view.findViewById<EditText>(R.id.waitTime6)
        val waitTime7 = view.findViewById<EditText>(R.id.waitTime7)


        val clickListener = View.OnClickListener { v ->

            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)
            val currentSecond = calendar.get(Calendar.SECOND)
            val timePickerDialogOne =
                TimePickerDialog.newInstance(
                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute, seconds ->
                        when (v.getId()) {
                            R.id.expTime1 -> expTime1.setText(
                                String.format("%02d", hourOfDay) +
                                        ":" + String.format("%02d", minute) +
                                        ":" + String.format("%02d", seconds)
                            )
                            R.id.expTime2 -> expTime2.setText(
                                String.format("%02d", hourOfDay) +
                                        ":" + String.format("%02d", minute) +
                                        ":" + String.format("%02d", seconds)
                            )
                            R.id.expTime3 -> expTime3.setText(
                                String.format("%02d", hourOfDay) +
                                        ":" + String.format("%02d", minute) +
                                        ":" + String.format("%02d", seconds)
                            )
                            R.id.expTime4 -> expTime4.setText(
                                String.format("%02d", hourOfDay) +
                                        ":" + String.format("%02d", minute) +
                                        ":" + String.format("%02d", seconds)
                            )
                            R.id.expTime5 -> expTime5.setText(
                                String.format("%02d", hourOfDay) +
                                        ":" + String.format("%02d", minute) +
                                        ":" + String.format("%02d", seconds)
                            )
                            R.id.expTime6 -> expTime6.setText(
                                String.format("%02d", hourOfDay) +
                                        ":" + String.format("%02d", minute) +
                                        ":" + String.format("%02d", seconds)
                            )
                            R.id.expTime7 -> expTime7.setText(
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
            // timePickerDialogOne.setMinTime(0, 1, 0)
            // timePickerDialogOne.setMaxTime(1, 0, 0)
            timePickerDialogOne.setAccentColor(Color.parseColor("#07a15b"))
            timePickerDialogOne.show(requireActivity().fragmentManager, "picker")

        }

        val lclickListener = View.OnClickListener { v ->

            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)
            val currentSecond = calendar.get(Calendar.SECOND)

            when (v.getId()) {
                R.id.expLength1 -> {
                    try {
                        if (expTime1.text.toString().isEmpty() || expEndTime1.text.toString()
                                .isEmpty()
                        ) {
                            activity?.showToastMsg(
                                "Please enter start time , end time of Day 1.",
                                2
                            )
                        } else {
                            val inputStart =
                                expDate1.text.toString() + " " + expTime1.text.toString()
                                    .toUpperCase()
                            val inputStop =
                                expDate1.text.toString() + " " + expEndTime1.text.toString()
                                    .toUpperCase()

                            val f: DateTimeFormatter =
                                DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss")
                            val start: LocalTime = LocalTime.parse(inputStart, f)
                            val stop: LocalTime = LocalTime.parse(inputStop, f)
                            val d: Duration = Duration.between(start, stop)
                            val timePickerDialogThre =
                                TimePickerDialog.newInstance(
                                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute, seconds ->
                                        when (v.getId()) {
                                            R.id.expLength1 -> expLength1.setText(
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

                            timePickerDialogThre.setMinTime(0, 0, 0)
                            timePickerDialogThre.setMaxTime(
                                Integer.parseInt(hours.toString()),
                                Integer.parseInt(minutes.toString()),
                                Integer.parseInt(sec.toString())
                            )
                            timePickerDialogThre.setAccentColor(Color.parseColor("#07a15b"))
                            timePickerDialogThre.show(requireActivity().fragmentManager, "picker")
                        }
                    } catch (ex: Exception) {
                        activity?.showToastMsg(
                            "Please enter correct start time , end time of day 1.",
                            2
                        )
                    }
                }
                R.id.expLength2 -> {
                    try {
                    if (expTime2.text.toString().isEmpty() || expEndTime2.text.toString()
                            .isEmpty()
                    ) {
                        activity?.showToastMsg("Please enter start time , end time of Day 2.", 2)
                    } else {
                        val inputStart =
                            expDate2.text.toString() + " " + expTime2.text.toString().toUpperCase()
                        val inputStop = expDate2.text.toString() + " " + expEndTime2.text.toString()
                            .toUpperCase()

                        val f: DateTimeFormatter =
                            DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss")
                        val start: LocalTime = LocalTime.parse(inputStart, f)
                        val stop: LocalTime = LocalTime.parse(inputStop, f)
                        val d: Duration = Duration.between(start, stop)
                        val timePickerDialogThre =
                            TimePickerDialog.newInstance(
                                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute, seconds ->
                                    when (v.getId()) {
                                        R.id.expLength2 -> expLength2.setText(
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

                        timePickerDialogThre.setMinTime(0, 0, 0)
                        timePickerDialogThre.setMaxTime(
                            Integer.parseInt(hours.toString()),
                            Integer.parseInt(minutes.toString()),
                            Integer.parseInt(sec.toString())
                        )
                        timePickerDialogThre.setAccentColor(Color.parseColor("#07a15b"))
                        timePickerDialogThre.show(requireActivity().fragmentManager, "picker")
                    }} catch (ex: Exception) {
                        activity?.showToastMsg("Please enter correct start time , end time of day 2.", 2)
                    }
                }
                R.id.expLength3 -> {
                    try {
                        if (expTime3.text.toString().isEmpty() || expEndTime3.text.toString()
                                .isEmpty()
                        ) {
                            activity?.showToastMsg(
                                "Please enter start time , end time of Day 3.",
                                2
                            )
                        } else {
                            val inputStart =
                                expDate3.text.toString() + " " + expTime3.text.toString()
                                    .toUpperCase()
                            val inputStop =
                                expDate3.text.toString() + " " + expEndTime3.text.toString()
                                    .toUpperCase()

                            val f: DateTimeFormatter =
                                DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss")
                            val start: LocalTime = LocalTime.parse(inputStart, f)
                            val stop: LocalTime = LocalTime.parse(inputStop, f)
                            val d: Duration = Duration.between(start, stop)
                            val timePickerDialogThre =
                                TimePickerDialog.newInstance(
                                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute, seconds ->
                                        when (v.getId()) {
                                            R.id.expLength3 -> expLength3.setText(
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

                            timePickerDialogThre.setMinTime(0, 0, 0)
                            timePickerDialogThre.setMaxTime(
                                Integer.parseInt(hours.toString()),
                                Integer.parseInt(minutes.toString()),
                                Integer.parseInt(sec.toString())
                            )
                            timePickerDialogThre.setAccentColor(Color.parseColor("#07a15b"))
                            timePickerDialogThre.show(requireActivity().fragmentManager, "picker")
                        }
                    } catch (ex: Exception) {
                        activity?.showToastMsg("Please enter correct start time , end time of day 3.", 2)
                    }
                }
                R.id.expLength4 -> {
                    try{
                    if (expTime4.text.toString().isEmpty() || expEndTime4.text.toString()
                            .isEmpty()
                    ) {
                        activity?.showToastMsg("Please enter start time , end time of Day 4.", 2)
                    } else {
                        val inputStart =
                            expDate4.text.toString() + " " + expTime4.text.toString().toUpperCase()
                        val inputStop = expDate4.text.toString() + " " + expEndTime4.text.toString()
                            .toUpperCase()

                        val f: DateTimeFormatter =
                            DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss")
                        val start: LocalTime = LocalTime.parse(inputStart, f)
                        val stop: LocalTime = LocalTime.parse(inputStop, f)
                        val d: Duration = Duration.between(start, stop)
                        val timePickerDialogThre =
                            TimePickerDialog.newInstance(
                                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute, seconds ->
                                    when (v.getId()) {
                                        R.id.expLength4 -> expLength4.setText(
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

                        timePickerDialogThre.setMinTime(0, 0, 0)
                        timePickerDialogThre.setMaxTime(
                            Integer.parseInt(hours.toString()),
                            Integer.parseInt(minutes.toString()),
                            Integer.parseInt(sec.toString())
                        )
                        timePickerDialogThre.setAccentColor(Color.parseColor("#07a15b"))
                        timePickerDialogThre.show(requireActivity().fragmentManager, "picker")
                    }
                    } catch (ex: Exception) {
                        activity?.showToastMsg("Please enter correct start time , end time of day 4.", 2)
                    }
                }

                R.id.expLength5 -> {
                    try{
                    if (expTime5.text.toString().isEmpty() || expEndTime5.text.toString()
                            .isEmpty()
                    ) {
                        activity?.showToastMsg("Please enter start time , end time of Day 5.", 2)
                    } else {
                        val inputStart =
                            expDate5.text.toString() + " " + expTime5.text.toString().toUpperCase()
                        val inputStop = expDate5.text.toString() + " " + expEndTime5.text.toString()
                            .toUpperCase()

                        val f: DateTimeFormatter =
                            DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss")
                        val start: LocalTime = LocalTime.parse(inputStart, f)
                        val stop: LocalTime = LocalTime.parse(inputStop, f)
                        val d: Duration = Duration.between(start, stop)
                        val timePickerDialogThre =
                            TimePickerDialog.newInstance(
                                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute, seconds ->
                                    when (v.getId()) {
                                        R.id.expLength5 -> expLength5.setText(
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

                        timePickerDialogThre.setMinTime(0, 0, 0)
                        timePickerDialogThre.setMaxTime(
                            Integer.parseInt(hours.toString()),
                            Integer.parseInt(minutes.toString()),
                            Integer.parseInt(sec.toString())
                        )
                        timePickerDialogThre.setAccentColor(Color.parseColor("#07a15b"))
                        timePickerDialogThre.show(requireActivity().fragmentManager, "picker")
                    }} catch (ex: Exception) {
                        activity?.showToastMsg("Please enter correct start time , end time of day 5.", 2)
                    }
                }

                R.id.expLength6 -> {
                    try{
                    if (expTime6.text.toString().isEmpty() || expEndTime6.text.toString()
                            .isEmpty()
                    ) {
                        activity?.showToastMsg("Please enter start time , end time of Day 6.", 2)
                    } else {
                        val inputStart =
                            expDate6.text.toString() + " " + expTime6.text.toString().toUpperCase()
                        val inputStop = expDate6.text.toString() + " " + expEndTime6.text.toString()
                            .toUpperCase()

                        val f: DateTimeFormatter =
                            DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss")
                        val start: LocalTime = LocalTime.parse(inputStart, f)
                        val stop: LocalTime = LocalTime.parse(inputStop, f)
                        val d: Duration = Duration.between(start, stop)
                        val timePickerDialogThre =
                            TimePickerDialog.newInstance(
                                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute, seconds ->
                                    when (v.getId()) {
                                        R.id.expLength6 -> expLength6.setText(
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

                        timePickerDialogThre.setMinTime(0, 0, 0)
                        timePickerDialogThre.setMaxTime(
                            Integer.parseInt(hours.toString()),
                            Integer.parseInt(minutes.toString()),
                            Integer.parseInt(sec.toString())
                        )
                        timePickerDialogThre.setAccentColor(Color.parseColor("#07a15b"))
                        timePickerDialogThre.show(requireActivity().fragmentManager, "picker")
                    }} catch (ex: Exception) {
                        activity?.showToastMsg("Please enter correct start time , end time of day 6.", 2)
                    }
                }

                R.id.expLength7 -> {
                    try{
                    if (expTime7.text.toString().isEmpty() || expEndTime7.text.toString()
                            .isEmpty()
                    ) {
                        activity?.showToastMsg("Please enter start time , end time of Day 7.", 2)
                    } else {
                        val inputStart =
                            expDate7.text.toString() + " " + expTime7.text.toString().toUpperCase()
                        val inputStop = expDate7.text.toString() + " " + expEndTime7.text.toString()
                            .toUpperCase()

                        val f: DateTimeFormatter =
                            DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss")
                        val start: LocalTime = LocalTime.parse(inputStart, f)
                        val stop: LocalTime = LocalTime.parse(inputStop, f)
                        val d: Duration = Duration.between(start, stop)
                        val timePickerDialogThre =
                            TimePickerDialog.newInstance(
                                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute, seconds ->
                                    when (v.getId()) {
                                        R.id.expLength7 -> expLength7.setText(
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

                        timePickerDialogThre.setMinTime(0, 0, 0)
                        timePickerDialogThre.setMaxTime(
                            Integer.parseInt(hours.toString()),
                            Integer.parseInt(minutes.toString()),
                            Integer.parseInt(sec.toString())
                        )
                        timePickerDialogThre.setAccentColor(Color.parseColor("#07a15b"))
                        timePickerDialogThre.show(requireActivity().fragmentManager, "picker")
                    }} catch (ex: Exception) {
                        activity?.showToastMsg("Please enter correct start time , end time of day 7.", 2)
                    }
                }
            }
        }

        val eclickListener = View.OnClickListener { v ->

            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)
            val currentSecond = calendar.get(Calendar.SECOND)

            val timePickerDialogThre =
                TimePickerDialog.newInstance(
                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute, seconds ->
                        when (v.getId()) {
                            R.id.expEndTime1 -> expEndTime1.setText(
                                String.format("%02d", hourOfDay) +
                                        ":" + String.format("%02d", minute) +
                                        ":" + String.format("%02d", seconds)
                            )
                            R.id.expEndTime2 -> expEndTime2.setText(
                                String.format("%02d", hourOfDay) +
                                        ":" + String.format("%02d", minute) +
                                        ":" + String.format("%02d", seconds)
                            )
                            R.id.expEndTime3 -> expEndTime3.setText(
                                String.format("%02d", hourOfDay) +
                                        ":" + String.format("%02d", minute) +
                                        ":" + String.format("%02d", seconds)
                            )
                            R.id.expEndTime4 -> expEndTime4.setText(
                                String.format("%02d", hourOfDay) +
                                        ":" + String.format("%02d", minute) +
                                        ":" + String.format("%02d", seconds)
                            )
                            R.id.expEndTime5 -> expEndTime5.setText(
                                String.format("%02d", hourOfDay) +
                                        ":" + String.format("%02d", minute) +
                                        ":" + String.format("%02d", seconds)
                            )
                            R.id.expEndTime6 -> expEndTime6.setText(
                                String.format("%02d", hourOfDay) +
                                        ":" + String.format("%02d", minute) +
                                        ":" + String.format("%02d", seconds)
                            )

                            R.id.expEndTime7 -> expEndTime7.setText(
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
            //timePickerDialogThre.setMinTime(0, 1, 0)
            // timePickerDialogThre.setMaxTime(1, 0, 0)
            timePickerDialogThre.setAccentColor(Color.parseColor("#07a15b"))
            timePickerDialogThre.show(requireActivity().fragmentManager, "picker")
        }

        val wclickListener = View.OnClickListener { v ->

            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)
            val currentSecond = calendar.get(Calendar.SECOND)
            val timePickerDialogFor =
                TimePickerDialog.newInstance(
                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute, seconds ->
                        when (v.getId()) {
                            R.id.waitTime1 -> waitTime1.setText(
                                String.format("%02d", hourOfDay) +
                                        ":" + String.format("%02d", minute) +
                                        ":" + String.format("%02d", seconds)
                            )
                            R.id.waitTime2 -> waitTime2.setText(
                                String.format("%02d", hourOfDay) +
                                        ":" + String.format("%02d", minute) +
                                        ":" + String.format("%02d", seconds)
                            )
                            R.id.waitTime3 -> waitTime3.setText(
                                String.format("%02d", hourOfDay) +
                                        ":" + String.format("%02d", minute) +
                                        ":" + String.format("%02d", seconds)
                            )
                            R.id.waitTime4 -> waitTime4.setText(
                                String.format("%02d", hourOfDay) +
                                        ":" + String.format("%02d", minute) +
                                        ":" + String.format("%02d", seconds)
                            )
                            R.id.waitTime5 -> waitTime5.setText(
                                String.format("%02d", hourOfDay) +
                                        ":" + String.format("%02d", minute) +
                                        ":" + String.format("%02d", seconds)
                            )
                            R.id.waitTime6 -> waitTime6.setText(
                                String.format("%02d", hourOfDay) +
                                        ":" + String.format("%02d", minute) +
                                        ":" + String.format("%02d", seconds)
                            )

                            R.id.waitTime7 -> waitTime7.setText(
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
            timePickerDialogFor.setMinTime(0, 1, 0)
            timePickerDialogFor.setMaxTime(1, 0, 0)
            timePickerDialogFor.setAccentColor(Color.parseColor("#07a15b"))
            timePickerDialogFor.show(requireActivity().fragmentManager, "picker")
        }



        expTime1.setKeyListener(null)
        expTime2.setKeyListener(null)
        expTime3.setKeyListener(null)
        expTime4.setKeyListener(null)
        expTime5.setKeyListener(null)
        expTime6.setKeyListener(null)
        expTime7.setKeyListener(null)

        expLength1.setKeyListener(null)
        expLength2.setKeyListener(null)
        expLength3.setKeyListener(null)
        expLength4.setKeyListener(null)
        expLength5.setKeyListener(null)
        expLength6.setKeyListener(null)
        expLength7.setKeyListener(null)

        expEndTime1.setKeyListener(null)
        expEndTime2.setKeyListener(null)
        expEndTime3.setKeyListener(null)
        expEndTime4.setKeyListener(null)
        expEndTime5.setKeyListener(null)
        expEndTime6.setKeyListener(null)
        expEndTime7.setKeyListener(null)

        waitTime1.setKeyListener(null)
        waitTime2.setKeyListener(null)
        waitTime3.setKeyListener(null)
        waitTime4.setKeyListener(null)
        waitTime5.setKeyListener(null)
        waitTime6.setKeyListener(null)
        waitTime7.setKeyListener(null)


        expTime1.setOnClickListener(clickListener)
        expTime2.setOnClickListener(clickListener)
        expTime3.setOnClickListener(clickListener)
        expTime4.setOnClickListener(clickListener)
        expTime5.setOnClickListener(clickListener)
        expTime6.setOnClickListener(clickListener)
        expTime7.setOnClickListener(clickListener)

        expLength1.setOnClickListener(lclickListener)
        expLength2.setOnClickListener(lclickListener)
        expLength3.setOnClickListener(lclickListener)
        expLength4.setOnClickListener(lclickListener)
        expLength5.setOnClickListener(lclickListener)
        expLength6.setOnClickListener(lclickListener)
        expLength7.setOnClickListener(lclickListener)


        expEndTime1.setOnClickListener(eclickListener)
        expEndTime2.setOnClickListener(eclickListener)
        expEndTime3.setOnClickListener(eclickListener)
        expEndTime4.setOnClickListener(eclickListener)
        expEndTime5.setOnClickListener(eclickListener)
        expEndTime6.setOnClickListener(eclickListener)
        expEndTime7.setOnClickListener(eclickListener)

        waitTime1.setOnClickListener(wclickListener)
        waitTime2.setOnClickListener(wclickListener)
        waitTime3.setOnClickListener(wclickListener)
        waitTime4.setOnClickListener(wclickListener)
        waitTime5.setOnClickListener(wclickListener)
        waitTime6.setOnClickListener(wclickListener)
        waitTime7.setOnClickListener(wclickListener)


        val vapeTime1 = view.findViewById<EditText>(R.id.vapeTime1)
        val vapeTime2 = view.findViewById<EditText>(R.id.vapeTime2)
        val vapeTime3 = view.findViewById<EditText>(R.id.vapeTime3)
        val vapeTime4 = view.findViewById<EditText>(R.id.vapeTime4)
        val vapeTime5 = view.findViewById<EditText>(R.id.vapeTime5)
        val vapeTime6 = view.findViewById<EditText>(R.id.vapeTime6)
        val vapeTime7 = view.findViewById<EditText>(R.id.vapeTime7)

        val totalVapes1 = view.findViewById<EditText>(R.id.totalVapes1)
        val totalVapes2 = view.findViewById<EditText>(R.id.totalVapes2)
        val totalVapes3 = view.findViewById<EditText>(R.id.totalVapes3)
        val totalVapes4 = view.findViewById<EditText>(R.id.totalVapes4)
        val totalVapes5 = view.findViewById<EditText>(R.id.totalVapes5)
        val totalVapes6 = view.findViewById<EditText>(R.id.totalVapes6)
        val totalVapes7 = view.findViewById<EditText>(R.id.totalVapes7)

        val expCycle1 = view.findViewById<EditText>(R.id.expCycle1)
        val expCycle2 = view.findViewById<EditText>(R.id.expCycle2)
        val expCycle3 = view.findViewById<EditText>(R.id.expCycle3)
        val expCycle4 = view.findViewById<EditText>(R.id.expCycle4)
        val expCycle5 = view.findViewById<EditText>(R.id.expCycle5)
        val expCycle6 = view.findViewById<EditText>(R.id.expCycle6)
        val expCycle7 = view.findViewById<EditText>(R.id.expCycle7)

        /*expCycle1.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        expCycle2.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        expCycle3.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        expCycle4.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        expCycle5.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        expCycle6.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        expCycle7.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)*/


        val drugType1 = view.findViewById<Spinner>(R.id.drugType1)
        val drugType2 = view.findViewById<Spinner>(R.id.drugType2)
        val drugType3 = view.findViewById<Spinner>(R.id.drugType3)
        val drugType4 = view.findViewById<Spinner>(R.id.drugType4)
        val drugType5 = view.findViewById<Spinner>(R.id.drugType5)
        val drugType6 = view.findViewById<Spinner>(R.id.drugType6)
        val drugType7 = view.findViewById<Spinner>(R.id.drugType7)


        val drugCalc1 = view.findViewById<EditText>(R.id.drugCalc1)
        val drugCalc2 = view.findViewById<EditText>(R.id.drugCalc2)
        val drugCalc3 = view.findViewById<EditText>(R.id.drugCalc3)
        val drugCalc4 = view.findViewById<EditText>(R.id.drugCalc4)
        val drugCalc5 = view.findViewById<EditText>(R.id.drugCalc5)
        val drugCalc6 = view.findViewById<EditText>(R.id.drugCalc6)
        val drugCalc7 = view.findViewById<EditText>(R.id.drugCalc7)


        /* drugCalc1.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
         drugCalc2.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
         drugCalc3.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
         drugCalc4.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
         drugCalc5.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
         drugCalc6.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
         drugCalc7.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)*/

        val watts1 = view.findViewById<EditText>(R.id.watts1)
        val watts2 = view.findViewById<EditText>(R.id.watts2)
        val watts3 = view.findViewById<EditText>(R.id.watts3)
        val watts4 = view.findViewById<EditText>(R.id.watts4)
        val watts5 = view.findViewById<EditText>(R.id.watts5)
        val watts6 = view.findViewById<EditText>(R.id.watts6)
        val watts7 = view.findViewById<EditText>(R.id.watts7)

        /*watts1.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        watts2.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        watts3.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        watts4.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        watts5.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        watts6.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
        watts7.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)*/

        val temprature1 = view.findViewById<EditText>(R.id.temprature1)
        val temprature2 = view.findViewById<EditText>(R.id.temprature2)
        val temprature3 = view.findViewById<EditText>(R.id.temprature3)
        val temprature4 = view.findViewById<EditText>(R.id.temprature4)
        val temprature5 = view.findViewById<EditText>(R.id.temprature5)
        val temprature6 = view.findViewById<EditText>(R.id.temprature6)
        val temprature7 = view.findViewById<EditText>(R.id.temprature7)


        /*    temprature1.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
            temprature2.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
            temprature3.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
            temprature4.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
            temprature5.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
            temprature6.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
            temprature7.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
    */

        val drugConcetration1 = view.findViewById<EditText>(R.id.drugConcetration1)
        val drugConcetration2 = view.findViewById<EditText>(R.id.drugConcetration2)
        val drugConcetration3 = view.findViewById<EditText>(R.id.drugConcetration3)
        val drugConcetration4 = view.findViewById<EditText>(R.id.drugConcetration4)
        val drugConcetration5 = view.findViewById<EditText>(R.id.drugConcetration5)
        val drugConcetration6 = view.findViewById<EditText>(R.id.drugConcetration6)
        val drugConcetration7 = view.findViewById<EditText>(R.id.drugConcetration7)


        /*  drugConcetration1.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
          drugConcetration2.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
          drugConcetration3.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
          drugConcetration4.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
          drugConcetration5.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
          drugConcetration6.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
          drugConcetration7.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)*/


        val lungs1 = view.findViewById<EditText>(R.id.lungs1)
        val lungs2 = view.findViewById<EditText>(R.id.lungs2)
        val lungs3 = view.findViewById<EditText>(R.id.lungs3)
        val lungs4 = view.findViewById<EditText>(R.id.lungs4)
        val lungs5 = view.findViewById<EditText>(R.id.lungs5)
        val lungs6 = view.findViewById<EditText>(R.id.lungs6)
        val lungs7 = view.findViewById<EditText>(R.id.lungs7)

        /* lungs1.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
         lungs2.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
         lungs3.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
         lungs4.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
         lungs5.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
         lungs6.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
         lungs7.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)*/


        val expEndTime1 = view.findViewById<EditText>(R.id.expEndTime1)
        val expEndTime2 = view.findViewById<EditText>(R.id.expEndTime2)
        val expEndTime3 = view.findViewById<EditText>(R.id.expEndTime3)
        val expEndTime4 = view.findViewById<EditText>(R.id.expEndTime4)
        val expEndTime5 = view.findViewById<EditText>(R.id.expEndTime5)
        val expEndTime6 = view.findViewById<EditText>(R.id.expEndTime6)
        val expEndTime7 = view.findViewById<EditText>(R.id.expEndTime7)

        drugListName.clear()
        getDrugs()


        /*  btnSubmit.setOnClickListener {
              val expDate = arrayListOf<Any>()
              val expTime = arrayListOf<Any>()
              val expLength = arrayListOf<Any>()
              val vapeTime = arrayListOf<Any>()
              val totalVapes = arrayListOf<Any>()
              val expCycle = arrayListOf<Any>()
              val waitTime = arrayListOf<Any>()
              val drugType = arrayListOf<Any>()
              val drugCalc = arrayListOf<Any>()
              val watts = arrayListOf<Any>()
              val temprature = arrayListOf<Any>()
              val drugConcetration = arrayListOf<Any>()
              val lungs = arrayListOf<Any>()
              val expEndTime = arrayListOf<Any>()

              expDate.addAll(
                  listOf(
                      expDate1.getText().toString(),
                      expDate2.getText().toString(),
                      expDate3.getText().toString(),
                      expDate4.getText().toString(),
                      expDate5.getText().toString(),
                      expDate6.getText().toString(),
                      expDate7.getText().toString()
                  )
              )

              expTime.addAll(
                  listOf(
                      expTime1.getText().toString(),
                      expTime2.getText().toString(),
                      expTime3.getText().toString(),
                      expTime4.getText().toString(),
                      expTime5.getText().toString(),
                      expTime6.getText().toString(),
                      expTime7.getText().toString()
                  )
              )

              expLength.addAll(
                  listOf(
                      expLength1.getText().toString(),
                      expLength2.getText().toString(),
                      expLength3.getText().toString(),
                      expLength4.getText().toString(),
                      expLength5.getText().toString(),
                      expLength6.getText().toString(),
                      expLength7.getText().toString()
                  )
              )

              vapeTime.addAll(
                  listOf(
                      vapeTime1.getText().toString(),
                      vapeTime2.getText().toString(),
                      vapeTime3.getText().toString(),
                      vapeTime4.getText().toString(),
                      vapeTime5.getText().toString(),
                      vapeTime6.getText().toString(),
                      vapeTime7.getText().toString()
                  )
              )

              totalVapes.addAll(
                  listOf(
                      totalVapes1.getText().toString(),
                      totalVapes2.getText().toString(),
                      totalVapes3.getText().toString(),
                      totalVapes4.getText().toString(),
                      totalVapes5.getText().toString(),
                      totalVapes6.getText().toString(),
                      totalVapes7.getText().toString()
                  )
              )

              expCycle.addAll(
                  listOf(
                      expCycle1.getText().toString(),
                      expCycle2.getText().toString(),
                      expCycle3.getText().toString(),
                      expCycle4.getText().toString(),
                      expCycle5.getText().toString(),
                      expCycle6.getText().toString(),
                      expCycle7.getText().toString()
                  )
              )

              waitTime.addAll(
                  listOf(
                      waitTime1.getText().toString(),
                      waitTime2.getText().toString(),
                      waitTime3.getText().toString(),
                      waitTime4.getText().toString(),
                      waitTime5.getText().toString(),
                      waitTime6.getText().toString(),
                      waitTime7.getText().toString()
                  )
              )

              drugType.addAll(
                  listOf(
                      selectedDrug1,selectedDrug2,selectedDrug3,selectedDrug4,selectedDrug5,selectedDrug6,selectedDrug7
                  )
              )

              drugCalc.addAll(
                  listOf(
                      drugCalc1.getText().toString(),
                      drugCalc2.getText().toString(),
                      drugCalc3.getText().toString(),
                      drugCalc4.getText().toString(),
                      drugCalc5.getText().toString(),
                      drugCalc6.getText().toString(),
                      drugCalc7.getText().toString()
                  )
              )

              watts.addAll(
                  listOf(
                      watts1.getText().toString(),
                      watts2.getText().toString(),
                      watts3.getText().toString(),
                      watts4.getText().toString(),
                      watts5.getText().toString(),
                      watts6.getText().toString(),
                      watts7.getText().toString()
                  )
              )

              temprature.addAll(
                  listOf(
                      temprature1.getText().toString(),
                      temprature2.getText().toString(),
                      temprature3.getText().toString(),
                      temprature4.getText().toString(),
                      temprature5.getText().toString(),
                      temprature6.getText().toString(),
                      temprature7.getText().toString()
                  )
              )

              drugConcetration.addAll(
                  listOf(
                      drugConcetration1.getText().toString(),
                      drugConcetration2.getText().toString(),
                      drugConcetration3.getText().toString(),
                      drugConcetration4.getText().toString(),
                      drugConcetration5.getText().toString(),
                      drugConcetration6.getText().toString(),
                      drugConcetration7.getText().toString()
                  )
              )

              lungs.addAll(
                  listOf(
                      lungs1.getText().toString(),
                      lungs2.getText().toString(),
                      lungs3.getText().toString(),
                      lungs4.getText().toString(),
                      lungs5.getText().toString(),
                      lungs6.getText().toString(),
                      lungs7.getText().toString()
                  )
              )

              expEndTime.addAll(
                  listOf(
                      expEndTime1.getText().toString(),
                      expEndTime2.getText().toString(),
                      expEndTime3.getText().toString(),
                      expEndTime4.getText().toString(),
                      expEndTime5.getText().toString(),
                      expEndTime6.getText().toString(),
                      expEndTime7.getText().toString()
                  )
              )





              rejultObj1.addProperty("authToken", getAppPref().getString(AppConstant.KEY_TOKEN)!!);
              rejultObj1.addProperty("calDate", hdate0.toString());
              rejultObj1.addProperty("expDate", expDate.toString());
              rejultObj1.addProperty("expTime", expTime.toString());
              rejultObj1.addProperty("expLength", expLength.toString());
              rejultObj1.addProperty("vapeTime", vapeTime.toString());
              rejultObj1.addProperty("totalVapes", totalVapes.toString());
              rejultObj1.addProperty("expCycle", expCycle.toString());
              rejultObj1.addProperty("waitTime", waitTime.toString());
              rejultObj1.addProperty("drugType", selectedDrug1);
              rejultObj1.addProperty("drugCalc", drugCalc.toString());
              rejultObj1.addProperty("watts", watts.toString());
              rejultObj1.addProperty("temprature", temprature.toString());
              rejultObj1.addProperty("drugConcetration", drugConcetration.toString());
              rejultObj1.addProperty("lungs", lungs.toString());
              rejultObj1.addProperty("expEndTime", expEndTime.toString());


              val json: String = Gson().toJson(rejultObj1)


              Log.d("Response::::", json.toString())


              val jsonParser = JsonParser()
              val jsonObject = jsonParser.parse(json).asJsonObject



              RetrofitClient.instance!!.postExpRawJSON(jsonObject)?.enqueue(
                  object : Callback<ExpResultEditResponse> {
                      override fun onFailure(
                          call: Call<ExpResultEditResponse>,
                          t: Throwable
                      ) {
                          t.message?.let { it1 -> activity?.showToastMsg(it1, 2) }

                      }

                      override fun onResponse(
                          call: Call<ExpResultEditResponse>,
                          response: Response<ExpResultEditResponse>
                      ) {
                          if (response.body()!!.status_code == AppConstant.CODE_SUCCESS) {

                              //calculateResult.setEnabled(true);
                              getAppPref().setString("expDates", expDate.toString())
                              getAppPref().setString("experimentId", response.body()!!.experId)

                              experimentId = response.body()!!.experId;

                              // findNavController().navigate(R.id.action_distressExperimentSetupFragment_to_distressScenarioStep3)
                              activity?.showToastMsg(response.body()!!.message, 1)
                              // Log.i("app:retro:service", "onResponse true ${response.body()!!.toString()}")
                          } else {
                              activity?.showToastMsg(response.body()!!.message, 2)
                          }
                      }
                  }
              )
              Log.d("Response::::", jsonObject.toString())

          }*/
        btnDayseven.setOnClickListener {
            when {
                expDate7.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp Date of Day " + hday7, 2)
                }
                expTime7.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp Start Time of Day " + hday7, 2)
                }
                expLength7.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp Length of Day " + hday7, 2)
                }
                vapeTime7.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Vape Time of Day " + hday7, 2)
                }
                totalVapes7.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Total Vapes of Day " + hday7, 2)
                }
                expCycle7.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter # Hits/Cycle of Day " + hday7, 2)
                }
                waitTime7.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Wait Time of Day " + hday7, 2)
                }
                selectedDrug7.trim().isEmpty() ||  selectedDrug7.trim().equals("Select Drug")-> {
                    activity?.showToastMsg("Please Enter Drug Type of Day " + hday7, 2)
                }
                drugCalc7.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Drug Calc of Day " + hday7, 2)
                }
                Integer.parseInt(drugCalc7.text.toString()) > 100000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_drugCalc_limit) + " of Day " + hday7,
                        2
                    )
                }
                watts7.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Watts of Day " + hday7, 2)
                }
                Integer.parseInt(watts7.text.toString()) > 200 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_watts_limit) + " of Day " + hday7,
                        2
                    )
                }
                temprature7.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Temperature of Day " + hday7, 2)
                }
                Integer.parseInt(temprature7.text.toString()) < 100 || Integer.parseInt(
                    temprature7.text.toString()
                ) > 1000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_temprature_limit) + " of Day " + hday7,
                        2
                    )
                }
                drugConcetration7.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Drug Concentration of Day " + hday7, 2)
                }
                Integer.parseInt(drugConcetration7.text.toString()) > 1000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_drugConcetration_limit) + " of Day " + hday7,
                        2
                    )
                }
                lungs7.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg(
                        "Please Enter # of mL that remain in the lungs of Day " + hday7,
                        2
                    )
                }
                Integer.parseInt(lungs7.text.toString()) > 10000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_lungs_limit) + " of Day " + hday7,
                        2
                    )
                }
                expEndTime7.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp End Time of Day " + hday7, 2)
                }
                else -> {
                    dayvalue = "7"
                    daytype = expDate7.text.toString()
                    AppAlertDialog().showPassword(view, this, getActivity())
                }
            }
        }
        btnDaysix.setOnClickListener {
            when {
                expDate6.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp Date of Day " + hday6, 2)
                }
                expTime6.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp Start Time of Day " + hday6, 2)
                }
                expLength6.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp Length of Day " + hday6, 2)
                }
                vapeTime6.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Vape Time of Day " + hday6, 2)
                }
                totalVapes6.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Total Vapes of Day " + hday6, 2)
                }
                expCycle6.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter # Hits/Cycle of Day " + hday6, 2)
                }
                waitTime6.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Wait Time of Day " + hday6, 2)
                }
                selectedDrug6.trim().isEmpty() ||  selectedDrug6.trim().equals("Select Drug")-> {
                    activity?.showToastMsg("Please Enter Drug Type of Day " + hday6, 2)
                }
                drugCalc6.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Drug Calc of Day " + hday6, 2)
                }
                Integer.parseInt(drugCalc6.text.toString()) > 100000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_drugCalc_limit) + " of Day " + hday6,
                        2
                    )
                }
                watts6.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Watts of Day " + hday6, 2)
                }
                Integer.parseInt(watts6.text.toString()) > 200 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_watts_limit) + " of Day " + hday6,
                        2
                    )
                }
                temprature6.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Temperature of Day " + hday6, 2)
                }
                Integer.parseInt(temprature6.text.toString()) < 100 || Integer.parseInt(
                    temprature6.text.toString()
                ) > 1000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_temprature_limit) + " of Day " + hday6,
                        2
                    )
                }
                drugConcetration6.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Drug Concentration of Day " + hday6, 2)
                }
                Integer.parseInt(drugConcetration6.text.toString()) > 1000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_drugConcetration_limit) + " of Day " + hday6,
                        2
                    )
                }
                lungs6.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg(
                        "Please Enter # of mL that remain in the lungs of Day " + hday6,
                        2
                    )
                }
                Integer.parseInt(lungs6.text.toString()) > 10000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_lungs_limit) + " of Day " + hday6,
                        2
                    )
                }
                expEndTime6.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp End Time of Day " + hday6, 2)
                }

                else -> {
                    dayvalue = "6"
                    daytype = expDate6.text.toString()
                    AppAlertDialog().showPassword(view, this, getActivity())
                }
            }
        }
        btnDayfive.setOnClickListener {
            when {
                expDate5.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp Date of Day " + hday5, 2)
                }
                expTime5.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp Start Time of Day " + hday5, 2)
                }
                expLength5.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp Length of Day " + hday5, 2)
                }
                vapeTime5.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Vape Time of Day " + hday5, 2)
                }
                totalVapes5.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Total Vapes of Day " + hday5, 2)
                }
                expCycle5.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter # Hits/Cycle of Day " + hday5, 2)
                }
                waitTime5.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Wait Time of Day " + hday5, 2)
                }
                selectedDrug5.trim().isEmpty() ||  selectedDrug5.trim().equals("Select Drug")-> {
                    activity?.showToastMsg("Please Enter Drug Type of Day " + hday5, 2)
                }
                drugCalc5.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Drug Calc of Day " + hday5, 2)
                }
                Integer.parseInt(drugCalc5.text.toString()) > 100000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_drugCalc_limit) + " of Day " + hday6,
                        2
                    )
                }
                watts5.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Watts of Day " + hday5, 2)
                }
                Integer.parseInt(watts5.text.toString()) > 200 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_watts_limit) + " of Day " + hday5,
                        2
                    )
                }
                temprature5.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Temperature of Day " + hday5, 2)
                }
                Integer.parseInt(temprature5.text.toString()) < 100 || Integer.parseInt(
                    temprature5.text.toString()
                ) > 1000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_temprature_limit) + " of Day " + hday5,
                        2
                    )
                }
                drugConcetration5.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Drug Concentration of Day " + hday5, 2)
                }
                Integer.parseInt(drugConcetration5.text.toString()) > 1000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_drugConcetration_limit) + " of Day " + hday5,
                        2
                    )
                }
                lungs5.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg(
                        "Please Enter # of mL that remain in the lungs of Day " + hday5,
                        2
                    )
                }
                Integer.parseInt(lungs5.text.toString()) > 10000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_lungs_limit) + " of Day " + hday5,
                        2
                    )
                }
                expEndTime5.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp End Time of Day " + hday5, 2)
                }

                else -> {
                    dayvalue = "5"
                    daytype = expDate5.text.toString()
                    AppAlertDialog().showPassword(view, this, getActivity())
                }
            }
        }
        btnDayfour.setOnClickListener {
            when {
                expDate4.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp Date of Day " + hday4, 2)
                }
                expTime4.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp Start Time of Day " + hday4, 2)
                }
                expLength4.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp Length of Day " + hday4, 2)
                }
                vapeTime4.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Vape Time of Day " + hday4, 2)
                }
                totalVapes4.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Total Vapes of Day " + hday4, 2)
                }
                expCycle4.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter # Hits/Cycle of Day " + hday4, 2)
                }
                waitTime4.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Wait Time of Day " + hday4, 2)
                }
                selectedDrug4.trim().isEmpty()||  selectedDrug4.trim().equals("Select Drug") -> {
                    activity?.showToastMsg("Please Enter Drug Type of Day " + hday4, 2)
                }
                drugCalc4.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Drug Calc of Day " + hday4, 2)
                }
                Integer.parseInt(drugCalc4.text.toString()) > 100000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_drugCalc_limit) + " of Day " + hday4,
                        2
                    )
                }
                watts4.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Watts of Day " + hday4, 2)
                }
                Integer.parseInt(watts4.text.toString()) > 200 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_watts_limit) + " of Day " + hday4,
                        2
                    )
                }
                temprature4.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Temperature of Day " + hday4, 2)
                }
                Integer.parseInt(temprature4.text.toString()) < 100 || Integer.parseInt(
                    temprature4.text.toString()
                ) > 1000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_temprature_limit) + " of Day " + hday4,
                        2
                    )
                }
                drugConcetration4.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Drug Concentration of Day " + hday4, 2)
                }
                Integer.parseInt(drugConcetration4.text.toString()) > 1000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_drugConcetration_limit) + " of Day " + hday4,
                        2
                    )
                }
                lungs4.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg(
                        "Please Enter # of mL that remain in the lungs of Day " + hday4,
                        2
                    )
                }
                Integer.parseInt(lungs4.text.toString()) > 10000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_lungs_limit) + " of Day " + hday4,
                        2
                    )
                }
                expEndTime4.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp End Time of Day " + hday4, 2)
                }

                else -> {
                    dayvalue = "4"
                    daytype = expDate4.text.toString()
                    AppAlertDialog().showPassword(view, this, getActivity())
                }
            }
        }
        btnDaythree.setOnClickListener {
            when {
                expDate3.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp Date of Day " + hday3, 2)
                }
                expTime3.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp Start Time of Day " + hday3, 2)
                }
                expLength3.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp Length of Day " + hday3, 2)
                }
                vapeTime3.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Vape Time of Day " + hday3, 2)
                }
                totalVapes3.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Total Vapes of Day " + hday3, 2)
                }
                expCycle3.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter # Hits/Cycle of Day " + hday3, 2)
                }
                waitTime3.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Wait Time of Day " + hday3, 2)
                }
                selectedDrug3.trim().isEmpty()||  selectedDrug3.trim().equals("Select Drug") -> {
                    activity?.showToastMsg("Please Enter Drug Type of Day " + hday3, 2)
                }
                drugCalc3.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Drug Calc of Day " + hday3, 2)
                }
                Integer.parseInt(drugCalc3.text.toString()) > 100000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_drugCalc_limit) + " of Day " + hday3,
                        2
                    )
                }
                watts3.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Watts of Day " + hday3, 2)
                }
                Integer.parseInt(watts3.text.toString()) > 200 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_watts_limit) + " of Day " + hday3,
                        2
                    )
                }
                temprature3.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Temperature of Day " + hday3, 2)
                }
                Integer.parseInt(temprature3.text.toString()) < 100 || Integer.parseInt(
                    temprature3.text.toString()
                ) > 1000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_temprature_limit) + " of Day " + hday3,
                        2
                    )
                }
                drugConcetration3.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Drug Concentration of Day " + hday3, 2)
                }
                Integer.parseInt(drugConcetration3.text.toString()) > 1000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_drugConcetration_limit) + " of Day " + hday3,
                        2
                    )
                }
                lungs3.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg(
                        "Please Enter # of mL that remain in the lungs of Day " + hday3,
                        2
                    )
                }
                Integer.parseInt(lungs3.text.toString()) > 10000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_lungs_limit) + " of Day " + hday3,
                        2
                    )
                }
                expEndTime3.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp End Time of Day " + hday3, 2)
                }

                else -> {
                    dayvalue = "3"
                    daytype = expDate3.text.toString()
                    AppAlertDialog().showPassword(view, this, getActivity())
                }
            }
        }
        btnDaytwo.setOnClickListener {
            when {
                expDate2.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp Date of Day " + hday2, 2)
                }
                expTime2.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp Start Time of Day " + hday2, 2)
                }
                expLength2.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp Length of Day " + hday2, 2)
                }
                vapeTime2.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Vape Time of Day " + hday2, 2)
                }
                totalVapes2.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Total Vapes of Day " + hday2, 2)
                }
                expCycle2.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter # Hits/Cycle of Day " + hday2, 2)
                }
                waitTime2.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Wait Time of Day " + hday2, 2)
                }
                selectedDrug2.trim().isEmpty()||  selectedDrug2.trim().equals("Select Drug") -> {
                    activity?.showToastMsg("Please Enter Drug Type of Day " + hday2, 2)
                }
                drugCalc2.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Drug Calc of Day " + hday2, 2)
                }
                Integer.parseInt(drugCalc2.text.toString()) > 100000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_drugCalc_limit) + " of Day " + hday2,
                        2
                    )
                }
                watts2.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Watts of Day " + hday2, 2)
                }
                Integer.parseInt(watts2.text.toString()) > 200 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_watts_limit) + " of Day " + hday2,
                        2
                    )
                }
                temprature2.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Temperature of Day " + hday2, 2)
                }
                Integer.parseInt(temprature2.text.toString()) < 100 || Integer.parseInt(
                    temprature2.text.toString()
                ) > 1000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_temprature_limit) + " of Day " + hday2,
                        2
                    )
                }
                drugConcetration2.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Drug Concentration of Day " + hday2, 2)
                }
                Integer.parseInt(drugConcetration2.text.toString()) > 1000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_drugConcetration_limit) + " of Day " + hday2,
                        2
                    )
                }
                lungs2.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg(
                        "Please Enter # of mL that remain in the lungs of Day " + hday2,
                        2
                    )
                }
                Integer.parseInt(lungs2.text.toString()) > 10000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_lungs_limit) + " of Day " + hday2,
                        2
                    )
                }
                expEndTime2.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp End Time of Day " + hday2, 2)
                }

                else -> {
                    dayvalue = "2"
                    daytype = expDate2.text.toString()
                    AppAlertDialog().showPassword(view, this, getActivity())
                }
            }
        }
        btnDayOne.setOnClickListener {
            when {
                expDate1.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp Date of Day " + hday1, 2)
                }
                expTime1.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp Start Time of Day " + hday1, 2)
                }
                expLength1.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp Length of Day " + hday1, 2)
                }
                vapeTime1.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Vape Time of Day " + hday1, 2)
                }
                totalVapes1.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Total Vapes of Day " + hday1, 2)
                }
                expCycle1.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter # Hits/Cycle of Day " + hday1, 2)
                }
                waitTime1.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Wait Time of Day " + hday1, 2)
                }
                selectedDrug1.trim().isEmpty() ||  selectedDrug1.trim().equals("Select Drug")-> {
                    activity?.showToastMsg("Please Enter Drug Type of Day " + hday1, 2)
                }
                drugCalc1.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Drug Calc of Day " + hday1, 2)
                }
                Integer.parseInt(drugCalc1.text.toString()) > 100000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_drugCalc_limit) + " of Day " + hday1,
                        2
                    )
                }
                watts1.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Watts of Day " + hday1, 2)
                }
                Integer.parseInt(watts1.text.toString()) > 200 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_watts_limit) + " of Day " + hday1,
                        2
                    )
                }
                temprature1.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Temperature of Day " + hday1, 2)
                }
                Integer.parseInt(temprature1.text.toString()) < 100 || Integer.parseInt(
                    temprature1.text.toString()
                ) > 1000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_temprature_limit) + " of Day " + hday1,
                        2
                    )
                }
                drugConcetration1.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Drug Concentration of Day " + hday1, 2)
                }
                Integer.parseInt(drugConcetration1.text.toString()) > 1000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_drugConcetration_limit) + " of Day " + hday1,
                        2
                    )
                }
                lungs1.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg(
                        "Please Enter # of mL that remain in the lungs of Day " + hday1,
                        2
                    )
                }
                Integer.parseInt(lungs1.text.toString()) > 10000 -> {
                    activity?.showToastMsg(
                        getString(R.string.error_lungs_limit) + " of Day " + hday1,
                        2
                    )
                }
                expEndTime1.text!!.trim().isEmpty() -> {
                    activity?.showToastMsg("Please Enter Exp End Time of Day " + hday1, 2)
                }

                else -> {
                    dayvalue = "1"
                    daytype = expDate1.text.toString()
                    AppAlertDialog().showPassword(view, this, getActivity())
                }
            }
        }
        calculateResult.setOnClickListener {
            /*getAppPref().setString("selDate", daytype)
            getAppPref().setString("selectedweek", daytype)*/
            getAppPref().setString("selData", expDate1.text.toString())
            getAppPref().setString("experimentId", experimentId)
            val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = requireActivity().currentFocus
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            findNavController().navigate(R.id.action_calculatedResultFragment_to_expResultWeeklyFragment)
        }
    }

    private var drugList = arrayListOf<DrugList.Data>()
    private var drugListName = arrayListOf<String>()
    var selectedDrug1: String = "Select Drug"
    var selectedDrug2: String = "Select Drug"
    var selectedDrug3: String = "Select Drug"
    var selectedDrug4: String = "Select Drug"
    var selectedDrug5: String = "Select Drug"
    var selectedDrug6: String = "Select Drug"
    var selectedDrug7: String = "Select Drug"
    lateinit var dadapter: ArrayAdapter<String>
    private fun getDrugs() {
        RetrofitClient.instance!!.getDrugList().enqueue(
            object : Callback<DrugList> {
                override fun onFailure(call: Call<DrugList>, t: Throwable) {
                    Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<DrugList>, response: Response<DrugList>) {
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
                        drugType1.setAdapter(dadapter)
                        drugType2.setAdapter(dadapter)
                        drugType3.setAdapter(dadapter)
                        drugType4.setAdapter(dadapter)
                        drugType5.setAdapter(dadapter)
                        drugType6.setAdapter(dadapter)
                        drugType7.setAdapter(dadapter)
                        drugType1.setOnItemSelectedListener(object :
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
                                    selectedDrug1 = parent.selectedItem as String
                                } catch (e: Exception) {
                                }
                            }
                        })
                        drugType2.setOnItemSelectedListener(object :
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
                                    selectedDrug2 = parent.selectedItem as String
                                } catch (e: Exception) {
                                }
                            }
                        })
                        drugType3.setOnItemSelectedListener(object :
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
                                    selectedDrug3 = parent.selectedItem as String
                                } catch (e: Exception) {
                                }
                            }
                        })
                        drugType4.setOnItemSelectedListener(object :
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
                                    selectedDrug4 = parent.selectedItem as String
                                } catch (e: Exception) {
                                }
                            }
                        })
                        drugType5.setOnItemSelectedListener(object :
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
                                    selectedDrug5 = parent.selectedItem as String
                                } catch (e: Exception) {
                                }
                            }
                        })
                        drugType6.setOnItemSelectedListener(object :
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
                                    selectedDrug6 = parent.selectedItem as String
                                } catch (e: Exception) {
                                }
                            }
                        })
                        drugType7.setOnItemSelectedListener(object :
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
                                    selectedDrug7 = parent.selectedItem as String
                                } catch (e: Exception) {
                                }
                            }
                        })
                        getData()
                    } else {
                        activity!!.responseHandler(
                            response.body()!!.status_code,
                            response.body()!!.message
                        )
                    }
                }
            })
    }

    public fun getData() {
        if (hdate0 != null) {
            RetrofitClient.instance!!.fetchExpResult(
                getAppPref().getString(AppConstant.KEY_TOKEN)!!, hdate1.toString()

            ).enqueue(object : Callback<ExpResultResponse> {
                override fun onFailure(call: Call<ExpResultResponse>, t: Throwable) {
                    activity?.showToastMsg(t.message!!, 2)
                }

                override fun onResponse(
                    call: Call<ExpResultResponse>,
                    response: Response<ExpResultResponse>
                ) {


                    val jsonArray = JSONArray(response.body()!!.data)

                    var exData = response.body()!!.data;

                    var totaldata = jsonArray.length()

                    if (response.body()!!.status_code == AppConstant.CODE_SUCCESS) {

                        Log.d("Response::::", response.body()!!.message.toString())
                        if (response.body()!!.message.equals("1")) {
                            calculateResult.setEnabled(true);
                        } else {
                            calculateResult.setEnabled(false);
                        }
                        for (i in 0 until jsonArray.length()) {
                            val id = exData?.get(i)?.id
                            val userId = exData?.get(i)?.userId
                            val expId = exData?.get(i)?.expId
                            val labelId = exData?.get(i)?.labelId
                            val startDate = exData?.get(i)?.startDate
                            val day_1 = exData?.get(i)?.day_1
                            val day_2 = exData?.get(i)?.day_2
                            val day_3 = exData?.get(i)?.day_3
                            val day_4 = exData?.get(i)?.day_4
                            val day_5 = exData?.get(i)?.day_5
                            val day_6 = exData?.get(i)?.day_6
                            val day_7 = exData?.get(i)?.day_7

                            if (expId != null) {
                                experimentId = expId
                            }

                            if (labelId == "1") {

                            }
                            if (labelId == "2") { /// Exp Time
                                expTime1.setText(day_1)
                                expTime2.setText(day_2)
                                expTime3.setText(day_3)
                                expTime4.setText(day_4)
                                expTime5.setText(day_5)
                                expTime6.setText(day_6)
                                expTime7.setText(day_7)
                            }

                            if (labelId == "3") { /// Exp Length
                                expLength1.setText(day_1)
                                expLength2.setText(day_2)
                                expLength3.setText(day_3)
                                expLength4.setText(day_4)
                                expLength5.setText(day_5)
                                expLength6.setText(day_6)
                                expLength7.setText(day_7)
                            }
                            if (labelId == "4") { /// Vape Time
                                vapeTime1.setText(day_1)
                                vapeTime2.setText(day_2)
                                vapeTime3.setText(day_3)
                                vapeTime4.setText(day_4)
                                vapeTime5.setText(day_5)
                                vapeTime6.setText(day_6)
                                vapeTime7.setText(day_7)
                            }

                            if (labelId == "5") { /// Total Vapes
                                totalVapes1.setText(day_1)
                                totalVapes2.setText(day_2)
                                totalVapes3.setText(day_3)
                                totalVapes4.setText(day_4)
                                totalVapes5.setText(day_5)
                                totalVapes6.setText(day_6)
                                totalVapes7.setText(day_7)
                            }

                            if (labelId == "6") { /// Exp Cycles
                                expCycle1.setText(day_1)
                                expCycle2.setText(day_2)
                                expCycle3.setText(day_3)
                                expCycle4.setText(day_4)
                                expCycle5.setText(day_5)
                                expCycle6.setText(day_6)
                                expCycle7.setText(day_7)
                            }

                            if (labelId == "7") { /// Wait Time
                                waitTime1.setText(day_1)
                                waitTime2.setText(day_2)
                                waitTime3.setText(day_3)
                                waitTime4.setText(day_4)
                                waitTime5.setText(day_5)
                                waitTime6.setText(day_6)
                                waitTime7.setText(day_7)
                            }

                            if (labelId == "8") { /// Drug Type
                                drugType1.setSelection(dadapter.getPosition(day_1))
                                drugType2.setSelection(dadapter.getPosition(day_2))
                                drugType3.setSelection(dadapter.getPosition(day_3))
                                drugType4.setSelection(dadapter.getPosition(day_4))
                                drugType5.setSelection(dadapter.getPosition(day_5))
                                drugType6.setSelection(dadapter.getPosition(day_6))
                                drugType7.setSelection(dadapter.getPosition(day_7))
                            }

                            if (labelId == "9") { /// Drug Calc
                                if (day_1!!.isDigitsOnly())
                                    drugCalc1.setText(day_1)
                                else
                                    drugCalc1.setText("")
                                if (day_2!!.isDigitsOnly())
                                    drugCalc2.setText(day_2)
                                else
                                    drugCalc2.setText("")
                                if (day_3!!.isDigitsOnly())
                                    drugCalc3.setText(day_3)
                                else
                                    drugCalc3.setText("")
                                if (day_4!!.isDigitsOnly())
                                    drugCalc4.setText(day_4)
                                else
                                    drugCalc4.setText("")
                                if (day_5!!.isDigitsOnly())
                                    drugCalc5.setText(day_5)
                                else
                                    drugCalc5.setText("")
                                if (day_6!!.isDigitsOnly())
                                    drugCalc6.setText(day_6)
                                else
                                    drugCalc6.setText("")
                                if (day_7!!.isDigitsOnly())
                                    drugCalc7.setText(day_7)
                                else
                                    drugCalc7.setText("")
                            }

                            if (labelId == "10") { /// Watts
                                if (day_1!!.isDigitsOnly())
                                    watts1.setText(day_1)
                                else
                                    watts1.setText("")
                                if (day_2!!.isDigitsOnly())
                                    watts2.setText(day_2)
                                else
                                    watts2.setText("")
                                if (day_3!!.isDigitsOnly())
                                    watts3.setText(day_3)
                                else
                                    watts3.setText("")
                                if (day_4!!.isDigitsOnly())
                                    watts4.setText(day_4)
                                else
                                    watts4.setText("")
                                if (day_5!!.isDigitsOnly())
                                    watts5.setText(day_5)
                                else
                                    watts5.setText("")
                                if (day_6!!.isDigitsOnly())
                                    watts6.setText(day_6)
                                else
                                    watts6.setText("")
                                if (day_7!!.isDigitsOnly())
                                    watts7.setText(day_7)
                                else
                                    watts7.setText("")
                            }

                            if (labelId == "11") { /// Temperature
                                if (day_1!!.isDigitsOnly())
                                    temprature1.setText(day_1)
                                else
                                    temprature1.setText("")
                                if (day_2!!.isDigitsOnly())
                                    temprature2.setText(day_2)
                                else
                                    temprature2.setText("")
                                if (day_3!!.isDigitsOnly())
                                    temprature3.setText(day_3)
                                else
                                    temprature3.setText("")
                                if (day_4!!.isDigitsOnly())
                                    temprature4.setText(day_4)
                                else
                                    temprature4.setText("")
                                if (day_5!!.isDigitsOnly())
                                    temprature5.setText(day_5)
                                else
                                    temprature5.setText("")
                                if (day_6!!.isDigitsOnly())
                                    temprature6.setText(day_6)
                                else
                                    temprature6.setText("")
                                if (day_7!!.isDigitsOnly())
                                    temprature7.setText(day_7)
                                else
                                    temprature7.setText("")
                            }

                            if (labelId == "12") { /// Drug Concentration
                                if (day_1!!.isDigitsOnly())
                                    drugConcetration1.setText(day_1)
                                else
                                    drugConcetration1.setText("")
                                if (day_2!!.isDigitsOnly())
                                    drugConcetration2.setText(day_2)
                                else
                                    drugConcetration2.setText("")
                                if (day_3!!.isDigitsOnly())
                                    drugConcetration3.setText(day_3)
                                else
                                    drugConcetration3.setText("")
                                if (day_4!!.isDigitsOnly())
                                    drugConcetration4.setText(day_4)
                                else
                                    drugConcetration4.setText("")
                                if (day_5!!.isDigitsOnly())
                                    drugConcetration5.setText(day_5)
                                else
                                    drugConcetration5.setText("")
                                if (day_6!!.isDigitsOnly())
                                    drugConcetration6.setText(day_6)
                                else
                                    drugConcetration6.setText("")
                                if (day_7!!.isDigitsOnly())
                                    drugConcetration7.setText(day_7)
                                else
                                    drugConcetration7.setText("")
                            }

                            if (labelId == "13") { /// Lungs
                                if (day_1!!.isDigitsOnly())
                                    lungs1.setText(day_1)
                                else
                                    lungs1.setText("")
                                if (day_2!!.isDigitsOnly())
                                    lungs2.setText(day_2)
                                else
                                    lungs2.setText("")
                                if (day_3!!.isDigitsOnly())
                                    lungs3.setText(day_3)
                                else
                                    lungs3.setText("")
                                if (day_4!!.isDigitsOnly())
                                    lungs4.setText(day_4)
                                else
                                    lungs4.setText("")
                                if (day_5!!.isDigitsOnly())
                                    lungs5.setText(day_5)
                                else
                                    lungs5.setText("")
                                if (day_6!!.isDigitsOnly())
                                    lungs6.setText(day_6)
                                else
                                    lungs6.setText("")
                                if (day_7!!.isDigitsOnly())
                                    lungs7.setText(day_7)
                                else
                                    lungs7.setText("")
                            }
                            if (labelId == "14") { /// Exp End Time
                                expEndTime1.setText(day_1)
                                expEndTime2.setText(day_2)
                                expEndTime3.setText(day_3)
                                expEndTime4.setText(day_4)
                                expEndTime5.setText(day_5)
                                expEndTime6.setText(day_6)
                                expEndTime7.setText(day_7)
                            }
                        }
                        Log.d("Response::::", response.body()!!.data.toString())

                    } else if (response.body()!!.status_code == AppConstant.ERROR) {
                        activity?.logoutUser(activity!!, response.body()!!.message)
                    }
                }
            })
        }
    }


    var daytype: String = ""
    var dayvalue: String = ""
    override fun response(response: Response<CalLoginDistressResponse>, type: Boolean) {
        super.response(response, type)

        if (type == true) {
            if (dayvalue.equals("1")) {
                calculateDay(
                    getAppPref().getString(AppConstant.KEY_TOKEN),
                    dayvalue,
                    hdate0,
                    expDate1.text.toString(),
                    expTime1.text.toString(),
                    expLength1.text.toString(),
                    vapeTime1.text.toString(),
                    totalVapes1.text.toString(),
                    expCycle1.text.toString(),
                    waitTime1.text.toString(),
                    selectedDrug1.toString(),
                    drugCalc1.text.toString(),
                    watts1.text.toString(),
                    temprature1.text.toString(),
                    drugConcetration1.text.toString(),
                    lungs1.text.toString()
                    ,
                    expEndTime1.text.toString()
                )
            }
            if (dayvalue.equals("2")) {
                calculateDay(
                    getAppPref().getString(AppConstant.KEY_TOKEN),
                    dayvalue,
                    hdate0,
                    expDate2.text.toString(),
                    expTime2.text.toString(),
                    expLength2.text.toString(),
                    vapeTime2.text.toString(),
                    totalVapes2.text.toString(),
                    expCycle2.text.toString(),
                    waitTime2.text.toString(),
                    selectedDrug2.toString(),
                    drugCalc2.text.toString(),
                    watts2.text.toString(),
                    temprature2.text.toString(),
                    drugConcetration2.text.toString(),
                    lungs2.text.toString()
                    ,
                    expEndTime2.text.toString()
                )
            }
            if (dayvalue.equals("3")) {
                calculateDay(
                    getAppPref().getString(AppConstant.KEY_TOKEN),
                    dayvalue,
                    hdate0,
                    expDate3.text.toString(),
                    expTime3.text.toString(),
                    expLength3.text.toString(),
                    vapeTime3.text.toString(),
                    totalVapes3.text.toString(),
                    expCycle3.text.toString(),
                    waitTime3.text.toString(),
                    selectedDrug3.toString(),
                    drugCalc3.text.toString(),
                    watts3.text.toString(),
                    temprature3.text.toString(),
                    drugConcetration3.text.toString(),
                    lungs3.text.toString()
                    ,
                    expEndTime3.text.toString()
                )
            }
            if (dayvalue.equals("4")) {
                calculateDay(
                    getAppPref().getString(AppConstant.KEY_TOKEN),
                    dayvalue,
                    hdate0,
                    expDate4.text.toString(),
                    expTime4.text.toString(),
                    expLength4.text.toString(),
                    vapeTime4.text.toString(),
                    totalVapes4.text.toString(),
                    expCycle4.text.toString(),
                    waitTime4.text.toString(),
                    selectedDrug4.toString(),
                    drugCalc4.text.toString(),
                    watts4.text.toString(),
                    temprature4.text.toString(),
                    drugConcetration4.text.toString(),
                    lungs4.text.toString()
                    ,
                    expEndTime4.text.toString()
                )
            }
            if (dayvalue.equals("5")) {
                calculateDay(
                    getAppPref().getString(AppConstant.KEY_TOKEN),
                    dayvalue,
                    hdate0,
                    expDate5.text.toString(),
                    expTime5.text.toString(),
                    expLength5.text.toString(),
                    vapeTime5.text.toString(),
                    totalVapes5.text.toString(),
                    expCycle5.text.toString(),
                    waitTime5.text.toString(),
                    selectedDrug5.toString(),
                    drugCalc5.text.toString(),
                    watts5.text.toString(),
                    temprature5.text.toString(),
                    drugConcetration5.text.toString(),
                    lungs5.text.toString()
                    ,
                    expEndTime5.text.toString()
                )
            }
            if (dayvalue.equals("6")) {
                calculateDay(
                    getAppPref().getString(AppConstant.KEY_TOKEN),
                    dayvalue,
                    hdate0,
                    expDate6.text.toString(),
                    expTime6.text.toString(),
                    expLength6.text.toString(),
                    vapeTime6.text.toString(),
                    totalVapes6.text.toString(),
                    expCycle6.text.toString(),
                    waitTime6.text.toString(),
                    selectedDrug6.toString(),
                    drugCalc6.text.toString(),
                    watts6.text.toString(),
                    temprature6.text.toString(),
                    drugConcetration6.text.toString(),
                    lungs6.text.toString()
                    ,
                    expEndTime6.text.toString()
                )
            }
            if (dayvalue.equals("7")) {
                calculateDay(
                    getAppPref().getString(AppConstant.KEY_TOKEN),
                    dayvalue,
                    hdate0,
                    expDate7.text.toString(),
                    expTime7.text.toString(),
                    expLength7.text.toString(),
                    vapeTime7.text.toString(),
                    totalVapes7.text.toString(),
                    expCycle7.text.toString(),
                    waitTime7.text.toString(),
                    selectedDrug7,
                    drugCalc7.text.toString(),
                    watts7.text.toString(),
                    temprature7.text.toString(),
                    drugConcetration7.text.toString(),
                    lungs7.text.toString(),
                    expEndTime7.text.toString()
                )
            }
        } else {
            activity?.showToastMsg(response.body()!!.message, 2)
        }
    }


    fun calculateDay(
        token: String?,
        day: String,
        hdate: String?,
        expDate: String?,
        expTime: String,
        expLength: String,
        vapeTime: String,
        totalVapes: String,
        expCycle: String,
        waitTime: String,
        drugType: String,
        drugCalc: String,
        watts: String,
        temprature: String,
        drugConcetration: String,
        lungs: String,
        expEndTime: String
    ) {
        rejultObj1.addProperty("authToken", token!!)
        rejultObj1.addProperty("calDate", hdate)
        rejultObj1.addProperty("dayNo", day)
        rejultObj1.addProperty("expDate", expDate)
        rejultObj1.addProperty("expTime", expTime)
        rejultObj1.addProperty("expLength", expLength)
        rejultObj1.addProperty("vapeTime", vapeTime)
        rejultObj1.addProperty("totalVapes", totalVapes)
        rejultObj1.addProperty("expCycle", expCycle)
        rejultObj1.addProperty("waitTime", waitTime)
        rejultObj1.addProperty("drugType", drugType)
        rejultObj1.addProperty("drugCalc", drugCalc)
        rejultObj1.addProperty("watts", watts)
        rejultObj1.addProperty("temprature", temprature)
        rejultObj1.addProperty("drugConcetration", drugConcetration)
        rejultObj1.addProperty("lungs", lungs)
        rejultObj1.addProperty("expEndTime", expEndTime)
        val json: String = Gson().toJson(rejultObj1)
        val jsonParser = JsonParser()
        val jsonObject = jsonParser.parse(json).asJsonObject

        RetrofitClient.instance!!.postExpResultJSON(jsonObject)?.enqueue(
            object : Callback<ExpResultEditResponse> {
                override fun onFailure(
                    call: Call<ExpResultEditResponse>,
                    t: Throwable
                ) {
                    t.message?.let { it1 -> activity?.showToastMsg(it1, 2) }
                }

                override fun onResponse(
                    call: Call<ExpResultEditResponse>,
                    response: Response<ExpResultEditResponse>
                ) {
                    if (response.body()?.status_code == AppConstant.CODE_SUCCESS) {
                        activity?.showToastMsg("Login Successfully", 1)
                        val expDate = arrayListOf<Any>()
                        getAppPref().setString("expDates", daytype)
                        getAppPref().setString("experimentId", response.body()!!.experId)
                        findNavController().navigate(R.id.action_experimentResultStep2_to_calculatedResultFragment)
                    } else {
                        activity?.showToastMsg(response.body()!!.message, 2)
                    }
                }
            }
        )
    }

    fun addDay(date: String, i: Int): String? {
        val dt = date // Start date

        val sdf = SimpleDateFormat("MM/dd/yy")
        val c = Calendar.getInstance()
        try {
            c.time = sdf.parse(dt)
        } catch (e: ParseException) {
            //e.printStackTrace()
        }

        val sdf1 = SimpleDateFormat("dd")

        c.add(
            Calendar.DATE,
            i
        ) // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE

        val day = sdf1.format(c.time)

        return day
    }

    fun addDate(date: String, i: Int): String? {
        val dt = date // Start date
        val sdf = SimpleDateFormat("MM/dd/yy")
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


}