package com.vision2020.data.response

import BaseModel

class GroupMembers:BaseModel{
    var `data`: ArrayList<Data>?=null
    constructor(msg: String) {
        super.message = msg
    }
    constructor(statusCode: Int, message: String) {
        super.message = message
        super.status_code = statusCode
    }
    class Data(
        var first_name: String,
        var group_name: String,
        var student_id:Int,
        var id: Int,
        var isStatus: Int,
        var isActive: Int,
        var last_name: String,
        var leader_id: Int,
        var user_id: Int
    )
}
