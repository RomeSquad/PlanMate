package logic.usecase.task

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.repository.TaskRepository
import org.example.logic.usecase.task.GetTaskByIdUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetTaskByIdUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var getTaskByIdUseCase: GetTaskByIdUseCase

    @BeforeEach
    fun setup() {
        taskRepository = mockk()
        getTaskByIdUseCase = GetTaskByIdUseCase(taskRepository)
    }

    @Test
    fun `should return task when existing it`() = runTest {
        val task = createTask(title = "title", description = "description")
        coEvery { taskRepository.getTaskById(task.taskId) } returns task

        val result = getTaskByIdUseCase.getTaskById(taskId = task.taskId)

        assertEquals(task, result)
        coVerify { taskRepository.getTaskById(task.taskId) }
    }
}