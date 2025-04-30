package data.utils

import java.io.File
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets


data class CustomFile(
    val fileName: String,
    val charset: Charset? = StandardCharsets.UTF_8,
    val allowedExtensions: Set<String> = emptySet()
) {
    val file: File
        get() = File(fileName)

    val extension: String
        get() = file.extension.lowercase()

    val isValidExtension: Boolean
        get() = allowedExtensions.isEmpty() || allowedExtensions.contains(extension)

    init {
        require(fileName.isNotBlank()) { "File name must not be blank" }
    }
}

