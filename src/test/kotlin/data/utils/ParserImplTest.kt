package data.utils

import org.example.data.utils.Parser
import org.example.data.utils.ParserImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class ParserImplTest {

    private lateinit var parser: Parser

    @BeforeEach
    fun setUp() {
        parser = ParserImpl()
    }

    @Test
    fun `parse throws IllegalArgumentException for null delimiter`() {
        assertThrows<IllegalArgumentException> {
            parser.parse("a,b", hasHeader = false, delimiter = null, skipEmptyLines = true)
        }
    }

    @Test
    fun `parse throws IllegalArgumentException for invalid delimiter in non-empty content`() {
        assertThrows<IllegalArgumentException> {
            parser.parse("a,b", hasHeader = false, delimiter = ';', skipEmptyLines = true)
        }
    }

    @Test
    fun `parse handles empty content bypassing delimiter check`() {
        val result = parser.parse("", hasHeader = false, delimiter = ',', skipEmptyLines = true)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `parse processes lines with header`() {
        val content = "header1,header2\ndata1,data2"
        val result = parser.parse(content, hasHeader = true, delimiter = ',', skipEmptyLines = true)
        assertEquals(listOf(listOf("data1", "data2")), result)
    }

    @Test
    fun `parse processes lines without header`() {
        val content = "header1,header2\ndata1,data2"
        val result = parser.parse(content, hasHeader = false, delimiter = ',', skipEmptyLines = true)
        assertEquals(listOf(listOf("header1", "header2"), listOf("data1", "data2")), result)
    }

    @Test
    fun `parse skips empty lines and delimiter-only lines when skipEmptyLines is true`() {
        val content = ",,,\ndata1,data2\n\n"
        val result = parser.parse(content, hasHeader = false, delimiter = ',', skipEmptyLines = true)
        assertEquals(listOf(listOf("data1", "data2")), result)
    }

    @Test
    fun `parse includes delimiter-only lines when skipEmptyLines is false`() {
        val content = ",,,\ndata1,data2"
        val result = parser.parse(content, hasHeader = false, delimiter = ',', skipEmptyLines = false)
        assertEquals(listOf(emptyList(), listOf("data1", "data2")), result)
    }

    @Test
    fun `parse trims and filters empty values`() {
        val content = " data1 , , data2 , "
        val result = parser.parse(content, hasHeader = false, delimiter = ',', skipEmptyLines = true)
        assertEquals(listOf(listOf("data1", "data2")), result)
    }

    @Test
    fun `parse handles custom delimiter`() {
        val content = "header1;header2\ndata1;data2"
        val result = parser.parse(content, hasHeader = false, delimiter = ';', skipEmptyLines = true)
        assertEquals(listOf(listOf("header1", "header2"), listOf("data1", "data2")), result)
    }


    @Test
    fun `parseWithHeader throws IllegalArgumentException for null delimiter`() {
        assertThrows<IllegalArgumentException> {
            parser.parseWithHeader("a,b", delimiter = null, skipEmptyLines = true)
        }
    }

    @Test
    fun `parseWithHeader throws IllegalArgumentException for invalid delimiter`() {
        assertThrows<IllegalArgumentException> {
            parser.parseWithHeader("a,b", delimiter = ';', skipEmptyLines = true)
        }
    }

    @Test
    fun `parseWithHeader returns empty pair for empty content`() {
        val (header, data) = parser.parseWithHeader("", delimiter = ',', skipEmptyLines = true)
        assertTrue(header.isEmpty() && data.isEmpty())
    }

    @Test
    fun `parseWithHeader processes lines correctly`() {
        val content = "header1,header2\ndata1,data2"
        val (header, data) = parser.parseWithHeader(content, delimiter = ',', skipEmptyLines = true)
        assertEquals(listOf("header1", "header2"), header)
        assertEquals(listOf(listOf("data1", "data2")), data)
    }

    @Test
    fun `parseWithHeader handles empty header line`() {
        val content = ",,,\ndata1,data2"
        val (header, data) = parser.parseWithHeader(content, delimiter = ',', skipEmptyLines = true)
        assertEquals(emptyList(), header)
        assertEquals(listOf(listOf("data1", "data2")), data)
    }

    @Test
    fun `parseWithHeader skips empty lines when skipEmptyLines is true`() {
        val content = "header1,header2\n\ndata1,data2"
        val (header, data) = parser.parseWithHeader(content, delimiter = ',', skipEmptyLines = true)
        assertEquals(listOf("header1", "header2"), header)
        assertEquals(listOf(listOf("data1", "data2")), data)
    }

    @Test
    fun `parseWithHeader includes empty lines when skipEmptyLines is false`() {
        val content = "header1,header2\n\ndata1,data2"
        val (header, data) = parser.parseWithHeader(content, delimiter = ',', skipEmptyLines = false)
        assertEquals(listOf("header1", "header2"), header)
        assertEquals(listOf(emptyList(), listOf("data1", "data2")), data)
    }

    @Test
    fun `parseWithHeader handles custom delimiter`() {
        val content = "header1;header2\ndata1;data2"
        val (header, data) = parser.parseWithHeader(content, delimiter = ';', skipEmptyLines = true)
        assertEquals(listOf("header1", "header2"), header)
        assertEquals(listOf(listOf("data1", "data2")), data)
    }

    @Test
    fun `parse with empty content and null delimiter returns empty list`() {
        val result = parser.parse("", hasHeader = false, delimiter = null, skipEmptyLines = true)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `parseWithHeader with empty content and null delimiter returns empty pair`() {
        val (header, data) = parser.parseWithHeader("", delimiter = null, skipEmptyLines = true)
        assertTrue(header.isEmpty() && data.isEmpty())
    }

    @Test
    fun `parseWithHeader skips lines with only spaces when skipEmptyLines is true`() {
        val content = "header1,header2\n   \ndata1,data2"
        val (header, data) = parser.parseWithHeader(content, delimiter = ',', skipEmptyLines = true)
        assertEquals(listOf("header1", "header2"), header)
        assertEquals(listOf(listOf("data1", "data2")), data)
    }

    @Test
    fun `parseWithHeader includes lines with only spaces as empty lists when skipEmptyLines is false`() {
        val content = "header1,header2\n   \ndata1,data2"
        val (header, data) = parser.parseWithHeader(content, delimiter = ',', skipEmptyLines = false)
        assertEquals(listOf("header1", "header2"), header)
        assertEquals(listOf(emptyList<String>(), listOf("data1", "data2")), data)
    }

}