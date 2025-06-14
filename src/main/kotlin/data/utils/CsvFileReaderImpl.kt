package org.example.data.utils

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.nio.charset.Charset

class CsvFileReaderImpl(
    private val parser: Parser ,
    private val validator: FileValidator
) : CsvFileReader {
    override fun readCsv(file: File, charset: Charset): List<List<String>> {
        validator.validateFile(file, allowedExtensions = setOf("csv"), isReadOperation = true)

        try {
            BufferedReader(FileReader(file, charset)).use { reader ->
                val content = reader.readText()
                return parser.parseCsv(content)
            }
        } catch (e: IOException) {
            throw IOException("Failed to read file: ${file.absolutePath}", e)
        }
    }
}