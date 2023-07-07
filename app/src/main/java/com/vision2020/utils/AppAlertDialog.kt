package com.vision2020.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.InputType
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.vision2020.R
import com.vision2020.network.RetrofitClient

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import com.vision2020.data.response.CalLoginDistressResponse as CalLoginDistressResponse1


class AppAlertDialog {
    fun showPassword(
        view: View,
        callback: GetResult,
        activity: FragmentActivity?
    ) {
        val builder = AlertDialog.Builder(view.context)
        val dialog: AlertDialog = builder.create()
        val inflater = activity!!.layoutInflater
        val dialogView1: View = inflater.inflate(R.layout.fragment_dialog_with_weeklydata, null)
        val editText1 = dialogView1.findViewById(R.id.etPassword) as EditText
        editText1.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
        val button2 = dialogView1.findViewById(R.id.btnSubmit) as Button
        button2.setOnClickListener { // DO SOMETHINGS
            val progressDialog = ProgressDialog(activity)
            progressDialog.setMessage("Loading, please wait")
            progressDialog.show()
            val token = getAppPref().getString(AppConstant.KEY_TOKEN)

            RetrofitClient.instance!!.userDistressLogin(
                token!!, editText1.text.toString()

            ).enqueue(object : Callback<CalLoginDistressResponse1> {
                override fun onFailure(call: Call<CalLoginDistressResponse1>, t: Throwable) {
                    activity?.showToastMsg(t.message!!, 2)
                }

                override fun onResponse(
                    call: Call<CalLoginDistressResponse1>,
                    response: Response<CalLoginDistressResponse1>
                ) {
                    if (response.body()!!.status_code == AppConstant.CODE_SUCCESS) {
                        callback.response(response, true)
                    } else {
                        callback.response(response, false)
                    }
                    val imm = activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    var view = activity!!.currentFocus
                    if (view == null) {
                        view = View(activity)
                    }
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                    dialog.dismiss()
                    progressDialog.dismiss()
                }
            })
        }

        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.setView(dialogView1)
        dialog.show()
    }

