package data.utils

import org.example.data.utils.IFileExtensionChecker
import org.example.data.utils.Parser
import org.example.data.utils.ParserImpl
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets

class FileExtensionChecker(private val parser: Parser = ParserImpl()) : IFileExtensionChecker {

    override fun readCsv(
        csvFile: CustomFile,
        hasHeader: Boolean,
        delimiter: Char?,
        skipEmptyLines: Boolean
    ): List<List<String>> {
        validateFile(csvFile, isReadOperation = true)
        return parser.parse(
            csvFile.file.readText(csvFile.charset ?: StandardCharsets.UTF_8),
            hasHeader,
            delimiter ?: ',',
            skipEmptyLines
        )
    }

    override fun writeCsv(
        csvFile: CustomFile,
        data: List<List<String>>?,
        header: List<String>?,
        append: Boolean
    ) {
        validateFile(csvFile, isReadOperation = false, append = append)
        if (data == null && header == null) {
            return
        }
        createParentDirsIfNeeded(csvFile.file)
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

    private fun validateFile(file: CustomFile, isReadOperation: Boolean, append: Boolean = false) {
        if (!file.isValidExtension || file.extension != "csv") {
            throw IllegalArgumentException("Invalid file extension. Only .csv is supported")
        }
        when {
            isReadOperation && !file.file.exists() -> {
                throw IOException("File not found: ${file.file.absolutePath}")
            }
            !isReadOperation && !append && file.file.exists() -> {
                throw IOException("File already exists: ${file.file.absolutePath}")
            }
        }
    }

    private fun createParentDirsIfNeeded(file: File) {
        val parentDir = file.parentFile
        if (parentDir != null && !parentDir.exists()) {
            val dirsCreated = parentDir.mkdirs()
            if (!dirsCreated) {
                throw IOException("Failed to create directory: ${parentDir.absolutePath}")
            }
        }
    }
}