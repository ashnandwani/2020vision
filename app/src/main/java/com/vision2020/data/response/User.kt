package com.vision2020.data.response
import BaseModel
class User:BaseModel{
    constructor(msg: String) {
        super.message = msg
    }
    constructor(statusCode: Int, message: String) {
        super.message = message
        super.status_code = statusCode
    }
    var `data`: Data? = null
    var http_code: String?=null
        data class Data(
            var deviceToken: String,
            var deviceType: String,
            var email: String,
            var id: Int,
            var is_account_verified: Int,
            var is_deleted: Int,
            var password: String,
            var user_access_token_id: String,
            var user_type: Int,
            var first_name:String,
            var last_name:String,
            var school_id:String,
            var class_id:String,
            var phone:String,
            var address:String,
            var photo:String,
            var otp_token:String
        )
}
