package com.vision2020.ui.result

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vision2020.R
import com.vision2020.data.response.ExpCalWeekResponse
import com.vision2020.network.RetrofitClient
import com.vision2020.utils.AppConstant
import com.vision2020.utils.getAppPref
import com.vision2020.utils.showToastMsg
import kotlinx.android.synthetic.main.fragment_calculated_result.*
import kotlinx.android.synthetic.main.fragment_exp_result_weekly.*
import kotlinx.android.synthetic.main.fragment_exp_result_weekly.textViewTitle2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExpResultWeeklyFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {


        return inflater.inflate(R.layout.fragment_exp_result_weekly, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var experimentId = getAppPref().getString("experimentId")
        var selDate = getAppPref().getString("selData")
        var simulationYear = getAppPref().getString("simulationYear")
        textViewTitle2.setText("Calculated Result for a Simulated "+simulationYear+" year drug exposure")
        val token = getAppPref().getString(AppConstant.KEY_TOKEN)

        if (selDate != null) {
            RetrofitClient.instance!!.fetchExpResultbyWeek(
                token!!, selDate.toString(), experimentId.toString(),
                getAppPref().getString(AppConstant.SEM).toString()

            ).enqueue(object : Callback<ExpCalWeekResponse> {
                override fun onFailure(call: Call<ExpCalWeekResponse>, t: Throwable) {
                    // returnValue(UpdateProfileResponse(t.message!!))
                    // Toast.makeText(activity, t.message!!, Toast.LENGTH_LONG).show()
                    activity?.showToastMsg(t.message!!, 2)
                }

                override fun onResponse(
                    call: Call<ExpCalWeekResponse>,
                    response: Response<ExpCalWeekResponse>
                ) {
                    if (response.body()!!.status_code == AppConstant.CODE_SUCCESS) {
                        textViewWE2.text = response.body()!!.data.totalCycles
                        textViewWE4.text = response.body()!!.data.averageCycles
                        textViewWE6.text = response.body()!!.data.rangeofCycles
                        textViewWE8.text = response.body()!!.data.totalHits
                        textViewWE10.text = response.body()!!.data.averageHits
                        textViewWE12.text = response.body()!!.data.rangeofHits
                        textViewWE14.text = response.body()!!.data.totalVapeJuice
                        textViewWE16.text = response.body()!!.data.averageVapeJuice
                        textViewWE18.text = response.body()!!.data.rangeofVapeJuice
                        textViewWE20.text = response.body()!!.data.totalDrugDispensed
                        textViewWE22.text = response.body()!!.data.averageDrugDispensed
                        textViewW2.text = response.body()!!.data.rangeofDrugDispensed
                        textViewW4.text = response.body()!!.data.totalDrugLung
                        textViewW6.text = response.body()!!.data.averageDrugLung
                        textViewW8.text = response.body()!!.data.rangeofDrugLung
                        textViewW10.text = response.body()!!.data.totalLungs
                        textViewW12.text = response.body()!!.data.averageLungs
                        textViewW14.text = response.body()!!.data.rangeofLungs
                        textViewW16.text = response.body()!!.data.totalDrugDispersed
                        textViewW18.text = response.body()!!.data.averageDrugDispersed
                        textViewW20.text = response.body()!!.data.rangeofDrugDispersed
                        // Log.d("Response::::", response.body()!!.data.toString())
                    }
                }
            })
        }
    }
}