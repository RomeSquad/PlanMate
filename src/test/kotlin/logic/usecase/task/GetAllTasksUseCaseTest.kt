package logic.usecase.task

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.entity.ProjectState
import org.example.logic.entity.Task
import org.example.logic.repository.TaskRepository
import org.example.logic.usecase.task.GetAllTasksUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetAllTasksUseCaseTest {

    private lateinit var getAllTasksUseCase: GetAllTasksUseCase
    private lateinit var taskRepository: TaskRepository

    private val projectId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")

    @BeforeEach
    fun setUp() {
        taskRepository = mockk()
        getAllTasksUseCase = GetAllTasksUseCase(taskRepository)
    }

    @Test
    fun `should return list of tasks when repository returns tasks`() = runTest {
        // Given
        val tasks = listOf(
            Task(
                taskId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f"),
                title = "Task 1",
                description = "Description 1",
                projectId = projectId,
                state = ProjectState(
                    stateName = "TODO",
                    projectId = projectId
                ),
                createdBy = UUID.fromString("a1b2c3d4-5e6f-7a8b-9c0d-1e2f3a4b5c6d"),
                createdAt = 1000L,
                updatedAt = 1000L
            ),
            Task(
                taskId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6a"), // Fixed invalid UUID
                title = "Task 2",
                description = "Description 2",
                projectId = projectId,
                state = ProjectState(
                    stateName = "DONE",
                    projectId = projectId
                ),
                createdBy = UUID.fromString("b2c3d4e5-6f7a-8b9c-0d1e-2f3a4b5c6d7e"),
                createdAt = 2000L,
                updatedAt = 2000L
            )
        )
        coEvery { taskRepository.getAllTasks() } returns tasks

        // When
        val result = getAllTasksUseCase.getAllTasks()

        // Then
        coVerify(exactly = 1) { taskRepository.getAllTasks() }
        assertEquals(tasks, result, "Task lists do not match")
        assertEquals(2, result.size, "Expected 2 tasks")
        assertEquals("Task 1", result[0].title, "First task title mismatch")
        assertEquals("Description 1", result[0].description, "First task description mismatch")
        assertEquals("TODO", result[0].state.stateName, "First task state mismatch")
        assertEquals("Task 2", result[1].title, "Second task title mismatch")
        assertEquals("Description 2", result[1].description, "Second task description mismatch")
        assertEquals("DONE", result[1].state.stateName, "Second task state mismatch")
    }

    @Test
    fun `should return empty list when repository returns no tasks`() = runTest {
        // Given
        coEvery { taskRepository.getAllTasks() } returns emptyList()

        // When
        val result = getAllTasksUseCase.getAllTasks()

        // Then
        coVerify(exactly = 1) { taskRepository.getAllTasks() }
        assertTrue(result.isEmpty(), "Expected empty task list")
    }

    @Test
    fun `should throw exception when repository fails`() = runTest {
        // Given
        val exception = RuntimeException("Database error")
        coEvery { taskRepository.getAllTasks() } throws exception

        // When/Then
        val thrownException = assertThrows<RuntimeException> {
            getAllTasksUseCase.getAllTasks()
        }
        assertEquals("Database error", thrownException.message)
        coVerify(exactly = 1) { taskRepository.getAllTasks() }
    }
}