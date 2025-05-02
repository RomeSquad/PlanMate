package org.example.data.utils

import java.io.File
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

interface CsvFileWriter {
    fun writeCsv(
        file: File,
        data: List<List<String>>?,
        charset: Charset = StandardCharsets.UTF_8,
        header: List<String>? = null,
        append: Boolean = false,
        allowedExtensions: Set<String> = emptySet()
    )
}