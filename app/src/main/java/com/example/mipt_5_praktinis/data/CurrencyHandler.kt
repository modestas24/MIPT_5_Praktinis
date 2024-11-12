package com.example.mipt_5_praktinis.data

import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import java.util.LinkedList


class CurrencyHandler(private val list: LinkedList<CurrencyEntity>) : DefaultHandler() {
    private val buffer: StringBuilder = StringBuilder()
    private val values: HashMap<String, String> = HashMap()
    private var read: Boolean = false

    override fun startElement(
        uri: String?,
        localName: String?,
        qName: String?,
        attributes: Attributes?
    ) {
        read = when (localName) {
            "pubDate" -> true
            "baseCurrency" -> true
            "baseName" -> true
            "targetCurrency" -> true
            "targetName" -> true
            "exchangeRate" -> true
            "inverseRate" -> true
            else -> false
        }
    }

    override fun endElement(
        uri: String?,
        localName: String?,
        qName: String?
    ) {
        read = false
        when (localName) {
            "item" -> {
                list.add(
                    CurrencyEntity(
                        values["pubDate"] as String,
                        values["baseCurrency"] as String,
                        values["baseName"] as String,
                        values["targetCurrency"] as String,
                        values["targetName"] as String,
                        (values["exchangeRate"] as String).toFloat(),
                        (values["inverseRate"] as String).toFloat(),
                    )
                )
            }

            "pubDate" -> {
                values["pubDate"] = buffer.toString()
                buffer.setLength(0)
            }

            "baseCurrency" -> {
                values["baseCurrency"] = buffer.toString()
                buffer.setLength(0)
            }

            "baseName" -> {
                values["baseName"] = buffer.toString()
                buffer.setLength(0)
            }

            "targetCurrency" -> {
                values["targetCurrency"] = buffer.toString()
                buffer.setLength(0)
            }

            "targetName" -> {
                values["targetName"] = buffer.toString()
                buffer.setLength(0)
            }

            "exchangeRate" -> {
                values["exchangeRate"] = buffer.toString()
                buffer.setLength(0)
            }

            "inverseRate" -> {
                values["inverseRate"] = buffer.toString()
                buffer.setLength(0)
            }
        }
    }

    override fun characters(
        ch: CharArray?,
        start: Int,
        length: Int
    ) {
        if (read) buffer.appendRange(ch!!, start, start + length)
    }
}