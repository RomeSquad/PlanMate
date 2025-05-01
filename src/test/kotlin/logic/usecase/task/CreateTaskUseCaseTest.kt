package logic.usecase.task

import io.mockk.mockk
import logic.usecase.createTask
import org.example.logic.repository.TaskRepository
import org.example.logic.usecase.task.CreateTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertNull

class CreateTaskUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var createTaskUseCase: CreateTaskUseCase

    @BeforeEach
    fun setup() {
        taskRepository = mockk()
        createTaskUseCase = CreateTaskUseCase(taskRepository)
    }

    @Test
    fun `should throw exception when when create task without title`() {
        val task = createTask(
            title = "",
            description = "description"
        )

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

        assertNull(task.id)
    }
}