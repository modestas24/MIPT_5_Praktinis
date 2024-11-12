package com.example.mipt_5_praktinis.data

import org.xml.sax.InputSource
import java.util.LinkedList
import javax.xml.parsers.SAXParser
import javax.xml.parsers.SAXParserFactory


class CurrencyParser {
    private val parserFactory: SAXParserFactory = SAXParserFactory.newInstance()
    private val parser: SAXParser = parserFactory.newSAXParser()

    fun parseXML(source: InputSource): List<CurrencyEntity> {
        try {
            val list: LinkedList<CurrencyEntity> = LinkedList()
            parser.parse(source, CurrencyHandler(list))
            return list
        } catch (e: Exception) {
            println(e)
            return emptyList()
        }
    }
}

