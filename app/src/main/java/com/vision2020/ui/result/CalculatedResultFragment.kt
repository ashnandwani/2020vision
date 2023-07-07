package com.vision2020.ui.result

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.vision2020.R
import com.vision2020.data.response.ExpCalcResponse
import com.vision2020.network.RetrofitClient
import com.vision2020.utils.AppConstant
import com.vision2020.utils.getAppPref
import com.vision2020.utils.showToastMsg
import kotlinx.android.synthetic.main.fragment_calculated_result.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CalculatedResultFragment : Fragment() {


    private lateinit var linlay_parent: LinearLayout

    var expDatelist = ArrayList<String>()
    var selDate=""
    var experimentId:String?=""
    lateinit var v:View
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val imm: InputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v!!.windowToken, 0)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_calculated_result, container, false)
    }
    fun getDay(date: String): String? {
        val dt = date // Start date
        val sdf = SimpleDateFormat("MM-dd-yyyy")
        val c = Calendar.getInstance()
        try {
            c.time = sdf.parse(dt)
        } catch (e: ParseException) {
            //e.printStackTrace()
        }
        val sdf1 = SimpleDateFormat("EEEE")
       // c.add(Calendar.DATE) // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        val day = sdf1.format(c.time)
        return day
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v=view
        val selectedweek = getAppPref().getString("selectedweek")

        var day0 = view.findViewById<TextView>(R.id.sWeek)


         experimentId = getAppPref().getString("experimentId")
         var expdates = getAppPref().getString("expDates")
         day0.setText(getDay(expdates!!)+" , "+selectedweek)
         var simulationYear = getAppPref().getString("simulationYear")
         textViewTitle2.setText("Calculated Result for a Simulated "+simulationYear+" year drug exposure")

         // var expdatenew = getAppPref().getString("expDates")
         linlay_parent=view.findViewById(R.id.layoutForTeacher1)
        /*if (expdates != null) {
            expdates = expdates.replace("[", "")
            expdates = expdates.replace("]", "")
            val expdatelist = expdates.split(",").toTypedArray()
            for (i in 0 until expdatelist.size) {
                expDatelist.add(i, expdatelist[i].toString().trim())
                Log.d("Response::::", expdatelist[i].toString())
            }
            selDate = expDatelist[1].toString().trim()*/

            val datelist: TextView = view.findViewById<View>(R.id.datelist) as TextView
            datelist.setText(expdates.toString())
            selDate=expdates.toString()
            apiCall()
            //Log.d("Response::::", expdatelist[i].toString())
            /*   if (expDatelist != null) {
                val expDatelist = expDatelist.distinct().toList()
                Log.d("Response1::::", .toSexpDatelisttring())
                val adapter = activity?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item,
                        expDatelist
                    )
                }
                datelist.setAdapter(adapter)
              //  datelist.setSelection(7);
                datelist?.setOnItemSelectedListener(object :
                    AdapterView.OnItemSelectedListener {
                    @RequiresApi(Build.VERSION_CODES.N)
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        //if (position > 0) {
linlay_parent.visibility = View.VISIBLE
                      //  }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                })
            }*/





        btnWeekResult.setOnClickListener {

        }





    }

    private fun apiCall() {

        val token = getAppPref().getString(AppConstant.KEY_TOKEN)

        if (selDate != null) {
            RetrofitClient.instance!!.fetchExpResultbyDate(
                token!!, selDate.toString(), experimentId.toString(),
                getAppPref().getString(AppConstant.SEM).toString()
            ).enqueue(object : Callback<ExpCalcResponse> {
                override fun onFailure(
                    call: Call<ExpCalcResponse>,
                    t: Throwable
                ) {
                    activity?.showToastMsg(t.message!!, 2)
                }

                override fun onResponse(
                    call: Call<ExpCalcResponse>,
                    response: Response<ExpCalcResponse>
                ) {
                    if (response.body()?.status_code == AppConstant.CODE_SUCCESS) {
                        textView2.text = response.body()!!.data.expLength
                        textView4.text = response.body()!!.data.cycles
                        textView6.text = response.body()!!.data.totalHits
                        textView8.text =
                            response.body()!!.data.totalVapeJuice
                        textView10.text =
                            response.body()!!.data.drugConcentration
                        textView12.text =
                            response.body()!!.data.drugDispensed
                        textView14.text = response.body()!!.data.drugLungs
                        textView16.text =
                            response.body()!!.data.dispersedRestBody
                        textView18.text =
                            response.body()!!.data.drugdispersedRestBody

                        Log.d(
                            "Response::::",
                            response.body()!!.data.toString()
                        )
                    }
                }
            })
        }

    }


}