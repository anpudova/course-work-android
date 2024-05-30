package com.feature.profile.api.model

data class User(
    val id: Long,
    val username: String,
    val password: String,
    val record: Int
)