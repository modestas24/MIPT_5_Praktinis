package com.example.mipt_5_praktinis.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import org.xml.sax.InputSource
import java.io.StringReader


//https://www.floatrates.com/daily/usd.xml
object CurrencyLoader {
    private const val URL = "https://www.floatrates.com/daily/usd.xml"
    private val client = HttpClient(Android) {
        install(HttpTimeout) {
            requestTimeoutMillis = 3000
            connectTimeoutMillis = 3000
            socketTimeoutMillis = 3000
        }
    }

    suspend fun fetchCurrencyList(): List<CurrencyEntity> {
        try {
            val response = client.get(URL).bodyAsText()
            return CurrencyParser().parseXML(InputSource(StringReader(response)))
        } catch (e: Exception) {
            println(e)
            return emptyList()
        }

    }
}