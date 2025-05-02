package data.repository

import io.mockk.*
import org.example.data.datasource.task.TaskDataSource
import org.example.data.repository.TaskRepositoryImpl
import org.example.logic.entity.State
import org.example.logic.entity.Task
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.test.BeforeTest
import kotlin.test.assertFailsWith

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
        every { taskDataSource.createTask(any()) } returns Result.success(Unit)

        val result = taskRepository.createTask(sampleTask)

        assertTrue(result.isSuccess)
        verify { taskDataSource.createTask(sampleTask) }
    }
    @Test
    fun `should update task fields when calling editTask with valid id`() {
        val updatedTitle = "New Title"
        val updatedDesc = "New Desc"
        val updatedAt = 99L

        val tasks = listOf(sampleTask)
        every { taskDataSource.getAllTasks() } returns Result.success(tasks)
        every { taskDataSource.setAllTasks(any()) } returns Result.success(Unit)

        taskRepository.editTask("task1", updatedTitle, updatedDesc, updatedAt)

        verify {
            taskDataSource.setAllTasks(withArg {
                assertEquals(1, it.size)
                assertEquals(updatedTitle, it[0].title)
                assertEquals(updatedDesc, it[0].description)
                assertEquals(updatedAt, it[0].updatedAt)
            })
        }
    }

    @Test
    fun `editTask throws if task not found`() {
        every { taskDataSource.getAllTasks() } returns Result.success(emptyList())

        val exception = assertFailsWith <NoSuchElementException> {
            taskRepository.editTask("taskX", "title", "desc", 0)
        }

        assertTrue(exception.message!!.contains("not found"))
    }

    @Test
    fun `should return task when calling getTaskById with valid id`() {
        every { taskDataSource.getTaskByIdFromFile("task1") } returns Result.success(sampleTask)

        val result = taskRepository.getTaskById("task1")

        assertTrue(result.isSuccess)
        assertEquals("task1", result.getOrNull()?.id)
        verify { taskDataSource.getTaskByIdFromFile("task1") }
    }

    @Test
    fun `getTasksByProject returns filtered tasks`() {
        val tasks = listOf(sampleTask, sampleTask.copy(id = "t2", projectId = 2))
        every { taskDataSource.getAllTasks() } returns Result.success(tasks)

        val result = taskRepository.getTasksByProject(1)

        assertEquals(1, result.size)
        assertEquals("task1", result[0].id)
    }

    @Test
    fun `should delete task when calling deleteTask`() {
        every { taskDataSource.deleteTask(1, "task1") } just  Runs

        taskRepository.deleteTask(1, "task1")

        verify { taskDataSource.deleteTask(1, "task1") }
    }
}