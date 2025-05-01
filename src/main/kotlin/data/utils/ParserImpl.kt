package org.example.data.utils

class ParserImpl : Parser {

    private fun splitAndFilterLine(line: String, delimiter: Char): List<String> {
        return line.split(delimiter)
            .map(String::trim)
            .filter(String::isNotEmpty)
    }

    private fun validateInput(content: String, delimiter: Char?) {
        if (delimiter == null) {
            throw IllegalArgumentException("Delimiter cannot be null")
        }
        if (content.isNotEmpty() && !content.contains(delimiter)) {
            throw IllegalArgumentException("Invalid delimiter '$delimiter': not found in content")
        }
    }

    private fun parseLines(
        lines: List<String>,
        delimiter: Char,
        skipEmptyLines: Boolean,
        hasHeader: Boolean
    ): Pair<List<String>, List<List<String>>> {
        val header = mutableListOf<String>()
        val data = mutableListOf<List<String>>()

        lines.forEachIndexed { index, line ->
            if (skipEmptyLines && line.trim().isEmpty()) {
                return@forEachIndexed
            }

            val columns = splitAndFilterLine(line, delimiter)
            if (skipEmptyLines && columns.isEmpty()) {
                return@forEachIndexed
            }

            if (hasHeader && index == 0) {
                header.addAll(columns)
            } else {
                data.add(columns)
            }
        }

        return Pair(header, data)
    }

    override fun parse(
        content: String,
        hasHeader: Boolean,
        delimiter: Char?,
        skipEmptyLines: Boolean
    ): List<List<String>> {
        if (content.isEmpty()) {
            return emptyList()
        }
        validateInput(content, delimiter)
        val lines = content.lines()
        val (_, data) = parseLines(lines, delimiter!!, skipEmptyLines, hasHeader)
        return data
    }

    override fun parseWithHeader(
        content: String,
        delimiter: Char?,
        skipEmptyLines: Boolean
    ): Pair<List<String>, List<List<String>>> {
        if (content.isEmpty()) {
            return Pair(emptyList(), emptyList())
        }
        validateInput(content, delimiter)
        val lines = content.lines()
        return parseLines(lines, delimiter!!, skipEmptyLines, hasHeader = true)
    }
}