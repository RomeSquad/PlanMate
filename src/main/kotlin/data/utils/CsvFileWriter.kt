package org.example.data.utils

import data.utils.CustomFile

interface CsvFileWriter {
    fun writeCsv(
        csvFile: CustomFile,
        data: List<List<String>>?,
        header: List<String>? = null,
        append: Boolean = false
    )
}