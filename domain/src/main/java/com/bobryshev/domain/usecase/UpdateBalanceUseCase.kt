package com.bobryshev.domain.usecase

import com.bobryshev.domain.AbsUseCase
import com.bobryshev.domain.model.Balance
import com.bobryshev.domain.repository.UserRepository
import javax.inject.Inject

class UpdateBalanceUseCase @Inject constructor(
    private val userRepository: UserRepository
): AbsUseCase() {

    suspend operator fun invoke(sellBalance: Balance, receiveBalance: Balance) = runOnBackground {
        userRepository.updateBalance(sellBalance, receiveBalance)
    }
}