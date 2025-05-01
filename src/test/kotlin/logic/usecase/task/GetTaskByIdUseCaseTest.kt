package logic.usecase.task

import io.mockk.every
import io.mockk.mockk
import logic.usecase.createTask
import org.example.data.datasource.task.TaskDataSource
import org.example.logic.TaskNotFoundException
import org.example.logic.repository.TaskRepository
import org.example.logic.usecase.task.GetTaskByIdUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetTaskByIdUseCaseTest {

    private lateinit var taskDataSource: TaskDataSource
    private lateinit var taskRepository: TaskRepository
    private lateinit var getTaskByIdUseCase: GetTaskByIdUseCase

    @BeforeEach
    fun setup() {
        taskRepository = mockk()
        taskDataSource = mockk()
        getTaskByIdUseCase = GetTaskByIdUseCase(taskRepository)
    }

    @Test
    fun `should return task when existing it`() {
        val task = createTask("19","title", "description")
        every { taskDataSource.getTaskByIdFromFile(task.id) } returns task

        val result = getTaskByIdUseCase.getTaskById(taskId = task.id)

        assertEquals(task, result)
    }

    @Test
    fun `should throw TaskNotFoundException when not existing it`() {
        val taskId = "19"
        every { taskDataSource.getTaskByIdFromFile(taskId = taskId) } throws TaskNotFoundException("task not found")

        assertThrows<TaskNotFoundException> {
            getTaskByIdUseCase.getTaskById(taskId)
        }
    }

    @Test
    fun `should throw TaskNotFoundException when id is empty`() {
        val taskId = ""
        every { taskDataSource.getTaskByIdFromFile(taskId) } throws TaskNotFoundException("task not found")

        assertThrows<TaskNotFoundException> {
            getTaskByIdUseCase.getTaskById(taskId)
        }
    }
}