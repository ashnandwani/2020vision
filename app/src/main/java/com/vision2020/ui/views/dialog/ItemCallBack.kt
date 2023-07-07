package com.vision2020.ui.views.dialog

class ItemCallBack {
    interface IndexClick{
        fun clickIndex(pos: Int)
    }

    interface Callback {
        fun selected(pos: Int)
    }

    interface BtnClick {
        fun onClick()
    }

}