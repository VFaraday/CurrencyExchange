package com.bobryshev.data.datasource.local

import com.bobryshev.data.local.model.Balance
import com.bobryshev.data.local.model.User
import kotlinx.coroutines.flow.Flow

interface UserDataSource {

    suspend fun getUser(): User

    suspend fun updateUser(user: User)

    suspend fun getBalance(): Flow<List<Balance>>

    suspend fun updateBalance(balance: Balance)
}