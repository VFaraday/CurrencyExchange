package com.bobryshev.domain.usecase

import com.bobryshev.domain.AbsUseCase
import com.bobryshev.domain.NetworkResult
import com.bobryshev.domain.model.Rate
import com.bobryshev.domain.repository.CurrencyRepository
import javax.inject.Inject

class CurrencyRateUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
): AbsUseCase() {

    suspend operator fun invoke(): NetworkResult<List<Rate>> = runOnBackground {
        currencyRepository.getCurrencyRates()
    }
}