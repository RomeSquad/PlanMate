package logic.usecase.task

import io.mockk.*
import org.example.logic.repository.TaskRepository
import org.example.logic.usecase.task.DeleteTaskUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class DeleteTaskUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase

    @BeforeEach
    fun setup() {
        taskRepository = mockk(relaxed = true)
        deleteTaskUseCase = DeleteTaskUseCase(taskRepository)
    }

    @Test
    fun `should delete task successfully`() {
        val projectId = 1
        val taskId = "T-1"

        every { taskRepository.deleteTask(projectId, taskId) } just Runs

        deleteTaskUseCase.deleteTask(projectId, taskId)

        verify(exactly = 1) { taskRepository.deleteTask(projectId, taskId) }
    }

    @Test
    fun `should throw exception when projectId is blank`() {
        val exception = assertThrows<IllegalArgumentException> {
            deleteTaskUseCase.deleteTask(0, "T-1")
        }

        assertEquals("projectId must not be blank", exception.message)
        verify { taskRepository wasNot Called }
    }

    @Test
    fun `should throw exception when taskId is blank`() {
        val exception = assertThrows<IllegalArgumentException> {
            deleteTaskUseCase.deleteTask(1, " ")
        }

        assertEquals("taskId must not be blank", exception.message)
        verify { taskRepository wasNot Called }
    }

    @Test
    fun `should propagate exception from repository`() {
        val projectId = 1
        val taskId = "T-404"

        every { taskRepository.deleteTask(projectId, taskId) } throws RuntimeException("Task not found")

        val exception = assertThrows<RuntimeException> {
            deleteTaskUseCase.deleteTask(projectId, taskId)
        }

        assertEquals("Task not found", exception.message)
        verify { taskRepository.deleteTask(projectId, taskId) }
    }
}
