@file:OptIn(ExperimentalMaterial3Api::class)

package com.bobryshev.currency.view.mainscreen

import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bobryshev.currency.R
import com.bobryshev.currency.base.DialogData
import com.bobryshev.currency.base.UiIntent
import com.bobryshev.currency.utils.Constants
import com.bobryshev.currency.utils.CurrencyAmountInputVisualTransformation
import com.bobryshev.currency.utils.bitTextStyle
import com.bobryshev.currency.utils.lightTextStyle
import com.bobryshev.currency.utils.textStyle
import com.bobryshev.domain.model.Balance
import com.bobryshev.domain.model.Rate
import kotlin.random.Random

@ExperimentalMaterial3Api
@Composable
fun CurrencyScreen(
    viewModel: CurrencyViewModel = hiltViewModel(),
) {

    val context = LocalContext.current
    var dialogData: DialogData? = null

    val uiState by viewModel.uiState.collectAsState()
    val balance by uiState.userBalance.collectAsState(initial = emptyList())

    val openAlertDialog = remember { mutableStateOf(false) }

    openAlertDialog.value = uiState.openAlertDialog

    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when(it) {
                is ShowToast -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    viewModel.handleUiEvent(LoadData)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.currency_converter),
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
        if (openAlertDialog.value) {
            uiState.dialogData?.let { dialog ->
                AlertDialog(dialogData = dialog)
            }
        }
        Column(modifier = Modifier
            .padding(it)
            .padding(all = 16.dp)) {
            MyBalance(balance)
            Spacer(modifier = Modifier.height(24.dp))
            CurrencyExchange(
                balance = balance,
                rates = uiState.rates,
                receiveValue = uiState.receiveValue,
                viewModel::handleUiEvent
            )
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
                onClick = { viewModel.handleUiEvent(Exchange) },
            ) {
                Text(text = "Submit".uppercase())
            }
        }
    }
}

@Composable
fun MyBalance(list: List<Balance>) {
    Column {
        Text(text = stringResource(R.string.my_balance).uppercase(), style = lightTextStyle)
        Spacer(modifier = Modifier.height(14.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = list,
                key = { it.id }
            ) {
                MyBalanceItem(balance = it)
            }
        }
    }
}

@Composable
fun MyBalanceItem(balance: Balance) {
    Row {
        Text(text = balance.value.toString(), style = bitTextStyle)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = balance.rate, style = bitTextStyle)
    }
}

@Composable
fun CurrencyExchange(
    balance: List<Balance>,
    rates: List<Rate>,
    receiveValue: Double,
    onEvent: (UiIntent) -> Unit
) {
    Column {
        Text(text = stringResource(R.string.currency_exchange).uppercase(), style = lightTextStyle)
        Sell(rates = balance.map { it.rate }, onEvent)
        Divider(modifier = Modifier.padding(top = 8.dp, bottom = 16.dp))
        Receive(rates, receiveValue, onEvent)
    }
}

@Composable
fun Sell(rates: List<String>, onEvent: (UiIntent) -> Unit) {
    var text by remember { mutableStateOf("") }
    val visualTransformation = CurrencyAmountInputVisualTransformation()

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
        Text(text = stringResource(R.string.sell), style = textStyle)
        Spacer(modifier = Modifier.weight(1f))
        TextField(
            value = text,
            onValueChange = {
                text = if (it.startsWith("0")) {
                    ""
                } else {
                    it
                }
                onEvent(
                    UpdateSell(
                        visualTransformation
                            .filter(AnnotatedString(text)
                            )
                            .text
                            .text.toDouble()
                    )
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.White,
                focusedIndicatorColor = Color.White,
            ),
            textStyle = textStyle.copy(textAlign = TextAlign.End),
            modifier = Modifier
                .wrapContentWidth(Alignment.End)
                .weight(1f),
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword
            )
        )
        Spacer(modifier = Modifier.width(4.dp))
        Box(modifier = Modifier
            .weight(1f)) {
            DropdownRate(rates, true) {
                onEvent(UpdateSellRate(it))
            }
        }
    }
}

@Composable
fun Receive(rates: List<Rate>, receiveValue: Double,  onEvent: (UiIntent) -> Unit) {
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
        Text(text = stringResource(R.string.receive), style = textStyle)
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = if(receiveValue.toString().isEmpty() || receiveValue.toString() == "0.0") {
                receiveValue.toString()
            } else {
                "+${receiveValue}"
            },
            modifier = Modifier
                .wrapContentWidth(Alignment.End)
                .weight(0.5f),
            maxLines = 1,
            style = textStyle.copy(
                color = if (receiveValue.toString().isEmpty() || receiveValue.toString() == "0.0") {
                    Color.Black
                } else {
                    colorResource(id = R.color.receiveText)
                }
            )
            )
        Spacer(modifier = Modifier.width(4.dp),)
        Box(modifier = Modifier
            .weight(1f)) {
            DropdownRate(rates.map { it.rateName }, false) {
                onEvent(UpdateReceiveRate(it))
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun DropdownRate(
    list: List<String>,
    isSell: Boolean,
    onClick: (value: String) -> Unit
) {
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
            if (isSell) {
                list.firstOrNull { it == Constants.EUR }?.let { text ->
                    DropdownMenuItem(
                        text = { Text(text = text) },
                        onClick = {
                            selectedText = text
                            expanded = false
                            onClick(text)
                        }
                    )
                }
            } else {
                list.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedText = item
                            expanded = false
                            onClick(item)
                        }
                    )
                }
            }

            //TODO:uncomit if back return more rates for sell
//            list.forEach { item ->
//                DropdownMenuItem(
//                    text = { Text(text = item) },
//                    onClick = {
//                        selectedText = item
//                        expanded = false
//                    }
//                )
//            }
        }
    }
}

@Preview
@Composable
fun MyBalancePreview() {
    MyBalance(list = listOf(Balance(Random.nextInt(), Constants.EUR, 1000.00),
        Balance(Random.nextInt(), "USD", 0.00)))
}

@Preview
@Composable
fun SellPreview() {
    Sell(emptyList(), {})
}

@Preview
@Composable
fun ReceivePreview() {
    Receive(emptyList(), 0.00) {}
}

@Composable
fun AlertDialog(
    dialogData: DialogData
) {
    AlertDialog(
        onDismissRequest = {
            dialogData.onDismissRequest()
        },
        title = dialogData.title?.let {
            { Text(text = it) }
        },
        text = dialogData.text?.let{
            { Text(text = it) }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    dialogData.onConfirmation()
                }) {
                Text(stringResource(id = dialogData.positiveBtnRes))
            }
        },
        dismissButton = {
            dialogData.negativeBtnRes?.let {
                TextButton(
                    onClick = {
                        dialogData.onDismissRequest()
                    }) {
                    Text(stringResource(id = dialogData.negativeBtnRes))
                }
            }
        }
    )
}