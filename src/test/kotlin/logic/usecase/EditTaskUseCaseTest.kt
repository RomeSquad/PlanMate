package logic.usecase

import io.mockk.every
import io.mockk.mockk
import org.example.data.datasource.task.TaskDataSource
import org.example.logic.entity.State
import org.example.logic.entity.Task
import org.example.logic.repository.TaskRepository
import org.example.logic.usecase.EditTaskUseCase
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
        val task = Task (
            id = "A1",
            title = "",
            description = "description",
            state = State(),
            projectId = "P1",
            createdBy = "",
            createdAt = 0,
            updatedAt = 0
        )
        every { taskDataSource.getTaskById(task.id) } returns task

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
        val task = Task (
            id = "A1",
            title = "title",
            description = "",
            state = State(),
            projectId = "P1",
            createdBy = "",
            createdAt = 0,
            updatedAt = 0
        )
        every { taskDataSource.getTaskById(task.id) } returns task

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
        val task = Task (
            id = "A1",
            title = "",
            description = "description",
            state = State(),
            projectId = "P1",
            createdBy = "",
            createdAt = 0,
            updatedAt = 0
        )
        every { taskDataSource.getTaskById("") } returns task

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