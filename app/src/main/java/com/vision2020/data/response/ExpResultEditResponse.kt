package com.vision2020.data.response

data class ExpResultEditResponse(
    val status_code: Int,
    val message: String,
    val experId: String,
    val data: ExpResultData
)