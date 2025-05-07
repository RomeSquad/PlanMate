package data.repository

import io.mockk.*
import org.example.data.datasource.task.TaskDataSource
import org.example.data.repository.TaskRepositoryImpl
import org.example.logic.entity.State
import org.example.logic.entity.Task
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import kotlin.test.BeforeTest

class TaskRepositoryImplTest {

    private lateinit var taskDataSource: TaskDataSource
    private lateinit var taskRepository: TaskRepositoryImpl

    private val sampleTask = Task(
        id = "task1",
        title = "title1",
        description = "desc",
        state = State("1", "To-Do"),
        projectId = 1,
        createdBy = "U1",
        createdAt = 5,
        updatedAt = 10
    )

    @BeforeTest
    fun setup() {
        taskDataSource = mockk()
        taskRepository = TaskRepositoryImpl(taskDataSource)
    }

    @Test
    fun `should create task successfully when calling createTask`() {
        every { taskDataSource.createTask(any()) } just Runs

        taskRepository.createTask(sampleTask)

        verify { taskDataSource.createTask(sampleTask) }
    }

    @Test
    fun `should edit task when calling editTask`() {
        every { taskDataSource.editTask("task1", "new title", "new description", 20L) } just Runs

        taskRepository.editTask("task1", "new title", "new description", 20L)

        verify { taskDataSource.editTask("task1", "new title", "new description", 20L) }
    }

    @Test
    fun `should delete task when calling deleteTask`() {
        every { taskDataSource.deleteTask(1, "task1") } just Runs

        taskRepository.deleteTask(1, "task1")

        verify { taskDataSource.deleteTask(1, "task1") }
    }

    @Test
    fun `should return task when calling getTaskById with valid id`() {
        every { taskDataSource.getTaskByIdFromFile("task1") } returns sampleTask

        val result = taskRepository.getTaskById("task1")

        assertEquals("task1", result.id)
        verify { taskDataSource.getTaskByIdFromFile("task1") }
    }

    @Test
    fun `getTasksByProject returns filtered tasks`() {
        val tasks = listOf(sampleTask, sampleTask.copy(id = "t2", projectId = 2))
        every { taskDataSource.getAllTasks() } returns tasks

        val result = taskRepository.getTasksByProject(1)

        assertEquals(1, result.size)
        assertEquals("task1", result[0].id)
    }

    @Test
    fun `should return all tasks when calling getAllTasks`() {
        val tasks = listOf(sampleTask, sampleTask.copy(id = "task2"))
        every { taskDataSource.getAllTasks() } returns tasks

        val result = taskRepository.getAllTasks()

        assertEquals(2, result.size)
        assertEquals("task1", result[0].id)
        assertEquals("task2", result[1].id)
        verify { taskDataSource.getAllTasks() }
    }
}