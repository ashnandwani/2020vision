package com.vision2020.data.request

data class EditProfileReq(
    var tokenId: String,
    var first_name: String,
    var last_name: String,
    var address: String,
    var phone: String

)