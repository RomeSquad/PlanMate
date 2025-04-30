package org.example.data.utils

import data.utils.CustomFile

interface IFileExtensionChecker {
    fun readCsv(
        csvFile: CustomFile,
        hasHeader: Boolean = true,
        delimiter: Char? = ',',
        skipEmptyLines: Boolean = true
    ): List<List<String>>

    fun writeCsv(
        csvFile: CustomFile,
        data: List<List<String>>?,
        header: List<String>? = null,
        append: Boolean = false
    )
}