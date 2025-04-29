package org.example.data.utils

interface CsvParser {
    fun readCsv(
        csvFile: CsvFile,
        hasHeader: Boolean = true,
        delimiter: Char? = ',',
        skipEmptyLines: Boolean = true
    ): List<List<String>>

    fun writeCsv(
        csvFile: CsvFile,
        data: List<List<String>>?,
        header: List<String>? = null,
        append: Boolean = false
    )
}