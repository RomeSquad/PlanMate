package org.example.data.utils

import data.utils.CustomFile
import java.nio.charset.StandardCharsets

class CsvFileReaderImpl(
    private val parser: Parser = ParserImpl(),
    private val validator: FileValidator = FileValidator()
) : CsvFileReader {
    override fun readCsv(
        csvFile: CustomFile,
        hasHeader: Boolean,
        delimiter: Char?,
        skipEmptyLines: Boolean
    ): List<List<String>> {
        validator.validateFile(csvFile, isReadOperation = true)
        return parser.parse(
            csvFile.file.readText(csvFile.charset ?: StandardCharsets.UTF_8),
            hasHeader,
            delimiter ?: ',',
            skipEmptyLines
        )
    }
}