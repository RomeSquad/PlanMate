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
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetAllTasksUseCaseTest {
    private lateinit var getAllTasksUseCase: GetAllTasksUseCase
    private val taskRepository: TaskRepository = mockk()

    @BeforeEach
    fun setUp() {
        getAllTasksUseCase = GetAllTasksUseCase(taskRepository)
    }

    @Test
    fun `getAllTasks should return list of tasks when repository returns tasks`() = runTest {
        val projectId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")

        val tasks = listOf(
            Task(
                taskId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f"),
                title = "Task 1",
                description = "Description 1",
                state = ProjectState(projectId = projectId, stateName = "TODO"),
                projectId = projectId,
                createdBy = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f"),
                createdAt = 1000L,
                updatedAt = 1000L
            ),
            Task(
                taskId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6s"),
                title = "Task 2",
                description = "Description 2",
                state = ProjectState(projectId = projectId, stateName = "DONE"),
                projectId = projectId,
                createdBy = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e1a"),
                createdAt = 2000L,
                updatedAt = 2000L
            )
        )
        coEvery { taskRepository.getAllTasks() } returns tasks

        val result = getAllTasksUseCase.getAllTasks()

        coVerify(exactly = 1) { taskRepository.getAllTasks() }
        assertEquals(tasks, result)
        assertEquals(2, result.size)
        assertEquals("Task 1", result[0].title)
        assertEquals("Task 2", result[1].title)
    }

    @Test
    fun `getAllTasks should return empty list when repository returns no tasks`() = runTest {
        coEvery { taskRepository.getAllTasks() } returns emptyList()

        val result = getAllTasksUseCase.getAllTasks()

        coVerify(exactly = 1) { taskRepository.getAllTasks() }
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getAllTasks should throw exception when repository fails`() = runTest {
        val exception = RuntimeException("Database error")
        coEvery { taskRepository.getAllTasks() } throws exception

        try {
            getAllTasksUseCase.getAllTasks()
            assertTrue(false, "Expected exception to be thrown")
        } catch (e: RuntimeException) {
            assertEquals("Database error", e.message)
        }
        coVerify(exactly = 1) { taskRepository.getAllTasks() }
    }
}