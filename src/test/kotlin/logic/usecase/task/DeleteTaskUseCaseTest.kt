package logic.usecase.task

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.repository.TaskRepository
import org.example.logic.request.TaskDeletionRequest
import org.example.logic.usecase.task.DeleteTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertEquals

class DeleteTaskUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private val projectId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
    private val taskId = UUID.fromString("a1b2c3d4-5e6f-7a8b-9c0d-1e2f3a4b5c6d")
    private val nonExistentTaskId = UUID.fromString("b2c3d4e5-6f7a-8b9c-0d1e-2f3a4b5c6d7e")
    private val nonExistentProjectId = UUID.fromString("c3d4e5f6-7a8b-9c0d-1e2f-3a4b5c6d7e8f")

    @BeforeEach
    fun setup() {
        taskRepository = mockk()
        deleteTaskUseCase = DeleteTaskUseCase(taskRepository)
    }

    @Test
    fun `should delete task successfully`() = runTest {
        coEvery { taskRepository.deleteTask(TaskDeletionRequest(projectId, taskId)) } returns Unit

        deleteTaskUseCase.deleteTask(projectId, taskId)

        coVerify(exactly = 1) { taskRepository.deleteTask(TaskDeletionRequest(projectId, taskId)) }
    }

    @Test
    fun `should throw NoSuchElementException when task does not exist`() = runTest {
        coEvery {
            taskRepository.deleteTask(
                TaskDeletionRequest(
                    projectId,
                    nonExistentTaskId
                )
            )
        } throws NoSuchElementException("Task with id $nonExistentTaskId not found")

        val exception = assertThrows<NoSuchElementException> {
            deleteTaskUseCase.deleteTask(projectId, nonExistentTaskId)
        }
        assertEquals("Task with id $nonExistentTaskId not found", exception.message)
        coVerify(exactly = 1) { taskRepository.deleteTask((TaskDeletionRequest(projectId, nonExistentTaskId))) }
    }

    @Test
    fun `should throw NoSuchElementException when project does not exist`() = runTest {
        coEvery {
            taskRepository.deleteTask(
                TaskDeletionRequest(
                    nonExistentProjectId,
                    taskId
                )
            )
        } throws NoSuchElementException("Project with id $nonExistentProjectId not found")

        val exception = assertThrows<NoSuchElementException> {
            deleteTaskUseCase.deleteTask(nonExistentProjectId, taskId)
        }
        assertEquals("Project with id $nonExistentProjectId not found", exception.message)
        coVerify(exactly = 1) { taskRepository.deleteTask(TaskDeletionRequest(nonExistentProjectId, taskId)) }
    }

    @Test
    fun `should propagate RuntimeException from repository`() = runTest {
        val repositoryException = RuntimeException("Database error")
        coEvery { taskRepository.deleteTask(TaskDeletionRequest(projectId, taskId)) } throws repositoryException

        val exception = assertThrows<RuntimeException> {
            deleteTaskUseCase.deleteTask(projectId, taskId)
        }
        assertEquals("Database error", exception.message)
        coVerify(exactly = 1) { taskRepository.deleteTask(TaskDeletionRequest(projectId, taskId)) }
    }
}
