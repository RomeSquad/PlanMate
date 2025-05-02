package org.example.data.utils

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.nio.charset.Charset

class CsvFileWriterImpl(
    private val validator: FileValidator = FileValidator()
) : CsvFileWriter {
    override fun writeCsv(
        file: File,
        data: List<List<String>>?,
        charset: Charset,
        header: List<String>?,
        append: Boolean,
        allowedExtensions: Set<String>
    ) {
        validator.validateFile(file, allowedExtensions, isReadOperation = false)

        try {
            BufferedWriter(FileWriter(file, charset, append)).use { writer ->
                val shouldWriteHeader = !append || !file.exists()
                if (shouldWriteHeader && header != null && header.isNotEmpty()) {
                    writer.write(header.joinToString(","))
                    writer.newLine()
                }

                data?.forEach { row ->
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
}