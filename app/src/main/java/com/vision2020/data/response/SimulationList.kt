package com.vision2020.data.response

import BaseModel
class SimulationList:BaseModel{
    constructor(msg: String) {
        super.message = msg
    }
    constructor(statusCode: Int, message: String) {
        super.message = message
        super.status_code = statusCode
    }
    var `data`: Data?=null
    var http_code:String?=null

        data class Data(
            var givenDay: Int,
            var lowDosageWeek: Int,
            var lowDosageClass: Int,
            var midDosageWeek: Int,
            var midDosageClass: Int,
            var highDosageWeek: Int,
            var highDosageClass: Int,
            var simLowDosageYear: Int,
            var simLowDosageWeek: Int,
            var simLowDosageClass: String,
            var simMidDosageYear: Int,
            var simHighDosageYear: Int,
            var simHighDosageWeek: Int,
            var simHighDosageClass: String,
            var simMidDosageClass: String,
            var givenDate: String,
            var givenWeek: Int,
            var givenSemester: Int,
            var selectedWeek: Int,
            var id: Int,
            var userId: Int
        )
}
