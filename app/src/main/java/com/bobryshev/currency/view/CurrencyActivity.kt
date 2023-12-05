package com.bobryshev.currency.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import com.bobryshev.currency.view.mainscreen.CurrencyScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyActivity: AppCompatActivity() {
//
//    @Inject
//    lateinit var viewModel: CurrencyViewModel

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                CurrencyScreen()
            }
        }

//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.effect.collect {
//                    when(it) {
//                        is ShowToast -> Toast.makeText(this@CurrencyActivity, it.message, Toast.LENGTH_LONG).show()
//                    }
//                }
//            }
//        }
    }
}