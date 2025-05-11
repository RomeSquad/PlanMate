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
        val tasks = listOf(
            Task(
                id = "1",
                title = "Task 1",
                description = "Description 1",
                state = ProjectState(projectId = 1, stateName = "TODO"),
                projectId = 1,
                createdBy = "user1",
                createdAt = 1000L,
                updatedAt = 1000L
            ),
            Task(
                id = "2",
                title = "Task 2",
                description = "Description 2",
                state = ProjectState(projectId = 1, stateName = "DONE"),
                projectId = 1,
                createdBy = "user2",
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