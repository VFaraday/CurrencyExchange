package com.bobryshev.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bobryshev.data.local.dao.UserDao
import com.bobryshev.data.local.model.Balance
import com.bobryshev.data.local.model.User

@Database(entities = [User::class, Balance::class], version = 2)
abstract class DataBase: RoomDatabase() {

    abstract fun userDao(): UserDao
}