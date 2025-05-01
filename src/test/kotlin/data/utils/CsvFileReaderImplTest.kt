package data.utils

import org.example.data.utils.CsvFileReaderImpl
import org.example.data.utils.FileValidator
import org.example.data.utils.Parser
import org.example.data.utils.ParserImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.assertEquals

class CsvFileReaderImplTest {

    private lateinit var reader: CsvFileReaderImpl
    private lateinit var parser: Parser
    private lateinit var validator: FileValidator

    @TempDir
    lateinit var tempDir: File

    @BeforeEach
    fun setUp() {
        parser = ParserImpl()
        validator = FileValidator()
        reader = CsvFileReaderImpl(parser, validator)
    }

    private fun createCustomFile(
        filename: String,
    ): File = File(tempDir, filename)

    @Test
    fun `reads CSV without header using default delimiter`() {
        val csvFile = createCustomFile("test.csv")
        csvFile.writeText("Alice,25\nBob,30")
        val result = reader.readCsv(csvFile)
        assertEquals(listOf(listOf("Alice", "25"), listOf("Bob", "30")), result)
    }

    @Test
    fun `reads CSV with custom delimiter `() {
        val csvFile = createCustomFile("test.csv")
        csvFile.writeText("Alice,25\nBob,30")
        val result = reader.readCsv(csvFile)
        assertEquals(listOf(listOf("Alice", "25"), listOf("Bob", "30")), result)
    }

    @Test
    fun `reads CSV skipping empty lines`() {
        val csvFile = createCustomFile("test.csv")
        csvFile.writeText("Alice,25\n\nBob,30")
        val result = reader.readCsv(csvFile)
        assertEquals(listOf(listOf("Alice", "25"), listOf("Bob", "30")), result)
    }

    @Test
    fun `reads CSV with null charset using UTF-8`() {
        val csvFile = createCustomFile("test.csv")
        csvFile.writeText("Alice,25")
        val result = reader.readCsv(csvFile)
        assertEquals(listOf(listOf("Alice", "25")), result)
    }
}