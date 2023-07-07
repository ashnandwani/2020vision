package com.vision2020.data.request
data class SignUpReq(
    var user_type: String,
    var first_name: String,
    var last_name: String,
    var school_id:String,
    var student_id: String,
    var class_id: String,
    var phone: String,
    var email: String,
    var password: String,
    var address: String
)