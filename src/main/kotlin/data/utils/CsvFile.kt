package org.example.data.utils

import java.io.File
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

data class CsvFile(
    val fileName: String,
    val charset: Charset? = StandardCharsets.UTF_8
) {
    val file: File
        get() = File(fileName)

    init {
        require(fileName.isNotBlank()) { "File name must not be blank" }
    }
}