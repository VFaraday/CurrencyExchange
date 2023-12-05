package com.bobryshev.data.local.impl

import com.bobryshev.data.datasource.local.UserDataSource
import com.bobryshev.data.local.dao.UserDao
import com.bobryshev.data.local.model.Balance
import com.bobryshev.data.local.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val userDao: UserDao
): UserDataSource {
    override suspend fun getUser(): User {
        return userDao.getUser().firstOrNull() ?: let {
            val user = User(1)
            userDao.insertUser(user)
            userDao.insertBalance(Balance(1, "EUR", 1000.00f))
            user
        }
    }

    override suspend fun getBalance(): Flow<List<Balance>> {
        getUser()
        return userDao.getBalance()
    }

    override suspend fun updateBalance(balance: Balance) {
        userDao.insertBalance(balance)
    }
}