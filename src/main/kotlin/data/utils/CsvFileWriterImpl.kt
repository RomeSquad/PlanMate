package org.example.data.utils

import data.utils.CustomFile
import java.nio.charset.StandardCharsets

class CsvFileWriterImpl(
    private val validator: FileValidator = FileValidator()
) : CsvFileWriter {
    override fun writeCsv(
        csvFile: CustomFile,
        data: List<List<String>>?,
        header: List<String>?,
        append: Boolean
    ) {
        validator.validateFile(csvFile, isReadOperation = false, append = append)
        if (data == null && header == null) {
            return
        }
        validator.createParentDirsIfNeeded(csvFile.file)
        val content = buildString {
            header?.let { appendLine(it.joinToString(",")) }
            data?.forEach { appendLine(it.joinToString(",")) }
        }.trimEnd()
        if (append && csvFile.file.exists()) {
            csvFile.file.appendText("\n$content", csvFile.charset ?: StandardCharsets.UTF_8)
        } else {
            csvFile.file.writeText(content, csvFile.charset ?: StandardCharsets.UTF_8)
        }
    }

}