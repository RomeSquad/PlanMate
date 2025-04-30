package data.utils

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FileExtensionCheckerTest {

    private lateinit var handler: FileExtensionChecker

    @TempDir
    lateinit var tempDir: File

    @BeforeEach
    fun setUp() {
        handler = FileExtensionChecker()
    }

    private fun createCustomFile(
        path: String,
        charset: Charset? = StandardCharsets.UTF_8,
        allowedExtensions: Set<String> = setOf("csv")
    ): CustomFile {
        val file = File(tempDir, path)
        return CustomFile(file.absolutePath, charset, allowedExtensions)
    }

    @Test
    fun `CustomFile throws IllegalArgumentException for blank fileName`() {
        assertThrows<IllegalArgumentException> { CustomFile("") }
    }

    @Test
    fun `CustomFile sets properties correctly`() {
        val csvFile = createCustomFile("test.csv")
        assertEquals("test.csv", csvFile.file.name)
        assertEquals("csv", csvFile.extension)
        assertTrue(csvFile.isValidExtension)
        assertEquals(StandardCharsets.UTF_8, csvFile.charset)
    }

    @Test
    fun `CustomFile handles restricted extensions`() {
        val validFile = createCustomFile("test.csv", allowedExtensions = setOf("csv"))
        assertTrue(validFile.isValidExtension)
        val invalidFile = createCustomFile("test.txt", allowedExtensions = setOf("csv"))
        assertFalse(invalidFile.isValidExtension)
    }

    @Test
    fun `CustomFile supports null charset`() {
        val csvFile = createCustomFile("test.csv", charset = null)
        assertNull(csvFile.charset)
    }

    @Test
    fun `readCsv parses CSV with header`() {
        val csvFile = createCustomFile("test.csv")
        csvFile.file.writeText("Name,Age\nAlice,25")
        val result = handler.readCsv(csvFile, hasHeader = true)
        assertEquals(listOf(listOf("Alice", "25")), result)
    }

    @Test
    fun `readCsv parses CSV without header`() {
        val csvFile = createCustomFile("test.csv")
        csvFile.file.writeText("Alice,25\nBob,30")
        val result = handler.readCsv(csvFile, hasHeader = false)
        assertEquals(listOf(listOf("Alice", "25"), listOf("Bob", "30")), result)
    }

    @Test
    fun `readCsv uses custom delimiter and skips empty lines`() {
        val csvFile = createCustomFile("test.csv")
        csvFile.file.writeText("Name;Age\nAlice;25\n\nBob;30")
        val result = handler.readCsv(csvFile, hasHeader = true, delimiter = ';', skipEmptyLines = true)
        assertEquals(listOf(listOf("Alice", "25"), listOf("Bob", "30")), result)
    }

    @Test
    fun `readCsv defaults to UTF-8 with null charset`() {
        val csvFile = createCustomFile("test.csv", charset = null)
        csvFile.file.writeText("Alice,25")
        val result = handler.readCsv(csvFile, hasHeader = false)
        assertEquals(listOf(listOf("Alice", "25")), result)
    }

    @Test
    fun `readCsv throws IOException for non-existent file`() {
        val csvFile = createCustomFile("nonexistent.csv")
        assertThrows<IOException> { handler.readCsv(csvFile, hasHeader = false) }
    }

    @Test
    fun `readCsv throws IllegalArgumentException for invalid extension`() {
        val invalidFile = createCustomFile("test.txt", allowedExtensions = setOf("csv"))
        assertThrows<IllegalArgumentException> { handler.readCsv(invalidFile, hasHeader = false) }
    }

    @Test
    fun `writeCsv writes header and data`() {
        val csvFile = createCustomFile("test.csv")
        handler.writeCsv(csvFile, listOf(listOf("Alice", "25")), listOf("Name", "Age"), false)
        assertEquals("Name,Age\nAlice,25", csvFile.file.readText().trim())
    }

    @Test
    fun `writeCsv writes data without header`() {
        val csvFile = createCustomFile("test.csv")
        handler.writeCsv(csvFile, listOf(listOf("Alice", "25")), null, false)
        assertEquals("Alice,25", csvFile.file.readText().trim())
    }

    @Test
    fun `writeCsv appends to existing file`() {
        val csvFile = createCustomFile("test.csv")
        handler.writeCsv(csvFile, listOf(listOf("Alice", "25")), listOf("Name", "Age"), false)
        handler.writeCsv(csvFile, listOf(listOf("Bob", "30")), null, true)
        assertEquals("Name,Age\nAlice,25\nBob,30", csvFile.file.readText().trim())
    }

    @Test
    fun `writeCsv creates file when appending to non-existent file`() {
        val csvFile = createCustomFile("test.csv")
        assertFalse(csvFile.file.exists())
        handler.writeCsv(csvFile, listOf(listOf("Alice", "25")), null, true)
        assertTrue(csvFile.file.exists())
        assertEquals("Alice,25", csvFile.file.readText().trim())
    }

    @Test
    fun `writeCsv creates parent directories if needed`() {
        val csvFile = createCustomFile("subdir/test.csv")
        handler.writeCsv(csvFile, listOf(listOf("Alice", "25")), null, false)
        assertTrue(csvFile.file.parentFile.exists())
        assertEquals("Alice,25", csvFile.file.readText().trim())
    }

    @Test
    fun `writeCsv uses UTF-8 when charset is null`() {
        val csvFile = createCustomFile("test.csv", charset = null)
        handler.writeCsv(csvFile, listOf(listOf("Alice", "25")), null, false)
        assertEquals("Alice,25", csvFile.file.readText().trim())
    }

    @Test
    fun `writeCsv throws IllegalArgumentException for invalid extension`() {
        val invalidFile = createCustomFile("test.txt", allowedExtensions = setOf("csv"))
        assertThrows<IllegalArgumentException> {
            handler.writeCsv(invalidFile, listOf(listOf("Alice", "25")), null, false)
        }
    }

    @Test
    fun `writeCsv throws IOException for existing file without append`() {
        val csvFile = createCustomFile("test.csv")
        csvFile.file.writeText("existing")
        assertThrows<IOException> {
            handler.writeCsv(csvFile, listOf(listOf("Alice", "25")), null, false)
        }
    }

    @Test
    fun `writeCsv with null data and header writes only header`() {
        val csvFile = createCustomFile("test.csv")
        handler.writeCsv(csvFile, null, listOf("Name", "Age"), false)
        assertEquals("Name,Age", csvFile.file.readText().trim())
    }

    @Test
    fun `writeCsv with empty data and header writes only header`() {
        val csvFile = createCustomFile("test.csv")
        handler.writeCsv(csvFile, emptyList(), listOf("Name", "Age"), false)
        assertEquals("Name,Age", csvFile.file.readText().trim())
    }

    @Test
    fun `writeCsv with empty data and null header writes empty file`() {
        val csvFile = createCustomFile("test.csv")
        handler.writeCsv(csvFile, emptyList(), null, false)
        assertEquals("", csvFile.file.readText().trim())
    }

    @Test
    fun `writeCsv allows appending to existing file`() {
        val csvFile = createCustomFile("test.csv")
        csvFile.file.writeText("existing")
        handler.writeCsv(csvFile, listOf(listOf("Alice", "25")), null, true)
        assertEquals("existing\nAlice,25", csvFile.file.readText().trim())
    }

    @Test
    fun `writeCsv with null data and null header does nothing on existing file with append true`() {
        val csvFile = createCustomFile("test.csv")
        csvFile.file.writeText("existing")
        handler.writeCsv(csvFile, null, null, true)
        assertEquals("existing", csvFile.file.readText().trim())
    }

    @Test
    fun `writeCsv with null data and null header does nothing on non-existing file`() {
        val csvFile = createCustomFile("test.csv")
        assertFalse(csvFile.file.exists())
        handler.writeCsv(csvFile, null, null, false)
        assertFalse(csvFile.file.exists())
    }
}