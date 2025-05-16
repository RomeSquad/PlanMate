package data.repository

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.example.data.datasource.task.TaskDataSource
import org.example.data.repository.TaskRepositoryImpl
import org.example.logic.entity.ProjectState
import org.example.logic.entity.Task
import org.example.logic.request.TaskDeletionRequest
import org.example.logic.request.TaskEditRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.assertTrue

class TaskRepositoryImplTest {
    private lateinit var taskDataSource: TaskDataSource
    private lateinit var taskRepository: TaskRepositoryImpl

    private val taskId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
    private val projectId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001")
    private val userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174002")

    private val sampleTask = Task(
        taskId = taskId,
        title = "title1",
        description = "desc",
        state = ProjectState(projectId, "To-Do"),
        projectId = projectId,
        createdBy = userId,
        createdAt = 5,
        updatedAt = 10
    )

    @BeforeTest
    fun setup() {
        taskDataSource = mockk()
        taskRepository = TaskRepositoryImpl(taskDataSource)
    }

    @Test
    fun `should create task successfully when calling createTask`() = runTest {
        coEvery { taskDataSource.createTask(any()) } just Runs

        taskRepository.createTask(sampleTask)

        coVerify { taskDataSource.createTask(sampleTask) }
    }

    @Test
    fun `should edit task successfully when calling editTask`() = runTest {
        coEvery { taskDataSource.editTask(TaskEditRequest(taskId, "new title", "new description", 20L)) } just Runs

        taskRepository.editTask(TaskEditRequest(taskId, "new title", "new description", 20L))

        coVerify { taskDataSource.editTask(TaskEditRequest(taskId, "new title", "new description", 20L)) }
    }

    @Test
    fun `should delete task successfully when calling deleteTask`() = runTest {
        coEvery { taskDataSource.deleteTask(TaskDeletionRequest(projectId, taskId)) } just Runs

        taskRepository.deleteTask(TaskDeletionRequest(projectId, taskId))

        coVerify { taskDataSource.deleteTask(TaskDeletionRequest(projectId, taskId)) }
    }

    @Test
    fun `should return task when calling getTaskById with valid id`() = runTest {
        coEvery { taskDataSource.getTaskByIdFromFile(taskId) } returns sampleTask

        val result = taskRepository.getTaskById(taskId)

        assertEquals(taskId, result.taskId)
        coVerify { taskDataSource.getTaskByIdFromFile(taskId) }
    }

    @Test
    fun `should return tasks by projectId when match tasks by projectId`() = runTest {
        val matchTask = sampleTask.copy(taskId = UUID.randomUUID(), projectId = projectId)
        val notMatchTask = sampleTask.copy(taskId = UUID.randomUUID(), projectId = UUID.randomUUID())
        coEvery { taskDataSource.getAllTasks() } returns listOf(matchTask, notMatchTask)

        val result = taskRepository.getTasksByProject(projectId)

        assertEquals(listOf(matchTask), result)
        coVerify { taskDataSource.getAllTasks() }
    }

    @Test
    fun `should return empty list of tasks when no tasks match projectId`() = runTest {
        val firstNotMatchTask = sampleTask.copy(taskId = UUID.randomUUID(), projectId = UUID.randomUUID())
        val secondNotMatchTask = sampleTask.copy(taskId = UUID.randomUUID(), projectId = UUID.randomUUID())
        coEvery { taskDataSource.getAllTasks() } returns listOf(firstNotMatchTask, secondNotMatchTask)

        val result = taskRepository.getTasksByProject(UUID.randomUUID())

        assertTrue(result.isEmpty())
        coVerify { taskDataSource.getAllTasks() }
    }

    @Test
    fun `should return all tasks when calling getAllTasks`() = runTest {
        val taskId2 = UUID.fromString("123e4567-e89b-12d3-a456-426614174003")
        val tasks = listOf(sampleTask, sampleTask.copy(taskId = taskId2))
        coEvery { taskDataSource.getAllTasks() } returns tasks

        val result = taskRepository.getAllTasks()

        assertEquals(2, result.size)
        coVerify { taskDataSource.getAllTasks() }
    }
}