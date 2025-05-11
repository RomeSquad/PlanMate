package logic.usecase.task

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.entity.ProjectState
import org.example.logic.entity.Task
import org.example.logic.repository.TaskRepository
import org.example.logic.usecase.task.GetTasksByProjectIdUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class GetTasksByProjectIdUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase

    @BeforeEach
    fun setup() {
        taskRepository = mockk()
        getTasksByProjectIdUseCase = GetTasksByProjectIdUseCase(taskRepository)
    }

    @Test
    fun `should return tasks when projectId exists`() = runTest {
        val projectId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
        val taskId1 = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
        val taskId2 = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")

        val tasks = listOf(
            projectId.createTask(taskId1, "Task 1", "Description 1"),
            projectId.createTask(taskId2, "Task 2", "Description 2")
        )
        coEvery { taskRepository.getTasksByProject(projectId) } returns tasks

        val result = getTasksByProjectIdUseCase.getTasksByProjectId(projectId)

        assertEquals(tasks, result)
        coVerify { taskRepository.getTasksByProject(projectId) }
    }

    @Test
    fun `should throw IllegalArgumentException when projectId is invalid`() = runTest {
        val projectId = UUID.fromString("00000000-0000-0000-0000-000000000000")

        val exception = assertThrows<IllegalArgumentException> {
            getTasksByProjectIdUseCase.getTasksByProjectId(projectId)
        }

        assertEquals("Project ID must be greater than zero", exception.message)
    }


    @Test
    fun `should return empty list when no tasks found for projectId`() = runTest {
        val projectId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
        val emptyList = emptyList<Task>()
        coEvery { taskRepository.getTasksByProject(projectId) } returns emptyList

        val result = getTasksByProjectIdUseCase.getTasksByProjectId(projectId)

        assertTrue(result.isEmpty())
        coVerify { taskRepository.getTasksByProject(projectId) }
    }

    private fun UUID.createTask(id: UUID, title: String, description: String): Task {
        return Task(
            taskId = id,
            title = title,
            description = description,
            state = ProjectState(this, "To-Do"),
            projectId = this,
            createdBy = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f"),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    }
}

