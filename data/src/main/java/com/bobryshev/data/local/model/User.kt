package com.bobryshev.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey
    val uid: Int,
    @ColumnInfo(name = "currencyExchangesCount")
    val currencyExchangesCount: Int
)