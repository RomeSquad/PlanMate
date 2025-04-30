package data.utils

import org.example.data.utils.CsvFileWriterImpl
import org.example.data.utils.FileValidator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.charset.Charset
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
        charset: Charset? = StandardCharsets.UTF_8,
        allowedExtensions: Set<String> = setOf("csv")
    ): CustomFile = CustomFile(File(tempDir, filename).absolutePath, charset, allowedExtensions)

    @Test
    fun `does nothing when data and header are null`() {
        val csvFile = createCustomFile("test.csv")
        writer.writeCsv(csvFile, null, null, false)
        assertFalse(csvFile.file.exists()) // File should not be created
    }

    @Test
    fun `writes header only when data is null`() {
        val csvFile = createCustomFile("test.csv")
        val header = listOf("Name", "Age")
        writer.writeCsv(csvFile, null, header, false)
        assertEquals("Name,Age", csvFile.file.readText().trim())
    }

    @Test
    fun `writes data only when header is null`() {
        val csvFile = createCustomFile("test.csv")
        val data = listOf(listOf("Alice", "25"))
        writer.writeCsv(csvFile, data, null, false)
        assertEquals("Alice,25", csvFile.file.readText().trim())
    }

    @Test
    fun `writes header and data when both provided`() {
        val csvFile = createCustomFile("test.csv")
        val header = listOf("Name", "Age")
        val data = listOf(listOf("Alice", "25"), listOf("Bob", "30"))
        writer.writeCsv(csvFile, data, header, false)
        assertEquals("Name,Age\nAlice,25\nBob,30", csvFile.file.readText().trim())
    }

    @Test
    fun `writes empty data with header`() {
        val csvFile = createCustomFile("test.csv")
        val header = listOf("Name", "Age")
        val data = emptyList<List<String>>()
        writer.writeCsv(csvFile, data, header, false)
        assertEquals("Name,Age", csvFile.file.readText().trim())
    }

    @Test
    fun `appends to existing file`() {
        val csvFile = createCustomFile("test.csv")
        csvFile.file.writeText("Name,Age\nAlice,25")
        val data = listOf(listOf("Bob", "30"))
        writer.writeCsv(csvFile, data, null, true)
        assertEquals("Name,Age\nAlice,25\nBob,30", csvFile.file.readText().trim())
    }

    @Test
    fun `creates and writes when appending to non-existent file`() {
        val csvFile = createCustomFile("test.csv")
        assertFalse(csvFile.file.exists())
        val data = listOf(listOf("Alice", "25"))
        writer.writeCsv(csvFile, data, null, true)
        assertTrue(csvFile.file.exists())
        assertEquals("Alice,25", csvFile.file.readText().trim())
    }

    @Test
    fun `overwrites non-existent file in non-append mode`() {
        val csvFile = createCustomFile("test.csv")
        val header = listOf("Name", "Age")
        writer.writeCsv(csvFile, null, header, false)
        assertEquals("Name,Age", csvFile.file.readText().trim())
    }

    @Test
    fun `creates parent directories if needed`() {
        val csvFile = createCustomFile("subdir/test.csv")
        val data = listOf(listOf("Alice", "25"))
        writer.writeCsv(csvFile, data, null, false)
        assertTrue(csvFile.file.parentFile.exists())
        assertEquals("Alice,25", csvFile.file.readText().trim())
    }

    @Test
    fun `uses specified charset`() {
        val csvFile = createCustomFile("test.csv", charset = StandardCharsets.UTF_16)
        val data = listOf(listOf("Alice", "25"))
        writer.writeCsv(csvFile, data, null, false)
        assertEquals("Alice,25", csvFile.file.readText(StandardCharsets.UTF_16).trim())
    }

    @Test
    fun `uses UTF-8 when charset is null`() {
        val csvFile = createCustomFile("test.csv", charset = null)
        val data = listOf(listOf("Alice", "25"))
        writer.writeCsv(csvFile, data, null, false)
        assertEquals("Alice,25", csvFile.file.readText(StandardCharsets.UTF_8).trim())
    }
}