    fun retriveData(
        activity: FragmentActivity?,
        callback: GetClick
    ) {
        val builder = AlertDialog.Builder(activity)
        val dialog: AlertDialog = builder.create()
        dialog.setCancelable(false)
        val inflater = activity!!.layoutInflater
        val dialogLayout = inflater.inflate(R.layout.fragment_dialog_retrive_data, null)
        val btnretrive = dialogLayout.findViewById<Button>(R.id.btnretrive)
        val btncustomedit = dialogLayout.findViewById<Button>(R.id.btncustomedit)
        val btnCancel = dialogLayout.findViewById<Button>(R.id.btnCancel)
        btnCancel.visibility = View.GONE
        builder.setView(dialogLayout)
        btnretrive.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            callback.response("retrive")
        })
        btncustomedit.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            callback.response("edit")
        })
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.setView(dialogLayout)
        dialog.show()
    }

    fun selectOptions(
        activity: FragmentActivity?,
        callback: GetClick
    ) {
        val builder = AlertDialog.Builder(activity)
        val dialog: AlertDialog = builder.create()
        dialog.setCancelable(false)
        val inflater = activity!!.layoutInflater
        val dialogLayout = inflater.inflate(R.layout.fragment_dialog_retrive_data, null)
        val btnretrive = dialogLayout.findViewById<Button>(R.id.btnretrive)
        val btncustomedit = dialogLayout.findViewById<Button>(R.id.btncustomedit)
        val btnCancel = dialogLayout.findViewById<Button>(R.id.btnCancel)
        btnretrive.setText("Camera")
        btncustomedit.setText("Gallery")
        btnCancel.visibility = View.VISIBLE
        builder.setView(dialogLayout)
        btnretrive.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            callback.response("camera")
        })
        btncustomedit.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            callback.response("gallery")
        })
        btnCancel.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.setView(dialogLayout)
        dialog.show()
    }

    fun selectDate(
        view: View,
        callback: GetClick,
        activity: FragmentActivity?, date: String
    ) {
        val builder = AlertDialog.Builder(view.context)
        val dialog: AlertDialog = builder.create()
        val inflater = activity!!.layoutInflater
        val dialogView1: View = inflater.inflate(R.layout.fragment_dialog_select_date, null)
        val head_date = dialogView1.findViewById(R.id.head_date) as TextView
        val sub_head_date = dialogView1.findViewById(R.id.sub_head_date) as TextView
        val one = dialogView1.findViewById(R.id.one) as TextView
        val two = dialogView1.findViewById(R.id.two) as TextView
        val three = dialogView1.findViewById(R.id.three) as TextView
        val four = dialogView1.findViewById(R.id.four) as TextView
        val five = dialogView1.findViewById(R.id.five) as TextView
        val six = dialogView1.findViewById(R.id.six) as TextView
        val seven = dialogView1.findViewById(R.id.seven) as TextView

        //"dd MMM yyyy HH:mm:ss 'GMT'"
        var inputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
        var outputFormat: DateFormat = SimpleDateFormat("EEEE, MMMM dd, yyyy")
        var outputDateStr: String = outputFormat.format(inputFormat.parse(date))
        head_date.setText(outputDateStr) //"Thursday, 24 August, 2021"

        var outputFormatNew: DateFormat = SimpleDateFormat("MMMM yyyy")
        var outputDateStrNew: String = outputFormatNew.format(inputFormat.parse(date))
        var dateList = getWeek(getAppPref().getString("dselDate"))
        var dateListNew = getWeekNew(getAppPref().getString("dselDate"))
        one.setText(dateList.get(1))
        one.setTag(dateListNew.get(1))
        two.setText(dateList.get(2))
        two.setTag(dateListNew.get(2))
        three.setText(dateList.get(3))
        three.setTag(dateListNew.get(3))
        four.setText(dateList.get(4))
        four.setTag(dateListNew.get(4))
        five.setText(dateList.get(5))
        five.setTag(dateListNew.get(5))
        six.setText(dateList.get(6))
        six.setTag(dateListNew.get(6))
        seven.setText(dateList.get(7))
        seven.setTag(dateListNew.get(7))
        sub_head_date.setText(outputDateStrNew)
        one.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            callback.response(one.tag.toString())
        })
        two.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            callback.response(two.tag.toString())
        })
        three.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            callback.response(three.tag.toString())
        })
        four.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            callback.response(four.tag.toString())
        })
        five.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            callback.response(five.tag.toString())
        })
        six.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            callback.response(six.tag.toString())
        })
        seven.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            callback.response(seven.tag.toString())
        })
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.setView(dialogView1)
        dialog.show()
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
 private fun getWeekNew(group: String?): ArrayList<String?> {
        var weekList = arrayListOf(
            "Select Date",
            group?.let { addPickDate(it, 0) },
            group?.let { addPickDate(it, 1) },
            group?.let { addPickDate(it, 2) },
            group?.let { addPickDate(it, 3) },
            group?.let { addPickDate(it, 4) },
            group?.let { addPickDate(it, 5) },
            group?.let { addPickDate(it, 6) })

        return weekList
    }

    fun addDate(date: String, i: Int): String? {
        val dt = date // Start date
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val c = Calendar.getInstance()
        try {
            c.time = sdf.parse(dt)
        } catch (e: ParseException) {
            //e.printStackTrace()
        }
        val sdf1 = SimpleDateFormat("dd")//-MM-yyyy
        c.add(
            Calendar.DATE,
            i
        ) // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        val day = sdf1.format(c.time)
        return day
    }


    fun addPickDate(date: String, i: Int): String? {
        val dt = date // Start date
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val c = Calendar.getInstance()
        try {
            c.time = sdf.parse(dt)
        } catch (e: ParseException) {
            //e.printStackTrace()
        }
        val sdf1 = SimpleDateFormat("yyyy-MM-dd")//
        c.add(
            Calendar.DATE,
            i
        ) // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        val day = sdf1.format(c.time)
        return day
    }



    interface GetResult {
        fun response(response: Response<CalLoginDistressResponse1>, type: Boolean) {}
    }

    interface GetClick {
        fun response(type: String) {

        }
    }
}