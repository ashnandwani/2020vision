
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

abstract class BaseFragment<V : ViewModel> : Fragment(){
    var TAG: String = "BaseFragments:-"

    // since its going to be common for all the activities
     var mViewModel: V? = null
    lateinit var mContext: Context
    var progress: AlertDialog? = null

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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.mViewModel = if (mViewModel == null) viewModel else mViewModel
        this.mContext = this.context!!
        onCreateStuff()

        return inflater.inflate(layoutId, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initListeners()
    }

    protected abstract fun onCreateStuff()

    protected abstract fun initListeners()

    /*fun setTitle(title : String){
        val toolbar: Toolbar = activity!!.findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = title
        //toolbar.navigationIcon = ContextCompat.getDrawable(mContext,R.drawable.ic_arrow_back)
    }*/

    override fun onDestroy() {
        super.onDestroy()
        if(progress!=null){
            progress!!.dismiss()
            progress = null
        }
    }

}

