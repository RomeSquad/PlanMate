package logic.usecase.task

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.example.logic.exception.TaskNotFoundException
import org.example.logic.repository.TaskRepository
import org.example.logic.usecase.task.EditTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class EditTaskUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var editTaskUseCase: EditTaskUseCase

    @BeforeEach
    fun setup() {
        taskRepository = mockk()
        editTaskUseCase = EditTaskUseCase(taskRepository)
    }

    @Test
    fun `should throw exception when edit task but title is empty`() = runTest {
        val task = createTask(
            id = "A1",
            title = "",
            description = "description",
            projectId = 1,
        )

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
    fun `should throw exception when edit task but description is empty`() = runTest {
        val task = createTask(
            id = "A1",
            title = "title",
            description = "",
            projectId = 1,
        )

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
    fun `should throw exception when task not found by id`() = runTest {
        val task = createTask(
            id = "A1",
            title = "",
            description = "description",
            projectId = 1,
        )
        coEvery {
            taskRepository.editTask(task.id, task.title, task.description, task.updatedAt)
        } throws TaskNotFoundException("Task not found")

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
    fun `should edit task successfully when valid data`() = runTest {
        val task = createTask(
            id = "A1",
            title = "Updated Title",
            description = "Updated Description",
            projectId = 1
        )

        coEvery {
            taskRepository.editTask(task.id, task.title, task.description, task.updatedAt)
        } just Runs

        editTaskUseCase.editTask(
            taskId = task.id,
            title = task.title,
            description = task.description,
            updatedAt = task.updatedAt
        )
        coVerify {
            taskRepository.editTask(
                task.id,
                task.title,
                task.description,
                task.updatedAt
            )
        }
    }
}