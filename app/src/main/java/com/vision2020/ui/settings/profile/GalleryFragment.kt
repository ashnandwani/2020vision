package com.vision2020.ui.settings.profile

import BaseFragment
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.vision2020.R
import com.vision2020.ui.BaseViewModel
import kotlinx.android.synthetic.main.fragment_gallery.*


class GalleryFragment : BaseFragment<BaseViewModel>() {
   override val layoutId: Int
        get() = R.layout.fragment_gallery
    override val viewModel: BaseViewModel
        get() = ViewModelProvider(this).get(BaseViewModel::class.java)
    lateinit var adapter: GalleryAdapter
    override fun onCreateStuff() {
    }
    fun calculateNoOfColumns(
        columnWidthDp: Float
    ): Int { // For example columnWidthdp=180
        val displayMetrics: DisplayMetrics = requireActivity().getResources().getDisplayMetrics()
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun initListeners() {
        val manager =
            GridLayoutManager(activity,calculateNoOfColumns(245f) , GridLayoutManager.VERTICAL, false)
        list.setLayoutManager(manager)
        adapter = GalleryAdapter(requireActivity()!!)
        list.setAdapter(adapter)
    }
}
