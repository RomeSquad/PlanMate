package logic.usecase.history

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.example.logic.entity.ModificationLog
import org.example.logic.repository.ChangeHistoryRepository
import org.example.logic.usecase.history.ShowTaskHistoryUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class ShowTaskHistoryUseCaseTest {
    private lateinit var repository: ChangeHistoryRepository
    private lateinit var useCase: ShowTaskHistoryUseCase
    private val taskId = UUID.fromString("11111111-1111-1111-1111-111111111111")

    @BeforeEach
    fun setUp() {
        repository = mockk()
        useCase = ShowTaskHistoryUseCase(repository)
    }

    @Test
    fun `should return task history for valid task ID`() = runBlocking {
        coEvery { repository.getHistoryByTaskID(taskId) } returns dummyChangeHistoryData()

        val result = useCase.execute(taskId)

        assertEquals(dummyChangeHistoryData(), result)
    }

    @Test
    fun `should throw IllegalArgumentException when repository throws exception`() = runBlocking {
        coEvery { repository.getHistoryByTaskID(taskId) } throws RuntimeException("DB Failed")

        val exception = assertThrows(IllegalArgumentException::class.java) {
            runBlocking {
                useCase.execute(taskId)
            }
        }

        assertTrue(exception.message!!.contains("Invalid Task ID"))
    }

    private fun dummyChangeHistoryData(): List<ModificationLog> {
        val fakeDate = Date(123)
        val taskId = UUID.fromString("11111111-1111-1111-1111-111111111111")

        return listOf(
            ModificationLog(
                projectID = UUID.fromString("11111111-1111-1111-1111-111111111111"),
                taskID = taskId,
                authorID = UUID.fromString("11111111-1111-1111-1111-111111111110"),
                changeDate = fakeDate,
                changeDescription = "Task created"
            ),
            ModificationLog(
                projectID = UUID.fromString("11111111-1111-1111-1111-111111111111"),
                taskID = taskId,
                authorID = UUID.fromString("11111111-1111-1111-1111-111111111111"),
                changeDate = fakeDate,
                changeDescription = "Changed status"
            )
        )
    }
}