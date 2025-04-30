package logic.usecase

import io.mockk.every
import io.mockk.mockk
import org.example.data.datasource.task.TaskDataSource
import org.example.logic.repository.TaskRepository
import org.example.logic.usecase.CreateTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CreateTaskUseCaseTest {

    private lateinit var taskDataSource: TaskDataSource
    private lateinit var taskRepository: TaskRepository
    private lateinit var createTaskUseCase: CreateTaskUseCase

    @BeforeEach
    fun setup() {
        taskRepository = mockk()
        taskDataSource = mockk()
        createTaskUseCase = CreateTaskUseCase(taskRepository)
    }

    @Test
    fun `should throw exception when when create task without title`() {
        val task = createTask(
            title = "",
            description = "description"
        )
        every { taskDataSource.getTask() } returns task

        assertThrows<IllegalArgumentException> {
            createTaskUseCase.createTask(task)
        }
    }

    @Test
    fun `should throw exception when when create task without description`() {
        val task = createTask(
            title = "title",
            description = ""
        )
        every { taskDataSource.getTask() } returns task

        assertThrows<IllegalArgumentException> {
            createTaskUseCase.createTask(task)
        }
    }

    @Test
    fun `should throw exception when when create task without projectId`() {
        val task = createTask(
            title = "title",
            description = "description",
            projectId = ""
        )
        every { taskDataSource.getTask() } returns task

        assertThrows<IllegalArgumentException> {
            createTaskUseCase.createTask(task)
        }
    }

    @Test
    fun `should throw exception when create task but id is null`() {
        val task = createTask(
            id = null,
            title = "title",
            description = "description",
        )
        every { taskDataSource.getTask() } returns task

        assertNull(task.id)
    }

    @Test
    fun `should throw exception when create task but error in database`() {
        val task = createTask(title = "title", description = "description")
        every { taskDataSource.getTask() } throws RuntimeException("Error while writing data!")

        try {
            createTaskUseCase.createTask(task)
        } catch (exception: RuntimeException) {
            assertEquals("Error while writing data!", exception.message)
        }
    }
}