package com.vision2020.data.response


import BaseModel

class ExpResultResponse : BaseModel {
    constructor(msg: String) {
        super.message = msg
    }

    constructor(statusCode: Int, message: String) {
        super.message = message
        super.status_code = statusCode
    }

    var `data`: ArrayList<Data>? = null
    var http_code: String? = null

    data class Data(
        val id: String,
        val userId: String,
        val expId: String,
        val labelId: String,
        val startDate: String,
        val day_1: String,
        val day_2: String,
        val day_3: String,
        val day_4: String,
        val day_5: String,
        val day_6: String,
        val day_7: String
    )

}