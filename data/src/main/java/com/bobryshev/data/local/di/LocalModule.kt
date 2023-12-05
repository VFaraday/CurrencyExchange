package com.bobryshev.data.local.di

import android.content.Context
import androidx.room.Room
import com.bobryshev.data.local.DataBase
import com.bobryshev.data.local.dao.UserDao
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
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideUserDao(dataBase: DataBase): UserDao {
        return dataBase.userDao()
    }
}