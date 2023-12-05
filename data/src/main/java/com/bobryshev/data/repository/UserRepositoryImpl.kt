package com.bobryshev.data.repository

import com.bobryshev.data.datasource.local.UserDataSource
import com.bobryshev.domain.model.Balance
import com.bobryshev.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.random.Random

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
): UserRepository {

    override suspend fun getBalance(): Flow<List<Balance>> {
        return userDataSource.getBalance().map { list ->
            list.map { Balance(it.rate, it.value) }
        }
    }

    override suspend fun updateBalance(balance: Balance) {
        userDataSource.updateBalance(balance = com.bobryshev.data.local.model.Balance(
            uid = Random.nextInt(),
            rate = balance.rate,
            value = balance.value
        ))
    }
}