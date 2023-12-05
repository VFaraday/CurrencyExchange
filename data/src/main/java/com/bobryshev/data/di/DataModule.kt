package com.bobryshev.data.di

import com.bobryshev.data.repository.CurrencyRepositoryImpl
import com.bobryshev.data.repository.UserRepositoryImpl
import com.bobryshev.domain.repository.CurrencyRepository
import com.bobryshev.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindCurrencyRepository(impl: CurrencyRepositoryImpl): CurrencyRepository

    @Binds
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}