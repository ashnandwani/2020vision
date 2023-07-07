package com.vision2020.data.response

import BaseModel

class StudentListing:BaseModel{
    constructor(msg: String) {
        super.message = msg
    }
    constructor(statusCode: Int, message: String) {
        super.message = message
        super.status_code = statusCode
    }
    var `data`: ArrayList<Data>?=null
    var http_code: String?=null

    data class Data(
        var first_name: String,
        val last_name: String,
        var id: Int,
        var status:String,
        var isActive:Int,
        var moreExist: Int
    )
}



