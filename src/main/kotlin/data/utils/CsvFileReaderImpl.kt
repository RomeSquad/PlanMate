package org.example.data.utils

import data.utils.CustomFile
import java.nio.charset.StandardCharsets

class CsvFileReaderImpl(
    private val parser: Parser = ParserImpl(),
    private val validator: FileValidator = FileValidator()
) : CsvFileReader {
    override fun readCsv(
        csvFile: CustomFile,
    ): List<List<String>> {
        validator.validateFile(csvFile, isReadOperation = true)
        return parser.parseCsv(
            csvFile.file.readText(csvFile.charset ?: StandardCharsets.UTF_8),
        )
    }
}