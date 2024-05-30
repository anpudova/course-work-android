package com.itis.core.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users", indices = [Index("username", unique = true)])
data class UserEntity(
    @PrimaryKey val id: Long,
    val username: String,
    val password: String,
    val record: Int
)