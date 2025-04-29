package org.example.data.utils

class CsvParserImpl : CsvParser {
    override fun readCsv(
        csvFile: CsvFile,
        hasHeader: Boolean,
        delimiter: Char?,
        skipEmptyLines: Boolean
    ): List<List<String>> {
        TODO("Not yet implemented")
    }

    override fun writeCsv(
        csvFile: CsvFile,
        data: List<List<String>>?,
        header: List<String>?,
        append: Boolean
    ) {
        TODO("Not yet implemented")
    }

}