package org.example.data.utils

import data.utils.CustomFile

interface CsvFileReader {
    fun readCsv(
        csvFile: CustomFile,
    ): List<List<String>>
}