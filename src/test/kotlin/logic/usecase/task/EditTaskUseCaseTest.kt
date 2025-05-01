package logic.usecase.task

import io.mockk.every
import io.mockk.mockk
import logic.usecase.createTask
import org.example.data.datasource.task.TaskDataSource
import org.example.logic.repository.TaskRepository
import org.example.logic.usecase.task.EditTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class EditTaskUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var taskDataSource: TaskDataSource
    private lateinit var editTaskUseCase: EditTaskUseCase

    @BeforeEach
    fun setup() {
        taskRepository = mockk()
        taskDataSource = mockk()
        editTaskUseCase = EditTaskUseCase(taskRepository)
    }

    @Test
    fun `should throw exception when edit task but title is empty`() {
        val task = createTask(
            id = "A1",
            title = "",
            description = "description",
            projectId = "P1",
        )
        every { taskDataSource.getTaskByIdFromFile(task.id) } returns Result.success(task)

        assertThrows<IllegalArgumentException> {
            editTaskUseCase.editTask(
                taskId = task.id,
                title = task.title,
                description = task.description,
                updatedAt = task.updatedAt
            )
        }
    }

    @Test
    fun `should throw exception when edit task but description is empty`() {
        val task = createTask(
            id = "A1",
            title = "title",
            description = "",
            projectId = "P1",
        )
        every { taskDataSource.getTaskByIdFromFile(task.id) } returns Result.success(task)

        assertThrows<IllegalArgumentException> {
            editTaskUseCase.editTask(
                taskId = task.id,
                title = task.title,
                description = task.description,
                updatedAt = task.updatedAt
            )
        }
    }

    @Test
    fun `should throw exception when task not found by id`() {
        val task = createTask(
            id = "A1",
            title = "",
            description = "description",
            projectId = "P1",
        )
        every { taskDataSource.getTaskByIdFromFile("") } returns Result.failure(Exception("Task not found"))

        assertThrows<IllegalArgumentException> {
            editTaskUseCase.editTask(
                taskId = task.id,
                title = task.title,
                description = task.description,
                updatedAt = task.updatedAt
            )
        }
    }
}