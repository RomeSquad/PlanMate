package logic.entity

import org.example.logic.entity.State
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class StateTest {

    @Test
    fun `should return task i n formatted string`() {
        //Given
        val state = State(
            projectId = "12T",
            name = "In Progress"
        )
        //when
        val expected = """
         Project ID: 12T | State: In Progress     
        """.trimIndent()

        //then
        assertEquals(state, expected)
    }

}