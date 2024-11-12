package com.example.mipt_5_praktinis.data


//https://www.floatrates.com/daily/usd.xml
data class CurrencyEntity(
    val pubDate: String,
    val baseCurrency: String,
    val baseName: String,
    val targetCurrency: String,
    val targetName: String,
    val exchangeRate: Float,
    val inverseRate: Float
)
