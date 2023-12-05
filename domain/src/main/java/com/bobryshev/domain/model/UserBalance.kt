package com.bobryshev.domain.model

data class UserBalance(
    val userName: String,
    val balance: List<Balance>
)