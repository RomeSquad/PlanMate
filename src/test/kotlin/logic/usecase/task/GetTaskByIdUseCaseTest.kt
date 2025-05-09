package logic.usecase.task

import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.example.logic.TaskNotFoundException
import org.example.logic.repository.TaskRepository
import org.example.logic.usecase.task.GetTaskByIdUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

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
        val task = createTask("19", "title", "description")
        coEvery { taskRepository.getTaskById(task.id) } returns task

        val result = getTaskByIdUseCase.getTaskById(taskId = task.id)

        assertEquals(task, result)
        coVerify { taskRepository.getTaskById(task.id) }
    }

    @Test
    fun `should throw TaskNotFoundException when not existing it`() = runTest {
        val taskId = "19"
        val exception = TaskNotFoundException("task not found")
        coEvery { taskRepository.getTaskById(taskId) } throws exception

        val result = assertThrows<TaskNotFoundException> {
            getTaskByIdUseCase.getTaskById(taskId)
        }

        assertEquals("task not found", result.message)
        coVerify { taskRepository.getTaskById(taskId) }
    }

    @Test
    fun `should throw IllegalArgumentException when id is blank`() = runTest {
        val exception = assertThrows<IllegalArgumentException> {
            getTaskByIdUseCase.getTaskById(" ")
        }

        assertEquals("taskId must not be blank", exception.message)
    }
}