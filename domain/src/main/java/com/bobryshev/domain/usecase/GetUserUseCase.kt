package com.bobryshev.domain.usecase

import com.bobryshev.domain.AbsUseCase
import com.bobryshev.domain.model.User
import com.bobryshev.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
): AbsUseCase() {

    suspend operator fun invoke(): User = runOnBackground {
        userRepository.getUser()
    }
}