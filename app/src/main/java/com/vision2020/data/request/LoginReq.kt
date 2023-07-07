package com.vision2020.data.request

data class LoginReq(
    var email:String,
    var password:String,
    var deviceType:String,
    var deviceToken:String
)