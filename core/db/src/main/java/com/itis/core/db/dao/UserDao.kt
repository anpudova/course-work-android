package com.itis.core.db.dao

import androidx.room.*
import com.itis.core.db.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createUser(user: UserEntity)

    @Query("select * from users where username = :username and password = :password")
    suspend fun getUser(username: String, password: String): UserEntity?

    @Query("select username from users where username = :username")
    suspend fun getUsername(username: String): String?

    @Query("select * from users where username = :username")
    suspend fun getUserByUsername(username: String): UserEntity

    @Query("update users set record = :newRecord WHERE username = :username")
    suspend fun updateRecordByUsername(username: String, newRecord: Int): Int

    @Delete
    suspend fun deleteUser(user: UserEntity)
}