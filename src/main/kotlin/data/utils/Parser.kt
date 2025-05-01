package org.example.data.utils

interface Parser {
    fun parseCsv(
        content: String,
    ): List<List<String>>
    fun parseStringList(list:String): List<String>
}
