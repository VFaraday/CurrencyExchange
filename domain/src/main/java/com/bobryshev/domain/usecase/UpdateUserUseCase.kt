package com.bobryshev.domain.usecase

import com.bobryshev.domain.AbsUseCase
import com.bobryshev.domain.model.User
import com.bobryshev.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
): AbsUseCase() {

    suspend operator fun invoke(user: User) = runOnBackground {
        userRepository.updateUser(user)
    }
}