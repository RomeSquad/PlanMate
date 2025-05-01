package data.utils

import org.example.data.utils.Parser

class ParserImpl : Parser {
    override fun parseCsv(content: String): List<List<String>> {
        if (content.isBlank()) return emptyList()

        val cleanedContent = content.trim().removeSurrounding("\"\"\"", "\"\"\"")
        return splitLines(cleanedContent)
            .filter { it.isNotBlank() }
            .map { parseCsvLine(it) }
    }

    override fun parseStringList(list: String): List<String> {
        val trimmedInput = list.trim()
        if (trimmedInput.isEmpty()) return emptyList()

        val cleanedInput = trimmedInput.removePrefix("[").removeSuffix("]")
        if (cleanedInput.isEmpty()) return emptyList()

        return when {
            cleanedInput.contains("[") -> parseCsvLine(cleanedInput)
            else -> cleanedInput.split(',')
                .map { it.trim().removeSurrounding("\"") }
        }
    }

    private fun splitLines(content: String): List<String> {
        val lines = mutableListOf<String>()
        val currentLine = StringBuilder()
        var insideQuotes = false

        content.forEach { char ->
            when {
                char == '"' -> {
                    insideQuotes = !insideQuotes
                    currentLine.append(char)
                }
                char == '\n' && !insideQuotes -> {
                    addLineIfNotEmpty(lines, currentLine)
                }
                else -> currentLine.append(char)
            }
        }

        addLineIfNotEmpty(lines, currentLine)
        return lines
    }

    private fun parseCsvLine(line: String): List<String> {
        if (line.isEmpty()) return emptyList()

        val fields = mutableListOf<String>()
        val currentField = StringBuilder()
        var insideQuotes = false
        var bracketDepth = 0

        line.forEach { char ->
            when {
                char == '[' -> {
                    bracketDepth++
                    currentField.append(char)
                }
                char == ']' -> {
                    bracketDepth--
                    currentField.append(char)
                }

                char == '"' && bracketDepth == 0 -> {
                    insideQuotes = !insideQuotes
                    currentField.append(char)
                }

                char == ',' && !insideQuotes && bracketDepth == 0 -> {
                    addField(fields, currentField)
                }

                else -> currentField.append(char)
            }
        }

        addField(fields, currentField)
        return fields
    }

    private fun addLineIfNotEmpty(lines: MutableList<String>, currentLine: StringBuilder) {
        if (currentLine.isNotEmpty()) {
            lines.add(currentLine.toString())
            currentLine.clear()
        }
    }

    private fun addField(fields: MutableList<String>, currentField: StringBuilder) {
        val fieldValue = currentField.toString().trim()
        if (fieldValue.isNotEmpty()) {
            fields.add(cleanField(fieldValue))
            currentField.clear()
        }
    }

    private fun cleanField(field: String): String {
        return if (field.startsWith("\"") && field.endsWith("\"")) {
            field.removeSurrounding("\"")
        } else {
            field
        }
    }
}