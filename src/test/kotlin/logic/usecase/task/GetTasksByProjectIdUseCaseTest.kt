package logic.usecase.task

import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import io.mockk.*
import org.example.logic.entity.State
import org.example.logic.entity.Task
import org.example.logic.repository.TaskRepository
import org.example.logic.usecase.task.GetTasksByProjectIdUseCase
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Test

class GetTasksByProjectIdUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase

    @BeforeEach
    fun setup() {
        taskRepository = mockk()
        getTasksByProjectIdUseCase = GetTasksByProjectIdUseCase(taskRepository)
    }

    @Test
    fun `should return tasks when projectId exists`() {
        val projectId = 1
        val tasks = listOf(
            projectId.createTask("1", "Task 1", "Description 1"),
            projectId.createTask("2", "Task 2", "Description 2")
        )
        every { taskRepository.getTasksByProject(projectId) } returns tasks

        val result = getTasksByProjectIdUseCase.getTasksByProjectId(projectId)

        assertEquals(tasks, result)
        verify { taskRepository.getTasksByProject(projectId) }
    }
    @Test
    fun `should throw IllegalArgumentException when projectId is invalid`() {
        val projectId = 0

        val exception = assertThrows<IllegalArgumentException> {
            getTasksByProjectIdUseCase.getTasksByProjectId(projectId)
        }

        assertEquals("Project ID must be greater than zero", exception.message)
    }


    @Test
    fun `should return empty list when no tasks found for projectId`() {
        val projectId = 1
        val emptyList = emptyList<Task>()
        every { taskRepository.getTasksByProject(projectId) } returns emptyList

        val result = getTasksByProjectIdUseCase.getTasksByProjectId(projectId)

        assertTrue(result.isEmpty())
        verify { taskRepository.getTasksByProject(projectId) }
    }

    private fun Int.createTask(id: String, title: String, description: String): Task {
        return Task(
            id = id,
            title = title,
            description = description,
            state = State("1", "To-Do"),
            projectId = this,
            createdBy = "Admin",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    }
}

