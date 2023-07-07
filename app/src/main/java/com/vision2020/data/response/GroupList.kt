package com.vision2020.data.response

import BaseModel
class GroupList:BaseModel{
    constructor(msg: String) {
        super.message = msg
    }
    constructor(statusCode: Int, message: String) {
        super.message = message
        super.status_code = statusCode
    }
    var `data`: ArrayList<Data>?=null
    var http_code:String?=null

        data class Data(
            var group_name: String,
            var narcan: String,
            var subjectpassed: String,
            var user_id: String,
            var id: Int,
            var isStatus: Int
        )
}
