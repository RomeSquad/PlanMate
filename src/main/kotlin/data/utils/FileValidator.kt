package org.example.data.utils

import data.utils.CustomFile
import java.io.File
import java.io.IOException

class FileValidator {
    fun validateFile(file: CustomFile, isReadOperation: Boolean, append: Boolean = false) {
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

    fun createParentDirsIfNeeded(file: File) {
        val parentDir = file.parentFile
        if (parentDir != null && !parentDir.exists()) {
            val dirsCreated = parentDir.mkdirs()
            if (!dirsCreated) {
                throw IOException("Failed to create directory: ${parentDir.absolutePath}")
            }
        }
    }
}