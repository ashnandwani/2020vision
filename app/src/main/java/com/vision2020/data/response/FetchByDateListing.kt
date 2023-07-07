package com.vision2020.data.response

import BaseModel

class FetchByDateListing : BaseModel{
    constructor(msg: String) {
        super.message = msg
    }
    constructor(statusCode: Int, message: String) {
        super.message = message
        super.status_code = statusCode
    }
    var data: Data?=null
    var http_code:String?=null
    var isExist:String?=null
    var type:String?=null
        data class Data(
            val expName: String,
            val expDate: String,
            val expTime: String,
            val expLength: String,
            val vapeTime: String,
            val totalVapes: String,
            val expCycle: String,
            val waitTime: String,
            val drugType: String,
            val drugCalc: String,
            val watts: String,
            val temprature: String,
            val drugConcetration: String,
            val lungs: String,
            val expEndTime: String,
            val groups: ArrayList<Group>
        )

    data class Group(
        val id: String,
        val userid: String,
        val expId: String,
        val group_id: String,
        val narcan: String,
        val subjectpassed: String
    )
}