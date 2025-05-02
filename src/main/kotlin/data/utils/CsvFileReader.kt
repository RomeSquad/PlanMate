package org.example.data.utils

import java.io.File
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

interface CsvFileReader {
    fun readCsv(file: File, charset: Charset = StandardCharsets.UTF_8): List<List<String>>
}