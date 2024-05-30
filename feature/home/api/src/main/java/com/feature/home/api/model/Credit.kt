package com.feature.home.api.model

data class Credit(
    var id: Long,
    var sum: Int,
    var percent: Int,
    var years: Int,
    var paymentPerYear: Float,
    var step: Int
)
