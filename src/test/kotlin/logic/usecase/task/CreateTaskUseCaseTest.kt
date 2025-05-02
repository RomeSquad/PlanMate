package logic.usecase.task

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.usecase.createTask
import org.example.logic.repository.TaskRepository
import org.example.logic.usecase.task.CreateTaskUseCase
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CreateTaskUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var createTaskUseCase: CreateTaskUseCase

    @BeforeEach
    fun setup() {
        taskRepository = mockk()
        createTaskUseCase = CreateTaskUseCase(taskRepository)
    }

    @Test
    fun `should return success when valid all attributes`() {
        val task = createTask(title = "title", description = "", projectId = 1)
        every { taskRepository.createTask(task) }

        val result = createTaskUseCase.createTask(task)

        assertTrue(result.isSuccess)
        verify { taskRepository.createTask(task) }
    }

    @Test
    fun `should return failure when title is empty`() {
        val task = createTask(
            title = "",
            description = "description"
        )
        val expectedException = "Title must not be empty"

        val result = createTaskUseCase.createTask(task)

        assertEquals(
            expected = expectedException,
            actual = result.exceptionOrNull()?.message
        )
        verify { taskRepository.createTask(task) }
    }

    @Test
    fun `should return failure when description is empty`() {
        val task = createTask(
            title = "title",
            description = ""
        )
        val expectedException = "Description must not be empty"

        val result = createTaskUseCase.createTask(task)

        assertEquals(expectedException, result.exceptionOrNull()?.message)
        verify { taskRepository.createTask(task) }
    }

    @Test
    fun `should return failure when projectId is zero`() {
        val task = createTask(
            title = "title",
            description = "description",
            projectId = 0
        )
        val expectedException = "Project ID cannot be zero"

        val result = createTaskUseCase.createTask(task)

        assertEquals(expectedException, result.exceptionOrNull()?.message)
        verify { taskRepository.createTask(task) }
    }

}