package com.vision2020.data.response

import BaseModel
class DrugList:BaseModel{
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
            var dgName: String,
            var id: Int,
            var isStatus: Int
        )
}


