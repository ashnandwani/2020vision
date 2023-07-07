package com.vision2020.ui.distress

import android.R.attr
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vision2020.R
import com.vision2020.utils.getAppPref
import kotlinx.android.synthetic.main.fragment_distress_scenario_step3.*
import pl.droidsonroids.gif.AnimationListener
import pl.droidsonroids.gif.GifDrawable


class DistressFinalFragment : Fragment(), AnimationListener {

    private lateinit var gifDrawable: GifDrawable
    private lateinit var gifImageViewstop: GifDrawable
    private lateinit var gifHeartView: GifDrawable
    private lateinit var heart_img: ImageView
    var grouplisting: ArrayList<GroupDataList> = ArrayList()

    val list = arrayListOf<Int>()

    var totalsubject = 0
    var totalsubjectchk = 1
    var totalnarcan = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_distress_final, container, false)
    }
    lateinit var abc : CountDownTimer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var grpName: TextView
        grpName = view.findViewById(R.id.grpName)
        heart_img = view.findViewById(R.id.heart_img)
        gifDrawable = gifImageView.drawable as GifDrawable
        gifImageViewstop = gifImageViewStop.drawable as GifDrawable
        gifHeartView = gifImageViewHeart.drawable as GifDrawable
        gifDrawable.stop()
        gifImageViewstop.stop()
        val subjectpassed = getAppPref().getString("subjectpassed")
        totalnarcan = getAppPref().getString("totalnarcan").toString()
        val groupName = getAppPref().getString("groupName")
        if (subjectpassed != null) {
            totalsubject = subjectpassed.toInt()
        }
        totalsubjectchk =   Integer.parseInt(totalsubject.toString())/10 - Integer.parseInt(totalnarcan.toString())/10
        grpName.setText(groupName)
        gifDrawable.start()
        gifHeartView.start()
        Log.d("Animation:::", "dds on start:::"+totalsubjectchk)
         abc =object : CountDownTimer((totalsubjectchk.toLong() *10000), 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                Log.d("Animation:::", "dds"+totalsubjectchk)
                try {
                    gifDrawable.stop()
                    val gifDrawable = GifDrawable(resources, R.drawable.heart_animation_line)
                    gifImageView.setImageDrawable(gifDrawable)
                    gifDrawable.start()
                    gifHeartView.stop()
                    animationCompleted()
                }catch (ex:Exception){

                }
            }
        }.start()
        Log.d("Animation start:::", "ass")
        gifDrawable.addAnimationListener(this)
    }


    private fun resetAnimation() {
        gifDrawable.stop()
        if (totalsubject != null)
            gifDrawable.loopCount = totalsubject

        //gifDrawable.seekToFrameAndGet(5)
    }

    private fun toggleAnimation() = when {
        gifDrawable.isPlaying -> gifDrawable.stop()
        else -> gifDrawable.start()
    }

    override fun onDestroyView() {
        abc.cancel()
        gifDrawable.removeAnimationListener(this)
        gifHeartView.removeAnimationListener(this)
        gifHeartView.reset()
        gifHeartView.loopCount = 0
        super.onDestroyView()
    }



    override fun onAnimationCompleted(loopNumber: Int) {
        /* Log.d("Animation complete:::", "ass")
         val view = view
         if (view != null) {
             var a = totalsubject - 1
             if (loopNumber == a) { //totalsubject
                 resetAnimation()
                 val dialogBuilder =
                     activity?.let { AlertDialog.Builder(it).create() }
                 val inflater = layoutInflater
                 val dialogView: View = inflater.inflate(R.layout.distressfinalpopup, null)

                 val btnOk =
                     dialogView.findViewById<View>(R.id.btnOk) as Button



                 btnOk.setOnClickListener {
                     if (dialogBuilder != null) {
                         dialogBuilder.dismiss()
                         findNavController().navigate(R.id.action_distressFinalFragment_to_navigation_distress)
                     }
                 }




                 if (dialogBuilder != null) {
                     dialogBuilder.getWindow()
                         ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                     dialogBuilder.setView(dialogView)
                     dialogBuilder.show()
                     dialogBuilder.setCanceledOnTouchOutside(true)
                 };

             }
         }*/
    }

    fun animationCompleted() {
        Log.d("Animation complete:::", "ass")
        val view = view
        if (view != null) {
            val dialogBuilder =
                activity?.let { AlertDialog.Builder(it).create() }
            val inflater = layoutInflater
            val dialogView: View = inflater.inflate(R.layout.distressfinalpopup, null)

            val btnOk =
                dialogView.findViewById<View>(R.id.btnOk) as Button
            btnOk.setOnClickListener {
                if (dialogBuilder != null) {
                    gifDrawable.stop()
                    dialogBuilder.dismiss()
                    findNavController().navigate(R.id.action_distressFinalFragment_to_navigation_distress)
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