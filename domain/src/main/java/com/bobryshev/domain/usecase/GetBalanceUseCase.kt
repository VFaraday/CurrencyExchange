package com.bobryshev.domain.usecase

import com.bobryshev.domain.AbsUseCase
import com.bobryshev.domain.model.Balance
import com.bobryshev.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBalanceUseCase @Inject constructor(
    private val userRepository: UserRepository
): AbsUseCase() {

    suspend operator fun invoke(): Flow<List<Balance>> = runOnBackground {
        userRepository.getBalance()
    }
}