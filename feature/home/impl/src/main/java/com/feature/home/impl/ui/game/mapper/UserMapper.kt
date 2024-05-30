package com.feature.home.impl.ui.game.mapper

import com.feature.home.api.model.User
import com.itis.core.db.entity.UserEntity

object UserMapper {

    fun mapUserEntity(user: User): UserEntity {
        with(user) {
            return UserEntity(
                id, username, password, record
            )
        }
    }

    fun mapUserModel(user: UserEntity?): User? {
        return user?.let {
            User(it.id, it.username, it.password, it.record)
        }
    }
}