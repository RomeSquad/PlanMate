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

        val expected = formatState(
            projectId = "12T",
            stateName = "In Progress"
        )
        //when

        val displayed =state.toString()

        //then
        assertEquals(displayed, expected)

    }
    // helper
    fun formatState(projectId: String, stateName: String): String {
        return String.format("%-20s | %-20s", "Project ID: $projectId", "State: $stateName")
    }

}