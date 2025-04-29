package org.example.data.utils

import java.io.*
import java.nio.charset.Charset

open class CsvParserImpl : CsvParser {

    override fun readCsv(
        csvFile: CsvFile,
        hasHeader: Boolean,
        delimiter: Char?,
        skipEmptyLines: Boolean
    ): List<List<String>> {
        if (delimiter == null) {
            throw IllegalArgumentException("Delimiter cannot be null")
        }

        val file = csvFile.file
        if (!file.exists()) {
            throw IOException("File does not exist: ${file.absolutePath}")
        }

        val charset = csvFile.charset ?: Charset.defaultCharset()
        val result = mutableListOf<List<String>>()

        try {
            BufferedReader(FileReader(file, charset)).use { reader ->
                var firstLine: String? = reader.readLine()
                if (firstLine == null) {
                    return emptyList() // Empty file
                }

                if (!firstLine.contains(delimiter)) {
                    throw IllegalArgumentException("Invalid delimiter '$delimiter': does not match file content")
                }

                reader.close()
                BufferedReader(FileReader(file, charset)).use { newReader ->
                    var line: String?
                    var isFirstLine = true

                    while (true) {
                        line = newReader.readLine()
                        if (line == null) {
                            break
                        }
                        if (skipEmptyLines && line.trim().isEmpty()) {
                            continue
                        }

                        if (isFirstLine && hasHeader) {
                            isFirstLine = false
                            continue
                        }

                        isFirstLine = false

                        val columns = line.split(delimiter)
                            .map { it.trim() }
                            .filter { it.isNotEmpty() }
                        result.add(columns)
                    }
                }
            }
        } catch (e: IOException) {
            throw IOException("Failed to read file: ${file.absolutePath}", e)
        }

        return result
    }

    override fun writeCsv(
        csvFile: CsvFile,
        data: List<List<String>>?,
        header: List<String>?,
        append: Boolean
    ) {
        if (data == null) {
            throw IllegalArgumentException("Data cannot be null")
        }

        val file = csvFile.file
        val charset = csvFile.charset ?: Charset.defaultCharset()

        val parentDir = file.parentFile
        ensureParentDirectories(parentDir)

        val shouldWriteHeader = !append || !file.exists()

        try {
            BufferedWriter(FileWriter(file, charset, append)).use { writer ->
                if (shouldWriteHeader && header != null && header.isNotEmpty()) {
                    writer.write(header.joinToString(","))
                    writer.newLine()
                }

                data.forEach { row ->
                    if (row.isNotEmpty()) {
                        writer.write(row.joinToString(","))
                        writer.newLine()
                    }
                }
            }
        } catch (e: IOException) {
            throw IOException("Failed to write file: ${file.absolutePath}", e)
        }
    }

    protected open fun ensureParentDirectories(parentDir: File?) {
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw IOException("Failed to create directories: ${parentDir.absolutePath}. Ensure the parent directory is writable and the path is valid.")
            }
        }
    }
}