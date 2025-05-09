package org.example.logic.usecase.history

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.example.logic.entity.ChangeHistory
import org.example.logic.repository.ChangeHistoryRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class ShowProjectHistoryUseCaseTest {

    private lateinit var repository: ChangeHistoryRepository
    private lateinit var useCase: ShowProjectHistoryUseCase

    private val fakeHistory = listOf(
        ChangeHistory(
            projectID = 1,
            taskID = 1,
            authorID = 10,
            changeDate = Date(1234),
            changeDescription = "Status changed"
        ),
        ChangeHistory(
            projectID = 1,
            taskID = 2,
            authorID = 11,
            changeDate = Date(1234),
            changeDescription = "Assigned new dev"
        )
    )

    @BeforeEach
    fun setUp() {
        repository = mockk()
        useCase = ShowProjectHistoryUseCase(repository)
    }

    @Test
    fun `should return change history for valid project ID`() = runBlocking {
        // Given
        val projectId = 1
        coEvery { repository.getHistoryByProjectID(projectId) } returns fakeHistory

        // When
        val result = useCase.execute(projectId)

        // Then
        assertEquals(fakeHistory, result)
    }

    @Test
    fun `should throw IllegalArgumentException when repository fails`() = runBlocking {
        // Given
        val projectId = -1
        coEvery { repository.getHistoryByProjectID(projectId) } throws RuntimeException("DB Failure")

        // When & Then
        val exception = assertThrows(IllegalArgumentException::class.java) {
            runBlocking {
                useCase.execute(projectId)
            }
        }

        assertTrue(exception.message!!.contains("Invalid Project ID"))
    }
}
