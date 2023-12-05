package com.bobryshev.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bobryshev.data.local.dao.UserDao
import com.bobryshev.data.local.model.Balance
import com.bobryshev.data.local.model.User

@Database(entities = [User::class, Balance::class], version = 1)
abstract class DataBase: RoomDatabase() {

    abstract fun userDao(): UserDao
}