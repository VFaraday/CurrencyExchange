package com.bobryshev.domain.repository

import com.bobryshev.domain.model.Balance
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getBalance(): Flow<List<Balance>>

    suspend fun updateBalance(balance: Balance)
}