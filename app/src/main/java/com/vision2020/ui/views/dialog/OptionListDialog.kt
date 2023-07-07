package com.vision2020.ui.views.dialog
import android.content.Context
import android.view.Gravity
import androidx.recyclerview.widget.LinearLayoutManager
import com.vision2020.adapter.OptionAdapter
import com.vision2020.data.response.GroupListing
import kotlinx.android.synthetic.main.dialog_options.*
class OptionListDialog(
    context: Context,
    themeResId: Int,
    private val LayoutId: Int,
    var list: ArrayList<GroupListing.Data>,
    title: String,
    private val callback: ItemCallBack.Callback
) : BaseListDialog(context, themeResId) {
    private lateinit var mAdapter: OptionAdapter
    var title: String
    init {
        val mWindow = this.window!!.attributes
        mWindow.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        window!!.attributes = mWindow
        this.title = title
    }
    override fun getInterfaceInstance(): ItemCallBack.IndexClick {
        return this
    }
    override fun onCreateStuff() {
        if (list.isNotEmpty()) {
            recyclerView!!.layoutManager = LinearLayoutManager(context)
            mAdapter = OptionAdapter(list, indexClick)
            recyclerView!!.adapter = mAdapter
            txtTitle.text = title
        }
        imgClose.setOnClickListener {
            dismiss()
        }
    }

    override fun getContentView(): Int {
        return LayoutId
    }

    override fun clickIndex(pos: Int) {
        dismiss()
        callback.selected(pos)
        mAdapter.notifyDataSetChanged()
    }

}

