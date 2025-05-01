package data.utils

import org.example.data.utils.FileValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.io.IOException
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class FileValidatorTest {

    private lateinit var validator: FileValidator

    @TempDir
    lateinit var tempDir: File

    @BeforeEach
    fun setUp() {
        validator = FileValidator()
    }

    private fun createCustomFile(filename: String) =
        File(tempDir, filename)

    @Test
    fun `throws IllegalArgumentException for invalid extension`() {
        val file = createCustomFile("test.txt")
        assertFailsWith<IllegalArgumentException>("Invalid file extension. Only .csv is supported") {
            validator.validateFile(file, isReadOperation = false)
        }
    }

    @Test
    fun `throws IOException for non-existent file on read`() {
        val file = createCustomFile("nonexistent.csv")
        assertFailsWith<IOException>("File not found: ${file.absolutePath}") {
            validator.validateFile(file, isReadOperation = true)
        }
    }

    @Test
    fun `validates read with existing file successfully`() {
        val file = createCustomFile("existing.csv")
        file.createNewFile()
        validator.validateFile(file, isReadOperation = true)
    }

    @Test
    fun `validates write without append on non-existent file successfully`() {
        val file = createCustomFile("nonexistent.csv")
        validator.validateFile(file, isReadOperation = false)
    }

    @Test
    fun `validates write with append on non-existent file successfully`() {
        val file = createCustomFile("nonexistent.csv")
        validator.validateFile(file, isReadOperation = false)
    }

    @Test
    fun `creates parent directories when they don't exist`() {
        val file = File(tempDir, "subdir/test.csv")
        validator.createParentDirsIfNeeded(file)
        assertTrue(file.parentFile.exists())
    }

    @Test
    fun `does nothing when parent directory already exists`() {
        val file = File(tempDir, "test.csv")
        tempDir.mkdirs()
        validator.createParentDirsIfNeeded(file)
    }

    @Test
    fun `does nothing when parent directory is null`() {
        val file = tempDir
        validator.createParentDirsIfNeeded(file)
    }
}