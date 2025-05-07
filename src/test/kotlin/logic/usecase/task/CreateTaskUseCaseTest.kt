package logic.usecase.task

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
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
    fun `should return success when valid all attributes`() {
        val task = createTask(title = "title", description = "description", projectId = 1)
        every { taskRepository.createTask(task) } just Runs

        createTaskUseCase.createTask(task)

        verify { taskRepository.createTask(task) }
    }

    @Test
    fun `should return failure when title is empty`() {
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
        verify(exactly = 0) { taskRepository.createTask(any()) }
    }

    @Test
    fun `should return failure when description is empty`() {
        val task = createTask(
            title = "title",
            description = ""
        )
        val expectedException = "Description must not be empty"

        val result = assertThrows<IllegalArgumentException> {
            createTaskUseCase.createTask(task)
        }

        assertEquals(expectedException, result.message)
        verify(exactly = 0) { taskRepository.createTask(any()) }
    }

    @Test
    fun `should return failure when projectId is zero`() {
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
        verify(exactly = 0) { taskRepository.createTask(any()) }
    }

}