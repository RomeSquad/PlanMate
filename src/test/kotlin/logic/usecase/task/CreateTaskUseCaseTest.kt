package logic.usecase.task

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.entity.ProjectState
import org.example.logic.entity.Task
import org.example.logic.repository.TaskRepository
import org.example.logic.usecase.task.CreateTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateTaskUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var createTaskUseCase: CreateTaskUseCase

    private val projectId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
    private val taskId = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        taskRepository = mockk()
        createTaskUseCase = CreateTaskUseCase(taskRepository)
    }

    @Test
    fun `should create task when all attributes are valid`() = runTest {
        // Given
        val task = Task(
            taskId = taskId,
            title = "title",
            description = "description",
            projectId = projectId,
            state = ProjectState(
                stateName = "stateName",
                projectId = projectId,
            ),
            createdBy = UUID.randomUUID(),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        coEvery { taskRepository.createTask(task) } returns Unit

        // When
        createTaskUseCase.createTask(task)

        // Then
        coVerify(exactly = 1) { taskRepository.createTask(task) }
    }

    @Test
    fun `should throw IllegalArgumentException when title is empty`() = runTest {
        // Given
        val task = Task(
            taskId = taskId,
            title = "",
            description = "description",
            projectId = projectId,
            state = ProjectState(
                stateName = "stateName",
                projectId = projectId,
            ),
            createdBy = UUID.randomUUID(),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        // When/Then
        val exception = assertThrows<IllegalArgumentException> {
            createTaskUseCase.createTask(task)
        }
        assertEquals("Title must not be empty", exception.message)
        coVerify(exactly = 0) { taskRepository.createTask(any()) }
    }

    @Test
    fun `should throw IllegalArgumentException when description is empty`() = runTest {
        // Given
        val task = Task(
            taskId = taskId,
            title = "title",
            description = "",
            projectId = projectId,
            state = ProjectState(
                stateName = "stateName",
                projectId = projectId,
            ),
            createdBy = UUID.randomUUID(),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        // When/Then
        val exception = assertThrows<IllegalArgumentException> {
            createTaskUseCase.createTask(task)
        }
        assertEquals("Description must not be empty", exception.message)
        coVerify(exactly = 0) { taskRepository.createTask(any()) }
    }

    @Test
    fun `should propagate exception when repository fails`() = runTest {
        // Given
        val task = Task(
            taskId = taskId,
            title = "title",
            description = "description",
            projectId = projectId,
            state = ProjectState(
                stateName = "stateName",
                projectId = projectId,
            ),
            createdBy = UUID.randomUUID(),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        val repositoryException = RuntimeException("Failed to create task")
        coEvery { taskRepository.createTask(task) } throws repositoryException

        // When/Then
        val exception = assertThrows<RuntimeException> {
            createTaskUseCase.createTask(task)
        }
        assertEquals("Failed to create task", exception.message)
        coVerify(exactly = 1) { taskRepository.createTask(task) }
    }
}