package usecase

import io.mockk.every
import io.mockk.mockk
import org.example.logic.entity.State
import org.example.logic.entity.Task
import org.example.logic.usecase.task.GetAllTasksUseCase
import org.example.logic.repository.TaskRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GetAllTasksUseCaseTest {

    private val taskRepository = mockk<TaskRepository>()
    private val useCase = GetAllTasksUseCase(taskRepository)

    private val sampleTask = Task(
        id = "t1",
        title = "Task Title",
        description = "Description",
        state = State("1", "To-Do"),
        projectId = 1,
        createdBy = "Admin",
        createdAt = 100L,
        updatedAt = 200L
    )

    @Test
    fun `should return list of tasks when repository succeeds`() {
        val tasks = listOf(sampleTask)
        every { taskRepository.getAllTasks() } returns Result.success(tasks)

        val result = useCase.getAllTasks()

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrThrow().size)
        assertEquals("t1", result.getOrThrow()[0].id)
    }

    @Test
    fun `should return failure when repository throws exception`() {
        val exception = RuntimeException("Something went wrong")
        every { taskRepository.getAllTasks() } throws exception

        val result = useCase.getAllTasks()

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
