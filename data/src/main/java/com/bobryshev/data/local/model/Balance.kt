package com.bobryshev.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Balance(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val uid: Int,
    @ColumnInfo(name = "Rate")
    val rate: String,
    @ColumnInfo(name = "BalanceValue")
    val value: Double
)