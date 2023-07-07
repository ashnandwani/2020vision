package com.vision2020.ui.distress

import BaseFragment
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.vision2020.R
import com.vision2020.data.response.DistressGroupDataListing
import com.vision2020.network.RetrofitClient
import com.vision2020.utils.AppConstant
import com.vision2020.utils.getAppPref
import org.json.JSONArray
import pl.droidsonroids.gif.AnimationListener
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifTextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class DistressExperimentSummary : BaseFragment<DistressScenarioViewModel>(), AnimationListener {


    override val layoutId: Int



        get() = R.layout.fragment_distress_experiment_summary
    override val viewModel: DistressScenarioViewModel
        get() = ViewModelProvider(requireActivity()).get(DistressScenarioViewModel::class.java)


    var grouplisting: ArrayList<GroupDataList> = ArrayList()

    val list = arrayListOf<Int>()

    var totalnarcan = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get the custom view for this fragment layout
        val view = inflater!!.inflate(R.layout.fragment_distress_experiment_summary,container,false)
        // Inflate the layout for this fragment

        var gifImageView: GifTextView
        var btnReset: Button
        var grouplist: Spinner

        gifImageView = view.findViewById(R.id.gifImageView);


        // btnReset = view.findViewById(R.id.btnReset);

        grouplist = view.findViewById(R.id.lstGroup);




        val drawable =
            GifDrawable(getResources(), R.drawable.heartanimationloopednew)
        drawable.stop()

        // drawable.loopCount = 10
        gifImageView.setBackground(drawable)

        /* btnReset.setOnClickListener {

             drawable.start()
             drawable.loopCount = 4
             drawable.seekToFrameAndGet(5)

         }*/


        val token = getAppPref().getString(AppConstant.KEY_TOKEN)
        RetrofitClient.instance!!.getdistressGroupList(token!!).enqueue(object : Callback<DistressGroupDataListing> {

            override fun onFailure(call: Call<DistressGroupDataListing>, t: Throwable) {
                ///  progress!!.dismiss()
                // t.message?.let { mContext.showToastMsg(it,1) }

                // Toast.makeText(mContext, t.message, Toast.LENGTH_LONG).show()
                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<DistressGroupDataListing>,
                response: Response<DistressGroupDataListing>) {

                if (response.body()!!.status_code.toInt()==AppConstant.CODE_SUCCESS) {

                    Log.d("Response Nisha Exp::::", response.body()!!.data.toString())


                    val jsonArray = JSONArray(response.body()!!.data)
                    grouplisting.add(GroupDataList("gid","Select Group"))

                    //   val jsonArray = jsonObject.optJSONArray("data")
                    var gData=response.body()!!.data;

                    var totaldata=jsonArray.length()

                    for (i in 0 until jsonArray.length()) {
                        val gname = gData?.get(i)?.group_name
                        val gid = gData?.get(i)?.id
                        val narscan = gData?.get(i)?.narcan
                        val subjectpassed = gData?.get(i)?.subjectpassed
                        if (narscan != null) {
                            list.add(narscan.toInt())
                        }

                        if (gname != null) {
                            if (gid != null) {
                                grouplisting.add(GroupDataList(gid.toString(),gname))
                                // grouplisting.add(i,gname)
                                //Log.d("Response::::", gname.toString())
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
                    grouplist.setAdapter(adapter)

                    // grouplisting.add(GroupDataList("", "Select Group"))

                    //  Log.d("Response::::", response.body()!!.status_code.toString())




                } else {
                    //progress!!.dismiss()
                    // mContext.showToastMsg(response.body()!!.message,1)
                    Toast.makeText(mContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                    //Toast.makeText(mContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                }

            }



        })



        grouplist.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                if(position!=0) {

                    totalnarcan = list.get(position-1) * 10

                    drawable.start()
                    drawable.loopCount = totalnarcan
                    drawable.seekToFrameAndGet(5)

                    Log.d("Response::::", totalnarcan.toString())




                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })


        onAnimationCompleted(totalnarcan);




        //gifImageView.startAnimation(fadeout);

        //Glide.with(this).load(R.raw.heartanimationoncenew).into(imageView)







        return view
    }

    override fun onAnimationCompleted(loopNumber: Int) {
        val view = view


        if (view != null) {
            Log.d("Response::::", "dfdsfsdfdsfsd")
        }
    }


    override fun onCreateStuff() {


    }

    override fun initListeners() {

    }

}

