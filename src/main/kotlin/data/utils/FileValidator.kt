package org.example.data.utils

import java.io.File
import java.io.IOException

class FileValidator {
    fun validateFile(
        file: File,
        allowedExtensions: Set<String> = emptySet(),
        isReadOperation: Boolean = false,
    ) {
        val extension = file.extension.lowercase()
        val isValidExtension = allowedExtensions.isEmpty() || allowedExtensions.contains(extension)
        if (!isValidExtension || extension != "csv") {
            throw IllegalArgumentException("Invalid file extension. Only .csv is supported")
        }

        if (isReadOperation) {
            if (!file.exists()) throw IOException("File does not exist: ${file.absolutePath}")
            if (!file.canRead()) throw IOException("File cannot be read: ${file.absolutePath}")
        } else {
            if (file.exists() && !file.canWrite()) {
                throw IOException("File cannot be written: ${file.absolutePath}")
            }
            createParentDirsIfNeeded(file)
        }
    }

    fun createParentDirsIfNeeded(file: File) {
        val parentDir = file.parentFile
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw IOException("Failed to create directories: ${parentDir.absolutePath}")
            }
        }
    }
}