package logic.usecase.task

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.example.logic.repository.TaskRepository
import org.example.logic.usecase.task.CreateTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateTaskUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var createTaskUseCase: CreateTaskUseCase

    @BeforeEach
    fun setup() {
        taskRepository = mockk()
        createTaskUseCase = CreateTaskUseCase(taskRepository)
    }

    @Test
    fun `should return success when valid all attributes`() = runTest {
        val projectId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
        val task = createTask(title = "title", description = "description", projectId = projectId)
        coEvery { taskRepository.createTask(task) } just Runs

        createTaskUseCase.createTask(task)

        coVerify { taskRepository.createTask(task) }
    }

    @Test
    fun `should return failure when title is empty`() = runTest {
        val task = createTask(
            title = "",
            description = "description"
        )
        val expectedException = "Title must not be empty"

        val result = assertThrows<IllegalArgumentException> {
            createTaskUseCase.createTask(task)
        }

        assertEquals(expectedException, result.message)
        coVerify(exactly = 0) { taskRepository.createTask(any()) }
    }

    @Test
    fun `should return failure when description is empty`() = runTest {
        val task = createTask(
            title = "title",
            description = ""
        )
        val expectedException = "Description must not be empty"

        val result = assertThrows<IllegalArgumentException> {
            createTaskUseCase.createTask(task)
        }

        assertEquals(expectedException, result.message)
        coVerify(exactly = 0) { taskRepository.createTask(any()) }
    }
}