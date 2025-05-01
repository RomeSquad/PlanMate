package org.example.data.utils

class ParserImpl : Parser {
    override fun parseCsv(content: String): List<List<String>> {
        if (content.isEmpty()) return emptyList()

        val cleanedContent = content.trim().removeSurrounding("""""", """""")
        val lines = splitLines(cleanedContent)
        return lines
            .filter(String::isNotBlank)
            .map(::parseCsvLine)
    }

    override fun parseStringList(list: String): List<String> {
        val trimmedList = list.trim()
        if (trimmedList.isEmpty()) return emptyList()

        val cleanedList = trimmedList.removePrefix("[").removeSuffix("]")
        if (cleanedList.isEmpty()) return emptyList()

        if (cleanedList.startsWith("[")) {
            return parseCsvLine(cleanedList.removePrefix("[").removeSuffix("]"))
        }

        return cleanedList.split(',')
            .map { it.trim().removeSurrounding("\"") }
    }

    private fun splitLines(content: String): List<String> {
        val lines = mutableListOf<String>()
        val currentLine = StringBuilder()
        var insideQuotes = false

        for (i in content.indices) {
            val char = content[i]
            when {
                char == '"' -> {
                    insideQuotes = !insideQuotes
                    currentLine.append(char)
                }
                char == '\n' && !insideQuotes -> {
                    if (currentLine.isNotEmpty()) {
                        lines.add(currentLine.toString())
                        currentLine.clear()
                    }
                }
                else -> currentLine.append(char)
            }
        }

        if (currentLine.isNotEmpty()) {
            lines.add(currentLine.toString())
        }

        return lines
    }

    private fun parseCsvLine(line: String): List<String> {
        if (line.isEmpty()) return emptyList()

        val columns = mutableListOf<String>()
        val currentField = StringBuilder()
        var insideQuotes = false
        var insideBrackets = 0

        for (char in line) {
            when {
                char == '[' -> {
                    insideBrackets++
                    currentField.append(char)
                }
                char == ']' -> {
                    insideBrackets--
                    currentField.append(char)
                }
                char == '"' && insideBrackets == 0 -> {
                    insideQuotes = !insideQuotes
                    currentField.append(char)
                }
                char == ',' && !insideQuotes && insideBrackets == 0 -> {
                    val field = currentField.toString().trim()
                    if (field.startsWith("[") && field.endsWith("]")) {
                        columns.addAll(parseStringList(field))
                    } else {
                        val cleanedField = if (field.startsWith("\"") && field.endsWith("\"")) {
                            field.removeSurrounding("\"")
                        } else {
                            field
                        }
                        columns.add(cleanedField)
                    }
                    currentField.clear()
                }
                else -> currentField.append(char)
            }
        }

        val field = currentField.toString().trim()
        if (field.isNotEmpty()) {
            if (field.startsWith("[") && field.endsWith("]")) {
                columns.addAll(parseStringList(field))
            } else {
                val cleanedField = if (field.startsWith("\"") && field.endsWith("\"")) {
                    field.removeSurrounding("\"")
                } else {
                    field
                }
                columns.add(cleanedField)
            }
        }

        return columns
    }
}