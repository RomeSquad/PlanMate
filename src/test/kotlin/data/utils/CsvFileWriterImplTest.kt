package data.utils

import org.example.data.utils.CsvFileWriterImpl
import org.example.data.utils.FileValidator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.charset.StandardCharsets
import kotlin.test.Test

class CsvFileWriterImplTest {
    private lateinit var writer: CsvFileWriterImpl
    private lateinit var validator: FileValidator

    @TempDir
    lateinit var tempDir: File

    @BeforeEach
    fun setUp() {
        validator = FileValidator()
        writer = CsvFileWriterImpl(validator)
    }

    private fun createCustomFile(
        filename: String,
    ): File = File(tempDir, filename)

    @Test
    fun `writes data only when header is null`() {
        val csvFile = createCustomFile("test.csv")
        val data = listOf(listOf("Alice", "25"))
        writer.writeCsv(csvFile, data)
        assertEquals("Alice,25", csvFile.readText().trim())
    }

    @Test
    fun `creates and writes when appending to non-existent file`() {
        val csvFile = createCustomFile("test.csv")
        assertFalse(csvFile.exists())
        val data = listOf(listOf("Alice", "25"))
        writer.writeCsv(csvFile, data)
        assertTrue(csvFile.exists())
        assertEquals("Alice,25", csvFile.readText().trim())
    }

    @Test
    fun `creates parent directories if needed`() {
        val csvFile = createCustomFile("subdir/test.csv")
        val data = listOf(listOf("Alice", "25"))
        writer.writeCsv(csvFile, data)
        assertTrue(csvFile.parentFile.exists())
        assertEquals("Alice,25", csvFile.readText().trim())
    }

    @Test
    fun `uses UTF-8 when charset is null`() {
        val csvFile = createCustomFile("test.csv")
        val data = listOf(listOf("Alice", "25"))
        writer.writeCsv(csvFile, data)
        assertEquals("Alice,25", csvFile.readText(StandardCharsets.UTF_8).trim())
    }
}
