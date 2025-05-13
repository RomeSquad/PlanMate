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
            projectID = UUID.fromString("11111111-1111-1111-1111-111111111111"),
            taskID = UUID.fromString("22222222-2222-2222-2222-222222222222"),
            authorID = UUID.fromString("33333333-3333-3333-3333-333333333333"),
            changeDate = Date(1234),
            changeDescription = "Status changed"
        ),
        ChangeHistory(
            projectID = UUID.fromString("11111111-1111-1111-1111-111111111111"),
            taskID = UUID.fromString("44444444-4444-4444-4444-444444444444"),
            authorID = UUID.fromString("55555555-5555-5555-5555-555555555555"),
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
        val projectId = UUID.fromString("11111111-1111-1111-1111-111111111111")
        coEvery { repository.getHistoryByProjectID(projectId) } returns fakeHistory

        // When
        val result = useCase.execute(projectId)

        // Then
        assertEquals(fakeHistory, result)
    }

    @Test
    fun `should throw IllegalArgumentException when repository fails`() = runBlocking {
        // Given
        val projectId = UUID.fromString("11111111-1111-1111-1111-111111111111")
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
