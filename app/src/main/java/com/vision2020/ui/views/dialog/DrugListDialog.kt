package com.vision2020.ui.views.dialog
import android.content.Context
import android.view.Gravity
import androidx.recyclerview.widget.LinearLayoutManager
import com.vision2020.adapter.DrugListAdapter
import com.vision2020.data.response.DrugList
import kotlinx.android.synthetic.main.dialog_options.*
class DrugListDialog(context: Context, themeResId: Int, private val LayoutId: Int, var list: ArrayList<DrugList.Data>,
    title: String,
    private val callback: ItemCallBack.Callback
) : BaseListDialog(context, themeResId) {
    private lateinit var mAdapter: DrugListAdapter
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
            mAdapter = DrugListAdapter(list, indexClick)
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

