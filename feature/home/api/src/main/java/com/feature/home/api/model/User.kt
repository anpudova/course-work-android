package com.feature.home.api.model

data class User(
    val id: Long,
    val username: String,
    val password: String,
    val record: Int
)