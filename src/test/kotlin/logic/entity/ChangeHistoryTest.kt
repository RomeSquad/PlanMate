package logic.entity

import org.example.logic.entity.ChangeHistory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.Date

class ChangeHistoryTest() {

    @Test
    fun ` should return ChangeHistory in formatted String`() {

        //given
        val changHistory = ChangeHistory(
            projectID = "AB3",
            taskID = "CD4",
            authorID = "R34",
            changeDate = Date(),
            changeDescription = "Updated task title"
        )
        val expected = formatted(
            projectID = "AB3",
            taskID = "CD4",
            authorID = "R34",
            changeDate = Date(),
            changeDescription = "Updated task title"
        )

        //when
        val displayed = changHistory.toString()

        //then
        assertEquals(displayed, expected)
    }

    //helper
    fun formatted(
        projectID: String,
        taskID: String,
        authorID: String,
        changeDate: Date,
        changeDescription: String
    ): String {
        return String.format(
            "%-12s | %-10s | %-10s | %-20s | %-30s",
            "ProjectID: $projectID",
            "TaskID: $taskID",
            "AuthorID: $authorID",
            "Date: $changeDate",
            "Change: $changeDescription"
        )

    }
}