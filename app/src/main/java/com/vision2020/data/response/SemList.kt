package com.vision2020.data.response

import BaseModel
class SemList:BaseModel{
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
            var noOfDays: Int,
            var noOfWeeks: Int,
            var noOfSemester: Int,
            var year: Int,
            var id: Int,
            var userId: Int
        )
}
