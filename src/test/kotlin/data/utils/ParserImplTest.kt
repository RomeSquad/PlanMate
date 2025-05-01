package data.utils

import org.example.data.utils.Parser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class ParserImplTest {

    private lateinit var parser: Parser
    @BeforeEach
    fun setUp() {
        parser = ParserImpl()
    }


    @Test
    fun `parse handles empty content by passing delimiter check`() {
        val result = parser.parseCsv("")
        assertTrue(result.isEmpty())
    }


    @Test
    fun `parse processes lines without header`() {
        val content = "header1,header2\ndata1,data2"
        val result = parser.parseCsv(content)
        assertEquals(listOf(listOf("header1", "header2"), listOf("data1", "data2")), result)
    }
}