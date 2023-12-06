package com.bobryshev.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.bobryshev.data.local.model.Balance
import com.bobryshev.data.local.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    suspend fun getUser(): List<User>

    @Query("SELECT * FROM balance")
    fun getBalance(): Flow<List<Balance>>

    @Upsert
    suspend fun insertBalance(balance: Balance)

    @Upsert
    suspend fun insertUser(user: User)

    @Delete
    suspend fun delete(user: User)
}