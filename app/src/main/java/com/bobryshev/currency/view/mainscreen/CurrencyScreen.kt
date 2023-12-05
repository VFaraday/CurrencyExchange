@file:OptIn(ExperimentalMaterial3Api::class)

package com.bobryshev.currency.view.mainscreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bobryshev.currency.R
import com.bobryshev.domain.model.Balance
import com.bobryshev.domain.model.Rate

@ExperimentalMaterial3Api
@Composable
fun CurrencyScreen(
    viewModel: CurrencyViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsState()
    val balance by uiState.userBalance.collectAsState(initial = emptyList())

    viewModel.handleUiEvent(LoadData)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Currency converter",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.primary),
                    titleContentColor = colorResource(id = R.color.white)
                )
            )
        }
    ) {
        Column(modifier = Modifier
            .padding(it)
            .padding(all = 16.dp)) {
            MyBalance(balance)
            Spacer(modifier = Modifier.height(24.dp))
            CurrencyExchange(balance = balance, rates = uiState.rates)
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                colorResource(id = R.color.colorPrimaryVariant),
                                colorResource(id = R.color.primary)
                            )
                        )
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                onClick = { /*TODO*/ },
            ) {
                Text(text = "Submit".uppercase())
            }
        }
    }
}

@Composable
fun MyBalance(list: List<Balance>) {
    Column {
        Text(text = "My balance".uppercase())
        Spacer(modifier = Modifier.height(14.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = list,
                key = { it.value }
            ) {
                MyBalanceItem(balance = it)
            }
        }
    }
}

@Composable
fun MyBalanceItem(balance: Balance) {
    Row {
        Text(text = balance.value.toString())
        Text(text = balance.rate)
    }
}

@Composable
fun CurrencyExchange(balance: List<Balance>, rates: List<Rate>) {
    Column {
        Text(text = "Currency Exchange".uppercase())
        Sell(rates = balance.map { it.rate })
        Divider(modifier = Modifier.padding(top = 8.dp, bottom = 16.dp))
        Receive(rates)
    }
}

@Composable
fun Sell(rates: List<String>) {
    var text by remember { mutableStateOf("0.00") }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = R.drawable.arrow_up),
            contentDescription = "",
            tint = Color.White,
            modifier = Modifier
                .background(colorResource(id = R.color.sell), shape = CircleShape)
                .padding(all = 4.dp)
                .size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Sell")
        Spacer(modifier = Modifier.weight(1f))
        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.White,
                focusedIndicatorColor = Color.White,
            ),
            modifier = Modifier
                .wrapContentWidth(Alignment.End)
                .weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Box(modifier = Modifier
            .weight(1f)) {
            DropdownRate(rates) {}
        }
    }
}

@Composable
fun Receive(rates: List<Rate>) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = R.drawable.arrow_down),
            contentDescription = "",
            tint = Color.White,
            modifier = Modifier
                .background(colorResource(id = R.color.receive), shape = CircleShape)
                .padding(all = 4.dp)
                .size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Receive")
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "100.00")
        Spacer(modifier = Modifier.width(4.dp))
        Box(modifier = Modifier
            .weight(1f)) {
            DropdownRate(rates.map { it.rateName }) {}
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun DropdownRate(list: List<String>, onClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(list.firstOrNull().orEmpty()) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = it
        }) {
        TextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.White,
                focusedIndicatorColor = Color.White,
            ),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            list.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        selectedText = item
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun MyBalancePreview() {
    MyBalance(list = listOf(Balance("EUR", 1000.00f), Balance("USD", 0.00f)))
}

@Preview
@Composable
fun SellPreview() {
    Sell(emptyList())
}

@Preview
@Composable
fun ReceivePreview() {
    Receive(emptyList())
}