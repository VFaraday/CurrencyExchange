package com.bobryshev.data.local.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bobryshev.data.local.DataBase
import com.bobryshev.data.local.dao.UserDao
import com.bobryshev.data.local.model.User
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    fun provideDb(@ApplicationContext context: Context): DataBase {
        return Room.databaseBuilder(
            context,
            DataBase::class.java,
            "database")
            .build()
    }

    @Provides
    fun provideUserDao(dataBase: DataBase): UserDao {
        return dataBase.userDao()
    }
}