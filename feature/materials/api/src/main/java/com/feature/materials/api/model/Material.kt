package com.feature.materials.api.model

import android.accounts.AuthenticatorDescription

data class Material (
    val id: Long,
    val name: String,
    val definition: String,
    val explanation: String
)