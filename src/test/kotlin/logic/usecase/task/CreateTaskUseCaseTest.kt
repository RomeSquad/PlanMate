package logic.usecase.task

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.example.logic.repository.TaskRepository
import org.example.logic.usecase.task.CreateTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
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
        val task = createTask(title = "title", description = "description", projectId = 1)
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

        assertEquals(
            expected = expectedException,
            actual = result.message
        )
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

    @Test
    fun `should return failure when projectId is zero`() = runTest {
        val task = createTask(
            title = "title",
            description = "description",
            projectId = 0
        )
        val expectedException = "Project ID cannot be zero"

        val result = assertThrows<IllegalArgumentException> {
            createTaskUseCase.createTask(task)
        }

        assertEquals(expectedException, result.message)
        coVerify(exactly = 0) { taskRepository.createTask(any()) }
    }

}