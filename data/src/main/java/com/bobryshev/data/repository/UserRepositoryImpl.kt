package com.bobryshev.data.repository

import com.bobryshev.data.datasource.local.UserDataSource
import com.bobryshev.domain.model.Balance
import com.bobryshev.domain.model.User
import com.bobryshev.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.bobryshev.data.local.model.Balance as BalanceLocal
import com.bobryshev.data.local.model.User as UserLocal

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
): UserRepository {

    override suspend fun getUser(): User {
        val lUser = userDataSource.getUser()
        return User(lUser.uid, lUser.currencyExchangesCount)
    }

    override suspend fun updateUser(user: User) {
        userDataSource.updateUser(UserLocal(user.id, user.countOfExchanges))
    }

    override suspend fun getBalance(): Flow<List<Balance>> {
        return userDataSource.getBalance().map { list ->
            list.map { Balance(it.uid, it.rate, it.value) }
        }
    }

    override suspend fun updateBalance(sellBalance: Balance, receiveBalance: Balance) {

        userDataSource.updateBalance(
            sellBalance = BalanceLocal(
            uid = sellBalance.id,
            rate = sellBalance.rate,
            value = sellBalance.value
            ),
            receiveBalance = BalanceLocal(
                uid = receiveBalance.id,
                rate = receiveBalance.rate,
                value = receiveBalance.value
            )
        )
    }
}