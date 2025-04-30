package org.example.data.utils

import data.utils.CustomFile

interface CsvFileReader {
    fun readCsv(
        csvFile: CustomFile,
        hasHeader: Boolean = true,
        delimiter: Char? = ',',
        skipEmptyLines: Boolean = true
    ): List<List<String>>
}