package logic.usecase.task

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.entity.ProjectState
import org.example.logic.entity.Task
import org.example.logic.repository.TaskRepository
import org.example.logic.usecase.task.GetTasksByProjectIdUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class GetTasksByProjectIdUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase

    private val projectId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")

    @BeforeEach
    fun setup() {
        taskRepository = mockk()
        getTasksByProjectIdUseCase = GetTasksByProjectIdUseCase(taskRepository)
    }

    @Test
    fun `should return tasks when projectId exists`() = runTest {
        // Given
        val taskId1 = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
        val taskId2 = UUID.fromString("a1b2c3d4-5e6f-7a8b-9c0d-1e2f3a4b5c6d") // Unique taskId
        val tasks = listOf(
            Task(
                taskId = taskId1,
                title = "Task 1",
                description = "Description 1",
                state = ProjectState(projectId = projectId, stateName = "To-Do"),
                projectId = projectId,
                createdBy = UUID.fromString("b2c3d4e5-6f7a-8b9c-0d1e-2f3a4b5c6d7e"),
                createdAt = 1000L,
                updatedAt = 1000L
            ),
            Task(
                taskId = taskId2,
                title = "Task 2",
                description = "Description 2",
                state = ProjectState(projectId = projectId, stateName = "To-Do"),
                projectId = projectId,
                createdBy = UUID.fromString("b2c3d4e5-6f7a-8b9c-0d1e-2f3a4b5c6d7e"),
                createdAt = 2000L,
                updatedAt = 2000L
            )
        )
        coEvery { taskRepository.getTasksByProject(projectId) } returns tasks

        val result = getTasksByProjectIdUseCase.getTasksByProjectId(projectId)

        coVerify(exactly = 1) { taskRepository.getTasksByProject(projectId) }
        assertEquals(tasks, result, "Task lists do not match")
        assertEquals(2, result.size, "Expected 2 tasks")
        assertEquals("Task 1", result[0].title, "First task title mismatch")
        assertEquals("Task 2", result[1].title, "Second task title mismatch")
    }

    @Test
    fun `should return empty list when no tasks found for projectId`() = runTest {
        coEvery { taskRepository.getTasksByProject(projectId) } returns emptyList()

        val result = getTasksByProjectIdUseCase.getTasksByProjectId(projectId)

        coVerify(exactly = 1) { taskRepository.getTasksByProject(projectId) }
        assertTrue(result.isEmpty(), "Expected empty task list")
    }

    @Test
    fun `should throw exception when repository fails`() = runTest {
        val exception = RuntimeException("Database error")
        coEvery { taskRepository.getTasksByProject(projectId) } throws exception

        val thrownException = assertThrows<RuntimeException> {
            getTasksByProjectIdUseCase.getTasksByProjectId(projectId)
        }
        assertEquals("Database error", thrownException.message)
        coVerify(exactly = 1) { taskRepository.getTasksByProject(projectId) }
    }
}

