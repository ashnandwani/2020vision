package com.vision2020.ui.distress

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vision2020.AppApplication
import com.vision2020.R
import com.vision2020.data.response.DistressGroupDataListing
import com.vision2020.network.RetrofitClient
import com.vision2020.utils.AppConstant
import com.vision2020.utils.getAppPref
import kotlinx.android.synthetic.main.fragment_distress_scenario_step3.*
import org.json.JSONArray
import pl.droidsonroids.gif.AnimationListener
import pl.droidsonroids.gif.GifDrawable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DistressScenarioStep3 : Fragment(), AnimationListener {

    private lateinit var gifDrawable: GifDrawable
    private lateinit var gifImageViewstop: GifDrawable
    private lateinit var gifHeartView: GifDrawable
    var grouplisting: ArrayList<GroupDataList> = ArrayList()

    val list = arrayListOf<Int>()
    val listsubject = arrayListOf<Int>()
    val listGrp = arrayListOf<String>()

    var totalnarcan = 0
    var totalnarcanchk = -1

    var totalSubject = 0
    var totalSubjectchk = -1

    var groupName = ""
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_distress_scenario_step3, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //  var grouplist: Spinner

        // grouplist = view.findViewById(R.id.lstGroup);
        try {
            gifDrawable = gifImageView.drawable as GifDrawable
            gifImageViewstop = gifImageViewStop.drawable as GifDrawable
            gifHeartView = gifImageViewHeart.drawable as GifDrawable
            gifDrawable.stop()
            gifImageViewstop.stop()
            gifHeartView.stop()
            //gifDrawable.addAnimationListener(this)

            // gifDrawable.stop()

            val token = getAppPref().getString(AppConstant.KEY_TOKEN)

            val userType = getAppPref().getString(AppConstant.KEY_USER_TYPE)
            if (userType == "2") {
                var expId =
                        AppApplication.getPref(AppApplication.getInstance()!!).getString("expid", "")
                RetrofitClient.instance!!.getdistressGroupList(token!!, expId!!).enqueue(object :
                        Callback<DistressGroupDataListing> {

                    override fun onFailure(call: Call<DistressGroupDataListing>, t: Throwable) {
                        Toast.makeText(activity, getString(R.string.server_error), Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                            call: Call<DistressGroupDataListing>,
                            response: Response<DistressGroupDataListing>
                    ) {
                        try {
                            if (response.body()!!.status_code.toInt() == AppConstant.CODE_SUCCESS) {
                                Log.d("Response::::", response.body()!!.data.toString())
                                val jsonArray = JSONArray(response.body()!!.data)
                                if (userType == "2") {
                                    grouplisting.add(GroupDataList("", "Select Group"))
                                }
                                var gData = response.body()!!.data;
                                var totaldata = jsonArray.length()
                                for (i in 0 until jsonArray.length()) {
                                    val gname = gData?.get(i)?.group_name
                                    val gid = gData?.get(i)?.id
                                    val narscan = gData?.get(i)?.narcan
                                    val subjectpassed = gData?.get(i)?.subjectpassed
                                    //if (narscan != 0 && subjectpassed != 0) {
                                    listGrp.add(gname.toString())
                                    if (narscan != null) {
                                        list.add(narscan.toInt())
                                    }
                                    if (subjectpassed != null) {
                                        listsubject.add(subjectpassed.toInt())
                                    }
                                    if (gname != null) {
                                        if (gid != null) {
                                            grouplisting.add(GroupDataList(gid.toString(), gname))
                                        }
                                    }
                                }
                                val adapter: ArrayAdapter<GroupDataList>? = activity?.let {
                                    ArrayAdapter<GroupDataList>(
                                            it,
                                            android.R.layout.simple_spinner_dropdown_item,
                                            grouplisting
                                    )
                                }
                                lstGroup.setAdapter(adapter)
                                // grouplisting.add(GroupDataList("", "Select Group"))
                                //  Log.d("Response::::", response.body()!!.status_code.toString())
                            } else {
                                Toast.makeText(activity, response.body()!!.message, Toast.LENGTH_LONG)
                                        .show()
                                if (response.body()!!.message.startsWith("No records available")) {
                                    findNavController().navigate(R.id.action_distressScenarioStep3_to_navigation_distress)
                                }
                            }
                        } catch (ex: Exception) {

                        }
                    }
                })
            } else {
                //for (i in 0 until jsonArray.length()) {
                val gname =
                        getAppPref().getString("g_grp_name")
                val gid = getAppPref().getString("g_grp_id")
                val narscan =
                        getAppPref().getInt("g_narscan")
                val subjectpassed =
                        getAppPref().getInt("g_sub_passed")
                listGrp.add(gname.toString())
                if (narscan != null) {
                    list.add(narscan)
                }
                if (subjectpassed != null) {
                    listsubject.add(subjectpassed)
                }
                if (gname != null) {
                    if (gid != null) {
                        grouplisting.add(GroupDataList(gid.toString(), gname))
                    }
                }
                //  }
                val adapter: ArrayAdapter<GroupDataList>? = activity?.let {
                    ArrayAdapter<GroupDataList>(
                            it,
                            android.R.layout.simple_spinner_dropdown_item,
                            grouplisting
                    )
                }
                lstGroup.setAdapter(adapter)
            }

            lstGroup.setOnItemSelectedListener(object :
                    AdapterView.OnItemSelectedListener {
                @RequiresApi(Build.VERSION_CODES.N)
                override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                ) {
                    if (userType == "2") {
                        if (position != 0) {
                            totalnarcan = list.get(position - 1) * 10
                            totalnarcanchk = (list.get(position - 1) * 10) - 1
                            totalSubject = listsubject.get(position - 1) * 10
                            totalSubjectchk = totalSubject - totalnarcan
                            groupName = listGrp.get(position - 1)
                            gifDrawable.start()
                            gifHeartView.start()
                            Log.d("Animation start:::", "dds")
                            //resetAnimation()
                            // toggleAnimation()
                            object : CountDownTimer((totalnarcan.toLong() * 1000), 1000) {
                                override fun onTick(millisUntilFinished: Long) {
                                }

                                override fun onFinish() {
                                    Log.d("Animation stop:::", "dds")
                                    gifDrawable.stop()
                                    gifHeartView.stop()
                                    animComplete()
                                }
                            }.start()

                            Log.d("Response::::", groupName)
                            lstGroup.setEnabled(false)
                            lstGroup.setClickable(false)
                        }
                    } else {
                        totalnarcan = list.get(position) * 10
                        totalnarcanchk = (list.get(position) * 10) - 1
                        totalSubject = listsubject.get(position) * 10
                        totalSubjectchk = totalSubject - totalnarcan
                        groupName = listGrp.get(position)
                        gifDrawable.start()
                        gifHeartView.start()
                        gifDrawable.start()
                        Log.d("Animation start:::", "dds")
                        object : CountDownTimer((totalnarcan.toLong() * 1000), 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                            }

                            override fun onFinish() {
                                Log.d("Animation stop:::", "dds")
                                gifDrawable.stop()
                                gifHeartView.stop()
                                animComplete()
                            }
                        }.start()
                        Log.d("Animation start:::", "ass")
                        Log.d("Response::::", groupName)
                        lstGroup.setEnabled(false)
                        lstGroup.setClickable(false)
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            })
        } catch (ex: Exception) {
            Log.v("sasd", "asdsad" + ex.message)
        }
    }


    private fun resetAnimation() {
        Log.v("Scenario 3 : ", "Value ::: " + totalnarcan)
        gifDrawable.stop()
        gifDrawable.loopCount = totalnarcan
        //gifDrawable.seekToFrameAndGet(5)
        lstGroup.setEnabled(true)
        lstGroup.setClickable(true)
        //  buttonToggle.isChecked = false
        // lstGroup.setAdapter(adapter);
    }

    private fun toggleAnimation() = when {
        gifDrawable.isPlaying -> gifDrawable.stop()
        else -> gifDrawable.start()
    }

    override fun onDestroyView() {
        gifDrawable.removeAnimationListener(this)
        gifDrawable.reset()
        gifDrawable.loopCount = 0
        gifHeartView.removeAnimationListener(this)
        gifHeartView.reset()
        gifHeartView.loopCount = 0
        super.onDestroyView()
    }

    override fun onAnimationCompleted(loopNumber: Int) {
        /*Log.d("Animation complete:::", "ass" + loopNumber)
        val view = view
        if (view != null) {
            if (loopNumber == totalnarcanchk) {
                resetAnimation()
                val dialogBuilder =
                    activity?.let { AlertDialog.Builder(it).create() }
                val inflater = layoutInflater
                val dialogView: View =
                    inflater.inflate(R.layout.distresssummarypopuplinearlayout, null)
                val btnYes =
                    dialogView.findViewById<View>(R.id.btnYes) as Button
                val btnNo =
                    dialogView.findViewById<View>(R.id.btnNo) as Button
                btnYes.setOnClickListener {
                    if (dialogBuilder != null) {
                        dialogBuilder.dismiss()
                        findNavController().navigate(R.id.action_distressScenarioStep3_to_navigation_distress)
                    }
                }
                btnNo.setOnClickListener {
                    if (dialogBuilder != null) {
                        dialogBuilder.dismiss()
                        getAppPref().setString(
                            "subjectpassed",
                            totalSubject.toString()
                        )
                        getAppPref().setString("groupName", groupName)
                        findNavController().navigate(R.id.action_distressScenarioStep3_to_distressFinalFragment)
                    }
                }
                if (dialogBuilder != null) {
                    dialogBuilder.getWindow()
                        ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialogBuilder.setView(dialogView)
                    dialogBuilder.show()
                    dialogBuilder.setCanceledOnTouchOutside(true)
                }
            }
        }*/
    }

    fun animComplete() {
        val view = view
        if (view != null) {
            val dialogBuilder =
                    activity?.let { AlertDialog.Builder(it).create() }
            val inflater = layoutInflater
            val dialogView: View =
                    inflater.inflate(R.layout.distresssummarypopuplinearlayout, null)
            val btnYes =
                    dialogView.findViewById<View>(R.id.btnYes) as Button
            val btnNo =
                    dialogView.findViewById<View>(R.id.btnNo) as Button
            btnYes.setOnClickListener {
                if (dialogBuilder != null) {
                    dialogBuilder.dismiss()
                    findNavController().navigate(R.id.action_distressScenarioStep3_to_navigation_distress)
                }
            }
            btnNo.setOnClickListener {
                if (dialogBuilder != null) {
                    dialogBuilder.dismiss()
                    getAppPref().setString(
                            "subjectpassed",
                            totalSubject.toString()
                    )
                    getAppPref().setString(
                            "totalnarcan",
                            totalnarcan.toString()
                    )
                    getAppPref().setString("groupName", groupName)
                    findNavController().navigate(R.id.action_distressScenarioStep3_to_distressFinalFragment)
                }
            }
            if (dialogBuilder != null) {
                dialogBuilder.getWindow()
                        ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialogBuilder.setView(dialogView)
                dialogBuilder.show()
                dialogBuilder.setCanceledOnTouchOutside(true)
            }
        }
    }


}