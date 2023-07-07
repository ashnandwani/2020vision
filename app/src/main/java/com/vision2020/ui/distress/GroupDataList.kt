package com.vision2020.ui.distress

class GroupDataList(var id: String, var name: String) {



    //to display object as a string in spinner
    override fun toString(): String {
        return name
    }

    override fun equals(obj: Any?): Boolean {
        if (obj is GroupDataList) {
            val c = obj
            if (c.name == name && c.id === id) return true
        }
        return false
    }




}