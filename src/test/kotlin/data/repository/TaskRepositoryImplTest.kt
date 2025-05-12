package data.repository

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.example.data.datasource.task.TaskDataSource
import org.example.data.repository.TaskRepositoryImpl
import org.example.logic.entity.ProjectState
import org.example.logic.entity.Task
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.BeforeTest

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
    fun `should edit task when calling editTask`() = runTest {
        coEvery { taskDataSource.editTask(taskId, "new title", "new description", 20L) } just Runs

        taskRepository.editTask(taskId, "new title", "new description", 20L)

        coVerify { taskDataSource.editTask(taskId, "new title", "new description", 20L) }
    }

    @Test
    fun `should delete task when calling deleteTask`() = runTest {
        coEvery { taskDataSource.deleteTask(projectId, taskId) } just Runs

        taskRepository.deleteTask(projectId, taskId)

        coVerify { taskDataSource.deleteTask(projectId, taskId) }
    }

    @Test
    fun `should return task when calling getTaskById with valid id`() = runTest {
        coEvery { taskDataSource.getTaskByIdFromFile(taskId) } returns sampleTask

        val result = taskRepository.getTaskById(taskId)

        assertEquals(taskId, result.taskId)
        coVerify { taskDataSource.getTaskByIdFromFile(taskId) }
    }

    @Test
    fun `getTasksByProject returns filtered tasks`() = runTest {
        val taskId2 = UUID.fromString("123e4567-e89b-12d3-a456-426614174003")
        val project2 = UUID.fromString("123e4567-e89b-12d3-a456-426614174004")
        val tasks = listOf(sampleTask, sampleTask.copy(taskId = taskId2, projectId = project2))
        coEvery { taskDataSource.getAllTasks() } returns tasks

        val result = taskRepository.getTasksByProject(projectId)

        assertEquals(1, result.size)
        assertEquals(taskId, result[0].taskId)
    }

    @Test
    fun `when use getTaskByProject returns empty list when no tasks found`() = runTest {
        coEvery { taskDataSource.getAllTasks() } returns emptyList()

        val result = taskRepository.getTasksByProject(projectId)

        assertEquals(0, result.size)
        coVerify { taskDataSource.getAllTasks() }
    }

    @Test
    fun `when use getTasksByProject returns list of matching tasks when some match projectId`() = runTest {
        val matchingTask = sampleTask.copy(taskId = UUID.randomUUID(), projectId = projectId)
        val nonMatchingTask = sampleTask.copy(taskId = UUID.randomUUID(), projectId = UUID.randomUUID())

        coEvery { taskDataSource.getAllTasks() } returns listOf(matchingTask, nonMatchingTask)

        val result = taskRepository.getTasksByProject(projectId)

        assertEquals(1, result.size)
        assertEquals(matchingTask.taskId, result[0].taskId)
        coVerify { taskDataSource.getAllTasks() }
    }

    @Test
    fun `getTasksByProject returns empty list when no tasks match projectId`() = runTest {
        val nonMatchingTask1 = sampleTask.copy(taskId = UUID.randomUUID(), projectId = UUID.randomUUID())
        val nonMatchingTask2 = sampleTask.copy(taskId = UUID.randomUUID(), projectId = UUID.randomUUID())

        coEvery { taskDataSource.getAllTasks() } returns listOf(nonMatchingTask1, nonMatchingTask2)

        val result = taskRepository.getTasksByProject(projectId)

        assertEquals(0, result.size)
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