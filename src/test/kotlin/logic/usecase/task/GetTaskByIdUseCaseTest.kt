package logic.usecase.task

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
    fun `should return task when existing it`() {
        val task = createTask("19", "title", "description")
        every { taskRepository.getTaskById(task.id) } returns task

        val result = getTaskByIdUseCase.getTaskById(taskId = task.id)

        assertEquals(task, result)
        verify { taskRepository.getTaskById(task.id) }
    }

    @Test
    fun `should throw TaskNotFoundException when not existing it`() {
        val taskId = "19"
        val exception = TaskNotFoundException("task not found")
        every { taskRepository.getTaskById(taskId) } throws exception

        val result = assertThrows<TaskNotFoundException> {
            getTaskByIdUseCase.getTaskById(taskId)
        }

        assertEquals("task not found", result.message)
        verify { taskRepository.getTaskById(taskId) }
    }

    @Test
    fun `should throw IllegalArgumentException when id is blank`() {
        val exception = assertThrows<IllegalArgumentException> {
            getTaskByIdUseCase.getTaskById(" ")
        }

        assertEquals("taskId must not be blank", exception.message)
    }
}