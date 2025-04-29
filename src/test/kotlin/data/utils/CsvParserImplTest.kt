package data.utils

import org.example.data.utils.CsvFile
import org.example.data.utils.CsvParser
import org.example.data.utils.CsvParserImpl
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CsvParserImplTest {

    private lateinit var csvFile: CsvFile
    private lateinit var csvParser: CsvParser

    @TempDir
    lateinit var tempDir: File

    @BeforeEach
    fun setUp() {
        csvFile = createCsvFile(File(tempDir, "test.csv"))
        csvParser = CsvParserImpl()
    }

    @AfterEach
    fun tearDown() {
        tempDir.listFiles()?.forEach { it.delete() }
    }

    private fun createCsvFile(file: File, charset: Charset? = StandardCharsets.UTF_8): CsvFile {
        return CsvFile(file.absolutePath, charset)
    }

    private fun getSampleData(): Pair<List<String>, List<List<String>>> {
        val header = listOf("Name", "Age", "Address")
        val records = listOf(
            listOf("Alice", "25", "123 Main St"),
            listOf("Bob", "30", "456 Oak Ave")
        )
        return header to records
    }

    private fun generateCsvContent(
        header: List<String>? = null,
        records: List<List<String>> = emptyList(),
        withEmptyLines: Boolean = false
    ): String = buildString {
        header?.let { appendLine(it.joinToString(",")) }
        records.forEachIndexed { index, row ->
            appendLine(row.joinToString(","))
            if (withEmptyLines && index < records.size - 1) appendLine()
        }
    }.trimEnd()

    private fun writeSampleData(file: CsvFile = csvFile): Pair<List<String>, List<List<String>>> {
        val (header, records) = getSampleData()
        file.file.writeText(generateCsvContent(header, records))
        return header to records
    }

    //region Read Tests
    @Test
    fun `readCsv counts rows correctly with header`() {
        writeSampleData()
        val result = csvParser.readCsv(csvFile, hasHeader = true, delimiter = ',', skipEmptyLines = true)
        assertEquals(2, result.size)
    }

    @Test
    fun `readCsv skips empty lines when enabled`() {
        val (header, records) = getSampleData()
        csvFile.file.writeText(generateCsvContent(header, records, withEmptyLines = true))
        val result = csvParser.readCsv(csvFile, hasHeader = true, delimiter = ',', skipEmptyLines = true)
        assertEquals(2, result.size)
    }

    @Test
    fun `readCsv includes empty lines when disabled`() {
        val (header, records) = getSampleData()
        csvFile.file.writeText(generateCsvContent(header, records, withEmptyLines = true))
        val result = csvParser.readCsv(csvFile, hasHeader = true, delimiter = ',', skipEmptyLines = false)
        assertEquals(3, result.size)
    }

    @Test
    fun `readCsv handles missing columns in row`() {
        val (header, records) = getSampleData()
        val unevenRecords = listOf(records[0], listOf("Bob", "30"))
        csvFile.file.writeText(generateCsvContent(header, unevenRecords))
        val result = csvParser.readCsv(csvFile, hasHeader = true, delimiter = ',', skipEmptyLines = true)
        assertEquals(listOf("Bob", "30"), result[1])
    }

    @Test
    fun `readCsv throws IllegalArgumentException for invalid delimiter`() {
        writeSampleData()
        assertThrows<IllegalArgumentException> {
            csvParser.readCsv(csvFile, hasHeader = true, delimiter = ';', skipEmptyLines = true)
        }
    }

    @Test
    fun `readCsv throws IOException for non-existent file`() {
        val nonExistentFile = createCsvFile(File("nonexistent.csv"))
        assertThrows<IOException> {
            csvParser.readCsv(nonExistentFile, hasHeader = true, delimiter = ',', skipEmptyLines = true)
        }
    }

    @Test
    fun `readCsv throws IllegalArgumentException for null delimiter`() {
        writeSampleData()
        assertThrows<IllegalArgumentException> {
            csvParser.readCsv(csvFile, hasHeader = true, delimiter = null, skipEmptyLines = true)
        }
    }

    @Test
    fun `readCsv returns empty list for empty file`() {
        csvFile.file.writeText(generateCsvContent())
        val result = csvParser.readCsv(csvFile, hasHeader = true, delimiter = ',', skipEmptyLines = true)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `readCsv handles empty value in first row`() {
        val (header, _) = getSampleData()
        val records = listOf(listOf("Alice", "25", "", "123 Main St"))
        csvFile.file.writeText(generateCsvContent(header, records))
        val result = csvParser.readCsv(csvFile, hasHeader = true, delimiter = ',', skipEmptyLines = true)
        assertEquals(listOf("Alice", "25", "123 Main St"), result[0])
    }

    @Test
    fun `readCsv handles empty value in second row`() {
        val (header, _) = getSampleData()
        val records = listOf(
            listOf("Alice", "25", "123 Main St"),
            listOf("Bob", "30", "", "456 Oak Ave")
        )
        csvFile.file.writeText(generateCsvContent(header, records))
        val result = csvParser.readCsv(csvFile, hasHeader = true, delimiter = ',', skipEmptyLines = true)
        assertEquals(listOf("Bob", "30", "456 Oak Ave"), result[1])
    }

    @Test
    fun `readCsv with null charset counts rows correctly`() {
        val nullCharsetFile = createCsvFile(File(tempDir, "null_charset.csv"), charset = null)
        val (header, records) = getSampleData()
        csvParser.writeCsv(nullCharsetFile, listOf(records[0]), header)
        val result = csvParser.readCsv(nullCharsetFile, hasHeader = true, delimiter = ',', skipEmptyLines = true)
        assertEquals(1, result.size)
    }

    @Test
    fun `readCsv with null charset reads data correctly`() {
        val nullCharsetFile = createCsvFile(File(tempDir, "null_charset.csv"), charset = null)
        val (header, records) = getSampleData()
        csvParser.writeCsv(nullCharsetFile, listOf(records[0]), header)
        val result = csvParser.readCsv(nullCharsetFile, hasHeader = true, delimiter = ',', skipEmptyLines = true)
        assertEquals(listOf("Alice", "25", "123 Main St"), result[0])
    }
    //endregion

    //region Write Tests
    @Test
    fun `writeCsv writes header correctly`() {
        val (header, records) = getSampleData()
        csvParser.writeCsv(csvFile, records, header)
        assertEquals("Name,Age,Address", csvFile.file.readLines()[0])
    }

    @Test
    fun `writeCsv writes first row correctly`() {
        val (header, records) = getSampleData()
        csvParser.writeCsv(csvFile, records, header)
        assertEquals("Alice,25,123 Main St", csvFile.file.readLines()[1])
    }

    @Test
    fun `writeCsv writes second row correctly`() {
        val (header, records) = getSampleData()
        csvParser.writeCsv(csvFile, records, header)
        assertEquals("Bob,30,456 Oak Ave", csvFile.file.readLines()[2])
    }

    @Test
    fun `writeCsv throws IllegalArgumentException for null data`() {
        val (header, _) = getSampleData()
        assertThrows<IllegalArgumentException> {
            csvParser.writeCsv(csvFile, null, header)
        }
    }

    @Test
    fun `writeCsv creates file in new directory`() {
        val newFile = createCsvFile(File(tempDir, "new_folder/test.csv"))
        val (header, records) = getSampleData()
        csvParser.writeCsv(newFile, listOf(records[0]), header)
        assertTrue(newFile.file.exists())
    }

    @Test
    fun `writeCsv creates parent directory if missing`() {
        val newFile = createCsvFile(File(tempDir, "new_folder/test.csv"))
        val (header, records) = getSampleData()
        csvParser.writeCsv(newFile, listOf(records[0]), header)
        assertTrue(newFile.file.parentFile.exists())
    }

    @Test
    fun `writeCsv throws IOException for directory conflict`() {
        val newCsvFile = createCsvFile(File(tempDir, "new_folder/test.csv"))
        val (header, records) = getSampleData()
        assertThrows<IOException> {
            csvParser.writeCsv(newCsvFile, listOf(records[0]), header)
        }
    }

    @Test
    fun `writeCsv with empty data creates non-empty file with header`() {
        val (header, _) = getSampleData()
        csvParser.writeCsv(csvFile, emptyList(), header)
        assertTrue(csvFile.file.readLines().isNotEmpty())
    }

    @Test
    fun `writeCsv appends new row correctly`() {
        val (header, records) = getSampleData()
        csvParser.writeCsv(csvFile, listOf(records[0]), header, append = false)
        csvParser.writeCsv(csvFile, listOf(records[1]), header, append = true)
        assertEquals("Bob,30,456 Oak Ave", csvFile.file.readLines()[2])
    }

    @Test
    fun `writeCsv in append mode preserves total line count`() {
        val (header, records) = getSampleData()
        csvParser.writeCsv(csvFile, listOf(records[0]), header, append = false)
        csvParser.writeCsv(csvFile, listOf(records[1]), header, append = true)
        assertEquals(3, csvFile.file.readLines().size)
    }

    @Test
    fun `writeCsv with null header writes data correctly`() {
        val (_, records) = getSampleData()
        csvParser.writeCsv(csvFile, listOf(records[0]), null)
        assertEquals("Alice,25,123 Main St", csvFile.file.readLines()[0])
    }

    @Test
    fun `writeCsv with empty header writes data correctly`() {
        val (_, records) = getSampleData()
        csvParser.writeCsv(csvFile, listOf(records[0]), emptyList())
        assertEquals("Alice,25,123 Main St", csvFile.file.readLines()[0])
    }

    @Test
    fun `writeCsv with empty rows preserves header`() {
        val (header, records) = getSampleData()
        csvParser.writeCsv(csvFile, listOf(emptyList(), records[0]), header)
        assertEquals("Name,Age,Address", csvFile.file.readLines()[0])
    }

    @Test
    fun `writeCsv with empty rows writes data correctly`() {
        val (header, records) = getSampleData()
        csvParser.writeCsv(csvFile, listOf(emptyList(), records[0]), header)
        assertEquals("Alice,25,123 Main St", csvFile.file.readLines()[1])
    }

    @Test
    fun `writeCsv with empty rows limits line count`() {
        val (header, records) = getSampleData()
        csvParser.writeCsv(csvFile, listOf(emptyList(), records[0]), header)
        assertEquals(2, csvFile.file.readLines().size)
    }

    @Test
    fun `writeCsv throws IOException for read-only file`() {
        val readOnlyFile = File(tempDir, "readonly.csv").apply {
            writeText(generateCsvContent(listOf("initial content")))
            setWritable(false)
        }
        val readOnlyCsvFile = createCsvFile(readOnlyFile)
        val (header, records) = getSampleData()
        assertThrows<IOException> {
            csvParser.writeCsv(readOnlyCsvFile, listOf(records[0]), header)
        }
        readOnlyFile.setWritable(true)
    }

    @Test
    fun `writeCsv with null charset writes header correctly`() {
        val nullCharsetFile = createCsvFile(File(tempDir, "null_charset.csv"), charset = null)
        val (header, records) = getSampleData()
        csvParser.writeCsv(nullCharsetFile, listOf(records[0]), header)
        assertEquals("Name,Age,Address", nullCharsetFile.file.readLines()[0])
    }

    @Test
    fun `writeCsv with null charset writes data correctly`() {
        val nullCharsetFile = createCsvFile(File(tempDir, "null_charset.csv"), charset = null)
        val (header, records) = getSampleData()
        csvParser.writeCsv(nullCharsetFile, listOf(records[0]), header)
        assertEquals("Alice,25,123 Main St", nullCharsetFile.file.readLines()[1])
    }

    @Test
    fun `writeCsv throws IOException for existing read-only file`() {
        val file = File(tempDir, "test.csv").apply {
            createNewFile()
            setReadOnly()
        }
        val csvFile = createCsvFile(file)
        val (header, records) = getSampleData()
        assertThrows<IOException> {
            csvParser.writeCsv(csvFile, listOf(records[0]), header)
        }
        file.setWritable(true)
    }
    //endregion
}