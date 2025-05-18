package logic.usecase.history

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.example.logic.entity.ModificationLog
import org.example.logic.repository.ChangeHistoryRepository
import org.example.logic.usecase.history.AddChangeHistoryUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class AddChangeHistoryUseCaseTest {
    private lateinit var repository: ChangeHistoryRepository
    private lateinit var useCase: AddChangeHistoryUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk()
        useCase = AddChangeHistoryUseCase(repository)
    }

    @Test
    fun `should add change history successfully`() = runBlocking {
        coEvery { repository.addChangeHistory(dummyChangeHistoryData) } returns dummyChangeHistoryData

        val result = useCase.execute(
            projectId = UUID.fromString("11111111-1111-1111-1111-111111111111"),
            taskId = UUID.fromString("22222222-2222-2222-2222-222222222222"),
            authorId = UUID.fromString("33333333-3333-3333-3333-333333333333"),
            changeDate = DummyDate,
            changeDescription = "Changed status to InProgress"
        )

        assertEquals(dummyChangeHistoryData, result)
    }

    @Test
    fun `should throw IllegalArgumentException when repository throws exception`() = runBlocking {
        coEvery { repository.addChangeHistory(any()) } throws RuntimeException("DB Error")

        val exception = assertThrows(IllegalArgumentException::class.java) {
            runBlocking {
                useCase.execute(
                    projectId = UUID.fromString("11111111-1111-1111-1111-111111111111"),
                    taskId = UUID.fromString("22222222-2222-2222-2222-222222222222"),
                    authorId = UUID.fromString("33333333-3333-3333-3333-333333333333"),
                    changeDate = DummyDate,
                    changeDescription = "Changed status to InProgress"
                )
            }
        }

        assertTrue(exception.message!!.contains("Invalid Change History data"))
    }
    private val DummyDate = Date(123)

    private val dummyChangeHistoryData = ModificationLog(
        projectID = UUID.fromString("11111111-1111-1111-1111-111111111111"),
        taskID = UUID.fromString("22222222-2222-2222-2222-222222222222"),
        authorID = UUID.fromString("33333333-3333-3333-3333-333333333333"),
        changeDate = DummyDate,
        changeDescription = "Changed status to InProgress"
    )
}
