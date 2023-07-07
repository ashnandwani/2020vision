package com.vision2020.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vision2020.DialogTimeoutListener
import com.vision2020.R
import com.vision2020.ui.user.LoginActivity
import com.vision2020.utils.AppConstant
import com.vision2020.utils.LogOutTimerUtil
import com.vision2020.utils.getAppPref
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), LogOutTimerUtil.LogOutListener,
    DialogTimeoutListener.Callback {
    lateinit var mcon: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mcon = this@MainActivity
        try {
            runOnUiThread {
                setContentView(R.layout.activity_main)
            }
        } catch (ex: java.lang.Exception) {
            runOnUiThread {
                setContentView(R.layout.activity_main)
            }
        }
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // val myNavHostFragment: NavHostFragment = nav_host_fragment as NavHostFragment
        //  val inflater = myNavHostFragment.navController.navInflater
        // val graph = inflater.inflate(R.navigation.for_teacher_navigation)
        //  myNavHostFragment.navController.graph = graph

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        // Student
        /*val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_profile,R.id.navigation_face,R.id.navigation_result,R.id.navigation_setting))*/
        // for teacher
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_setting,
                R.id.navigation_result,
                R.id.navigation_distress,
                R.id.distressExperimentSetupFragment,
                R.id.action_navigation_result_to_experimentResultStep2,
                R.id.navigation_face
            )
        )

        NavigationUI.setupWithNavController(toolbar, navController)
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val user_type = getAppPref().getString(AppConstant.KEY_USER_TYPE)
        if (user_type == "1") {
            navView.menu.findItem(R.id.navigation_result).isVisible = false
            //navView.getMenu().removeItem(R.id.navigation_result);
        }
        // for teacher
        // navView.menu.findItem(R.id.navigation_face).isVisible = false
        // for student
        // navView.menu.findItem(R.id.navigation_group).isVisible = false

        // for custom toolbar
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                /*  R.id.navigation_group ->{
                      txtTitle.text = getString(R.string.group_experiment)
                      back.visibility = View.GONE
                  }
                  R.id.navigation_face ->{
                      txtTitle.text = getString(R.string.face_recognition)
                      back.visibility = View.GONE
                  }
                  R.id.navigation_distress ->{
                     // txtTitle.text = getString(R.string.profile)
                      back.visibility =View.GONE
                  }*/
                R.id.distressExperimentSetupFragment -> {
                    //txtTitle.text = getString(R.string.group_experiment)
                    back.visibility = View.GONE
                }

                R.id.action_navigation_result_to_experimentResultStep2 -> {
                    //txtTitle.text = getString(R.string.group_experiment)
                    back.visibility = View.GONE
                }

                R.id.action_experimentResultStep2_to_calculatedResultFragment -> {
                    //txtTitle.text = getString(R.string.group_experiment)
                    back.visibility = View.GONE
                }
                R.id.action_calculatedResultFragment_to_expResultWeeklyFragment -> {
                    //txtTitle.text = getString(R.string.group_experiment)
                    back.visibility = View.GONE
                }

                /*R.id.action_distressFinalFragment_to_distressScenarioStep3 -> {
                    navController.navigate(R.id.action_distressFinalFragment_to_navigation_distress)
                }*/

                /*   R.id.navigation_result ->{
                       txtTitle.text = getString(R.string.distrss_scenario)
                       back.visibility =View.VISIBLE
                }*/
            }
        }
        back.setOnClickListener {
            navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("App 2020 Main::", "distroy view")

    }

    override fun onStop() {
        super.onStop()
        Log.e("App 2020 Main::", "Stop view")

    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        LogOutTimerUtil.startLogoutTimer(this, this)
        Log.e("Main App", "User interacting with screen")
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        LogOutTimerUtil.startLogoutTimer(this, this)
        Log.e("Main App", "User interacting with screen")
        return false
    }
    override fun onStart() {
        super.onStart()
        LogOutTimerUtil.startLogoutTimer(this, this);
        Log.e("Main App", "OnStart () &&& Starting timer");
    }

    override fun res(type: String) {
        super.res(type)
        appdialog.dismiss()
        getAppPref().setBoolean(AppConstant.KEY_LOOGED, false)
        val intent = Intent(mcon, LoginActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    lateinit var appdialog: AlertDialog
    override fun doLogout() {
        Log.e("Main App", "Logout called")
        this.runOnUiThread(Runnable {
            appdialog =
                AlertDialog.Builder(this)
                    .setTitle("Session Timeout")
                    .setMessage("Due to inactivity, you will soon be logged out.")
                    .setPositiveButton(
                        "Extend Session"
                    ) { dialog, which ->
                        appdialog.dismiss()
                        LogOutTimerUtil.startLogoutTimer(this, this)
                    }
                    .setNegativeButton(
                        "Log Out Now"
                    ) { dialog, which ->
                        appdialog.dismiss()
                        getAppPref().setBoolean(AppConstant.KEY_LOOGED, false)
                        val intent = Intent(mcon, LoginActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }
                    .create()
            val listener = DialogTimeoutListener(mcon, this)
            appdialog.setOnShowListener(listener)
            appdialog.setOnDismissListener(listener)
            try {
                appdialog.show()
            }catch (ex:Exception){

            }
        })
    }


}
