package org.example.data.utils

interface Parser {
    fun parse(
        content: String,
        hasHeader: Boolean,
        delimiter: Char?,
        skipEmptyLines: Boolean
    ): List<List<String>>

    fun parseWithHeader(
        content: String,
        delimiter: Char?,
        skipEmptyLines: Boolean
    ): Pair<List<String>, List<List<String>>>
}
