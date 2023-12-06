package com.bobryshev.domain.repository

import com.bobryshev.domain.model.Balance
import com.bobryshev.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getUser(): User

    suspend fun updateUser(user: User
    )
    suspend fun getBalance(): Flow<List<Balance>>

    suspend fun updateBalance(sellBalance: Balance, receiveBalance: Balance)
}