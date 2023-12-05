package com.bobryshev.data.di

import com.bobryshev.data.datasource.local.UserDataSource
import com.bobryshev.data.datasource.remote.CurrencyDataSource
import com.bobryshev.data.local.impl.UserDataSourceImpl
import com.bobryshev.data.remote.impl.CurrencyDataSourceImpl
import com.bobryshev.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindCurrencyDataSource(impl: CurrencyDataSourceImpl): CurrencyDataSource

    @Binds
    abstract fun bindUserDataSource(impl: UserDataSourceImpl): UserDataSource
}