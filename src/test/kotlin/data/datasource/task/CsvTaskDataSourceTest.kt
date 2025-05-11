package data.datasource.task

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.example.data.datasource.task.CsvTaskDataSource
import org.example.data.datasource.mapper.toCsvRow
import org.example.data.utils.CsvFileReader
import org.example.data.utils.CsvFileWriter
import org.example.logic.entity.ProjectState
import org.example.logic.entity.Task
import org.example.logic.exception.TaskNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class CsvTaskDataSourceTest {

    private lateinit var csvTaskDataSource: CsvTaskDataSource
    private lateinit var csvFileReader: CsvFileReader
    private lateinit var csvFileWriter: CsvFileWriter
    private val tasksFile = File("tasks.csv")

    @BeforeEach
    fun setup() {
        csvFileReader = mockk()
        csvFileWriter = mockk()
        csvTaskDataSource = CsvTaskDataSource(csvFileReader, csvFileWriter, tasksFile)
    }

    @Test
    fun `when use createTask should write task to csv file`() = runTest {
        val task = tasksData().first()
        coEvery { csvFileWriter.writeCsv(tasksFile, listOf(task.toCsvRow())) } just Runs

        csvTaskDataSource.createTask(task)

        coVerify { csvFileWriter.writeCsv(tasksFile, listOf(task.toCsvRow())) }
    }

    @Test
    fun `when use editTask should update task in csv file`() = runTest {
        val csvData = csvRowsData()
        coEvery { csvFileReader.readCsv(tasksFile) } returns csvData
        coEvery { csvFileWriter.writeCsv(tasksFile, any()) } just Runs

        csvTaskDataSource.editTask("task1", "new title", "new description", 19)

        coVerify { csvFileReader.readCsv(tasksFile) }
        coVerify { csvFileWriter.writeCsv(tasksFile, any()) }
    }

    @Test
    fun `when use editTask should throw exception when task not found`() = runTest {
        val csvData = csvRowsData()
        coEvery { csvFileReader.readCsv(tasksFile) } returns csvData

        assertThrows<TaskNotFoundException> {
            csvTaskDataSource.editTask("task3", "title", "description", 19)
        }
        coVerify { csvFileReader.readCsv(tasksFile) }
    }

    @Test
    fun `when use getTaskByProjectId should return tasks by same projectId`() = runTest {
        val csvData = csvRowsData()
        coEvery { csvFileReader.readCsv(tasksFile) } returns csvData

        val result = csvTaskDataSource.getTasksByProjectId(1)

        assertEquals("task1", result.first().id)
        coVerify { csvFileReader.readCsv(tasksFile) }
    }

    @Test
    fun `when use getTaskByIdFromFile should return task when found it`() = runTest {
        val csvData = csvRowsData()
        coEvery { csvFileReader.readCsv(tasksFile) } returns csvData

        val result = csvTaskDataSource.getTaskByIdFromFile("task1")

        assertEquals("task1", result.id)
        coVerify { csvFileReader.readCsv(tasksFile) }
    }

    @Test
    fun `when use getTaskByIdFromFile should not return task when not found it`() = runTest {
        val csvData = csvRowsData()
        coEvery { csvFileReader.readCsv(tasksFile) } returns csvData

        assertThrows<TaskNotFoundException> {
            csvTaskDataSource.getTaskByIdFromFile("task3")
        }
        coVerify { csvFileReader.readCsv(tasksFile) }
    }

    @Test
    fun `when use getAllTasks should return list of tasks from CSV data`() = runTest {
        val csvData = csvRowsData()
        val tasksData = tasksData()
        coEvery { csvFileReader.readCsv(tasksFile) } returns csvData

        val result = csvTaskDataSource.getAllTasks()

        assertEquals(tasksData.map { it.id }, result.map { it.id })
        coVerify { csvFileReader.readCsv(tasksFile) }
    }

    @Test
    fun `when use setAllTasks should set list of tasks to CSV file`() = runTest {
        val tasksData = tasksData()
        coEvery { csvFileWriter.writeCsv(tasksFile, any()) } just Runs

        csvTaskDataSource.saveAllTasks(tasksData)

        tasksData.forEach {
            coVerify { csvFileWriter.writeCsv(tasksFile, listOf(it.toCsvRow())) }
        }
    }

    @Test
    fun `when use deleteTask should throw exception when task not found`() = runTest {
        val csvData = csvRowsData()
        coEvery { csvFileReader.readCsv(tasksFile) } returns csvData

        assertThrows<TaskNotFoundException> {
            csvTaskDataSource.deleteTask(3, "task3")
        }
        coVerify { csvFileReader.readCsv(tasksFile) }
    }

    @Test
    fun `when use deleteTask should delete task when found it`() = runTest {
        val csvData = csvRowsData()
        val tasksData = tasksData().toMutableList()
        coEvery { csvFileReader.readCsv(tasksFile) } returns csvData
        coEvery { csvFileWriter.writeCsv(tasksFile, any()) } just Runs

        csvTaskDataSource.deleteTask(1, "task1")

        val remainTasks = tasksData.filterNot { it.projectId == 1 && it.id == "task1" }
        remainTasks.forEach {
            coVerify { csvFileWriter.writeCsv(tasksFile, listOf(it.toCsvRow())) }
        }
    }

    @Test
    fun `when use deleteTask should throw exception when taskId not match`() = runTest {
        val csvData = csvRowsData()
        coEvery { csvFileReader.readCsv(tasksFile) } returns csvData

        assertThrows<TaskNotFoundException> {
            csvTaskDataSource.deleteTask(1, "task3")
        }
        coVerify { csvFileReader.readCsv(tasksFile) }
    }

    @Test
    fun `when use deleteTask should throw exception when projectId not match`() = runTest {
        val csvData = csvRowsData()
        coEvery { csvFileReader.readCsv(tasksFile) } returns csvData

        assertThrows<TaskNotFoundException> {
            csvTaskDataSource.deleteTask(3, "task1")
        }
        coVerify { csvFileReader.readCsv(tasksFile) }
    }

    private fun csvRowsData(): List<List<String>> {
        return listOf(
            listOf(
                "task1",
                "title1",
                "description1",
                "[1, To-Do]",
                "1",
                "U1",
                "5",
                "8"
            ),
            listOf(
                "task2",
                "title2",
                "description2",
                "[2, In-Progress]",
                "2",
                "U2",
                "5",
                "8"
            )
        )
    }

    private fun tasksData(): List<Task> {
        return listOf(
            Task(
                id = "task1",
                title = "title1",
                description = "description1",
                state = ProjectState(
                    1,
                    "To-Do"
                ),
                projectId = 1,
                createdBy = "U1",
                createdAt = 5,
                updatedAt = 8
            ),
            Task(
                id = "task2",
                title = "title2",
                description = "description2",
                state = ProjectState(
                    2,
                    "In-Progress"
                ),
                projectId = 2,
                createdBy = "U2",
                createdAt = 5,
                updatedAt = 8
            )
        )
    }
}