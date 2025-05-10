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

class AddChangeHistoryUseCaseTest {

    private lateinit var repository: ChangeHistoryRepository
    private lateinit var useCase: AddChangeHistoryUseCase

    private val fakeDate = Date(123)

    private val fakeChange = ChangeHistory(
        projectID = 1,
        taskID = 23,
        authorID = 42,
        changeDate = fakeDate,
        changeDescription = "Changed status to InProgress"
    )

    @BeforeEach
    fun setUp() {
        repository = mockk()
        useCase = AddChangeHistoryUseCase(repository)
    }

    @Test
    fun `should add change history successfully`() = runBlocking {
        // Given
        coEvery { repository.addChangeHistory(fakeChange) } returns fakeChange

        // When
        val result = useCase.execute(
            projectId = 1,
            taskId = 23,
            authorId = 42,
            changeDate = fakeDate,
            changeDescription = "Changed status to InProgress"
        )

        // Then
        assertEquals(fakeChange, result)
    }

    @Test
    fun `should throw IllegalArgumentException when repository throws exception`() = runBlocking {
        // Given
        coEvery { repository.addChangeHistory(any()) } throws RuntimeException("DB Error")

        // When & Then
        val exception = assertThrows(IllegalArgumentException::class.java) {
            runBlocking {
                useCase.execute(
                    projectId = 1,
                    taskId = 11111111,
                    authorId = 42,
                    changeDate = fakeDate,
                    changeDescription = "Changed status to InProgress"
                )
            }
        }

        assertTrue(exception.message!!.contains("Invalid Change History data"))
    }
}
