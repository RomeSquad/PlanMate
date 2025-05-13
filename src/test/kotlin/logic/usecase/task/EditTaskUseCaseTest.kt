package logic.usecase.task

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.example.logic.exception.TaskNotFoundException
import org.example.logic.repository.TaskRepository
import org.example.logic.usecase.task.EditTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.*
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
        val projectId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
        val taskId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
        val task = createTask(
            id = taskId,
            title = "",
            description = "description",
            projectId = projectId,
        )

        assertThrows<IllegalArgumentException> {
            editTaskUseCase.editTask(
                taskId = task.taskId,
                title = task.title,
                description = task.description,
                updatedAt = task.updatedAt
            )
        }
    }

    @Test
    fun `should throw exception when edit task but description is empty`() = runTest {
        val projectId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
        val taskId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
        val task = createTask(
            id = taskId,
            title = "title",
            description = "",
            projectId = projectId,
        )

        assertThrows<IllegalArgumentException> {
            editTaskUseCase.editTask(
                taskId = task.taskId,
                title = task.title,
                description = task.description,
                updatedAt = task.updatedAt
            )
        }
    }

    @Test
    fun `should throw exception when task not found by id`() = runTest {
        val projectId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
        val taskId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
        val task = createTask(
            id = taskId,
            title = "",
            description = "description",
            projectId = projectId,
        )
        coEvery {
            taskRepository.editTask(task.taskId, task.title, task.description, task.updatedAt)
        } throws TaskNotFoundException("Task not found")

        assertThrows<IllegalArgumentException> {
            editTaskUseCase.editTask(
                taskId = task.taskId,
                title = task.title,
                description = task.description,
                updatedAt = task.updatedAt
            )
        }
    }

    @Test
    fun `should edit task successfully when valid data`() = runTest {
        val projectId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
        val taskId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
        val task = createTask(
            id = taskId,
            title = "Updated Title",
            description = "Updated Description",
            projectId = projectId
        )

        coEvery {
            taskRepository.editTask(task.taskId, task.title, task.description, task.updatedAt)
        } just Runs

        editTaskUseCase.editTask(
            taskId = task.taskId,
            title = task.title,
            description = task.description,
            updatedAt = task.updatedAt
        )
        coVerify {
            taskRepository.editTask(
                task.taskId,
                task.title,
                task.description,
                task.updatedAt
            )
        }
    }
}