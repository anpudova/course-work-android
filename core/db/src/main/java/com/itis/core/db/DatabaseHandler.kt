package com.itis.core.db

import android.content.Context
import androidx.room.Room
import com.itis.core.db.entity.UserEntity
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Module
@InstallIn(SingletonComponent::class)
object DatabaseHandler {

    const val versiondb: Int = 1
    const val namedb: String = "dbF"
    private var roomDatabase: InceptionDatabase? = null

    fun provideDatabase(appContext: Context) {
        if (roomDatabase == null) {
            roomDatabase = Room.databaseBuilder(
                appContext,
                InceptionDatabase::class.java,
                namedb
            ).build()
        }
    }

    suspend fun createUser(user: UserEntity) {
        withContext(Dispatchers.IO) {
            roomDatabase?.getUserDao()?.createUser(user)
        }
    }

    suspend fun getUser(username: String, password: String): UserEntity? {
        return withContext(Dispatchers.IO) {
            roomDatabase?.getUserDao()?.getUser(username, password)
        }
    }

    suspend fun getUsername(username: String): String? {
        return withContext(Dispatchers.IO) {
            roomDatabase?.getUserDao()?.getUsername(username)
        }
    }

    suspend fun getUserByUsername(username: String): UserEntity? {
        return withContext(Dispatchers.IO) {
            roomDatabase?.getUserDao()?.getUserByUsername(username)
        }
    }

    suspend fun deleteUser(user: UserEntity) {
        withContext(Dispatchers.IO) {
            roomDatabase?.getUserDao()?.deleteUser(user)
        }
    }

    suspend fun updateUserRecord(username: String, newRecord: Int) {
        withContext(Dispatchers.IO) {
            roomDatabase?.getUserDao()?.updateRecordByUsername(username, newRecord)
        }
    }
}