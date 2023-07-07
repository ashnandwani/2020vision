import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks
import androidx.lifecycle.ViewModel
import com.vision2020.AppApplication
import com.vision2020.LogoutListener

abstract class BaseActivity<V : ViewModel> : AppCompatActivity() {
    var progress: AlertDialog? = null

    var TAG: String = "BaseActivity:-"

    // since its going to be common for all the activities
    var mViewModel: V? = null
    lateinit var mContext: Context

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
//    abstract val bindingVariable: Int

    /**
     * @return toolbar resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract val viewModel: V

    /**
     *
     * @return context
     */
    protected abstract val context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        }*/
        setContentView(layoutId)
        mContext = context
        this.mViewModel = if (mViewModel == null) viewModel else mViewModel
        onCreate()
        initListeners()
        supportFragmentManager
            .registerFragmentLifecycleCallbacks(object: FragmentLifecycleCallbacks() {
                override fun onFragmentViewCreated(
                    fm: FragmentManager,
                    f: Fragment,
                    v: View,
                    savedInstanceState: Bundle?
                ) {
                    super.onFragmentViewCreated(fm, f, v, savedInstanceState)
                    Log.d("session App", "session onFragmentViewCreated")
                }

                override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
                    super.onFragmentStopped(fm, f)
                    Log.d("session App", "session onFragmentStopped")
                }

                override fun onFragmentCreated(
                    fm: FragmentManager,
                    f: Fragment,
                    savedInstanceState: Bundle?
                ) {
                    super.onFragmentCreated(fm, f, savedInstanceState)
                    Log.d("session App", "session onFragmentCreated")
                }

                override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                    super.onFragmentResumed(fm, f)
                    Log.d("session App", "session onFragmentResumed")
                }

                override fun onFragmentAttached(
                    fm: FragmentManager,
                    f: Fragment,
                    context: Context
                ) {
                    super.onFragmentAttached(fm, f, context)
                    Log.d("session App", "session onFragmentAttached")
                }

                override fun onFragmentPreAttached(
                    fm: FragmentManager,
                    f: Fragment,
                    context: Context
                ) {
                    super.onFragmentPreAttached(fm, f, context)
                    Log.d("session App", "session onFragmentPreAttached")
                }

                override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
                    super.onFragmentDestroyed(fm, f)
                    Log.d("session App", "session onFragmentDestroyed")
                }

                override fun onFragmentSaveInstanceState(
                    fm: FragmentManager,
                    f: Fragment,
                    outState: Bundle
                ) {
                    super.onFragmentSaveInstanceState(fm, f, outState)
                    Log.d("session App", "session onFragmentSaveInstanceState")
                }

                override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
                    super.onFragmentStarted(fm, f)
                    Log.d("session App", "session onFragmentStarted")
                }

                override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
                    super.onFragmentViewDestroyed(fm, f)
                    Log.d("session App", "session onFragmentViewDestroyed")
                }

                override fun onFragmentPreCreated(
                    fm: FragmentManager,
                    f: Fragment,
                    savedInstanceState: Bundle?
                ) {
                    super.onFragmentPreCreated(fm, f, savedInstanceState)
                    Log.d("session App", "session onFragmentPreCreated")
                }

                override fun onFragmentActivityCreated(
                    fm: FragmentManager,
                    f: Fragment,
                    savedInstanceState: Bundle?
                ) {
                    super.onFragmentActivityCreated(fm, f, savedInstanceState)
                    Log.d("session App", "session onFragmentActivityCreated")
                }

                override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
                    super.onFragmentPaused(fm, f)
                    Log.d("session App", "session onFragmentPaused")
                }

                override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
                    super.onFragmentDetached(fm, f)
                    Log.d("session App", "session onFragmentDetached")
                }
            },true)
    }

    abstract fun onCreate()

    abstract fun initListeners()
    override fun onResume() {
        super.onResume()
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.v("session onBackPressed", "onBackPressed")
    }

    /*fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }*/

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (progress != null) {
            progress!!.dismiss()
            progress = null



        }
    }
}

