package com.itis.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.itis.core.db.dao.UserDao
import com.itis.core.db.entity.UserEntity

@Database(entities = [UserEntity::class], version = DatabaseHandler.versiondb)
abstract class InceptionDatabase: RoomDatabase() {

    abstract fun getUserDao(): UserDao

}
