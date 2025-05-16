package data.datasource.task

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.example.data.datasource.mapper.toCsvRow
import org.example.data.datasource.task.CsvTaskDataSource
import org.example.data.utils.CsvFileReader
import org.example.data.utils.CsvFileWriter
import org.example.logic.entity.ProjectState
import org.example.logic.entity.Task
import org.example.logic.exception.TaskNotFoundException
import org.example.logic.request.TaskDeletionRequest
import org.example.logic.request.TaskEditRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class CsvTaskDataSourceTest {
    private lateinit var csvTaskDataSource: CsvTaskDataSource
    private lateinit var csvFileReader: CsvFileReader
    private lateinit var csvFileWriter: CsvFileWriter
    private val tasksFile = File("tasks.csv")

    private val firstProjectId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000")
    private val secondProjectId = UUID.fromString("550e8400-e29b-41d4-a716-446655440006")
    private val firstTaskId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001")
    private val secondTaskId = UUID.fromString("550e8400-e29b-41d4-a716-446655440002")
    private val thirdTaskId = UUID.fromString("550e8400-e29b-41d4-a716-446655440003")
    private val firstUserID = UUID.fromString("550e8400-e29b-41d4-a716-446655440004")
    private val secondUserID = UUID.fromString("550e8400-e29b-41d4-a716-446655440005")

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

        csvTaskDataSource.editTask(TaskEditRequest(firstTaskId, "new title", "new description", 19))

        coVerify { csvFileReader.readCsv(tasksFile) }
        coVerify { csvFileWriter.writeCsv(tasksFile, any()) }
    }

    @Test
    fun `when use editTask should throw exception when task not found`() = runTest {
        val csvData = csvRowsData()
        coEvery { csvFileReader.readCsv(tasksFile) } returns csvData

        assertThrows<TaskNotFoundException> {
            csvTaskDataSource.editTask(TaskEditRequest(thirdTaskId, "title", "description", 19))
        }
        coVerify { csvFileReader.readCsv(tasksFile) }
    }

    @Test
    fun `when use getTaskByProjectId should return tasks by same projectId`() = runTest {
        val csvData = csvRowsData()
        coEvery { csvFileReader.readCsv(tasksFile) } returns csvData

        val result = csvTaskDataSource.getTasksByProjectId(firstProjectId)

        assertEquals(firstTaskId, result.first().taskId)
        coVerify { csvFileReader.readCsv(tasksFile) }
    }

    @Test
    fun `when use getTaskByIdFromFile should return task when found it`() = runTest {
        val csvData = csvRowsData()
        coEvery { csvFileReader.readCsv(tasksFile) } returns csvData

        val result = csvTaskDataSource.getTaskByIdFromFile(firstTaskId)

        assertEquals(firstTaskId, result.taskId)
        coVerify { csvFileReader.readCsv(tasksFile) }
    }

    @Test
    fun `when use getTaskByIdFromFile should not return task when not found it`() = runTest {
        val csvData = csvRowsData()
        coEvery { csvFileReader.readCsv(tasksFile) } returns csvData

        assertThrows<TaskNotFoundException> {
            csvTaskDataSource.getTaskByIdFromFile(thirdTaskId)
        }
        coVerify { csvFileReader.readCsv(tasksFile) }
    }

    @Test
    fun `when use getAllTasks should return list of tasks from CSV data`() = runTest {
        val csvData = csvRowsData()
        val tasksData = tasksData()
        coEvery { csvFileReader.readCsv(tasksFile) } returns csvData

        val result = csvTaskDataSource.getAllTasks()

        assertEquals(tasksData.map { it.taskId }, result.map { it.taskId })
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
        val notFoundProjectId = UUID.fromString("550e8400-e29b-41d4-a716-446655440017")
        coEvery { csvFileReader.readCsv(tasksFile) } returns csvData

        assertThrows<TaskNotFoundException> {
            csvTaskDataSource.deleteTask(TaskDeletionRequest(notFoundProjectId, thirdTaskId))
        }
        coVerify { csvFileReader.readCsv(tasksFile) }
    }

    @Test
    fun `when use deleteTask should delete task when found it`() = runTest {
        val csvData = csvRowsData()
        val tasksData = tasksData().toMutableList()
        coEvery { csvFileReader.readCsv(tasksFile) } returns csvData
        coEvery { csvFileWriter.writeCsv(tasksFile, any()) } just Runs

        csvTaskDataSource.deleteTask(TaskDeletionRequest(firstProjectId, firstTaskId))

        val remainTasks = tasksData.filterNot { it.projectId == firstProjectId && it.taskId == firstTaskId }
        remainTasks.forEach {
            coVerify { csvFileWriter.writeCsv(tasksFile, listOf(it.toCsvRow())) }
        }
    }

    @Test
    fun `when use deleteTask should throw exception when taskId not match`() = runTest {
        val csvData = csvRowsData()
        coEvery { csvFileReader.readCsv(tasksFile) } returns csvData

        assertThrows<TaskNotFoundException> {
            csvTaskDataSource.deleteTask(TaskDeletionRequest(firstProjectId, thirdTaskId))
        }
        coVerify { csvFileReader.readCsv(tasksFile) }
    }

    @Test
    fun `when use deleteTask should throw exception when projectId not match`() = runTest {
        val csvData = csvRowsData()
        val notFoundProjectId = UUID.fromString("550e8400-e29b-41d4-a716-446655440017")
        coEvery { csvFileReader.readCsv(tasksFile) } returns csvData

        assertThrows<TaskNotFoundException> {
            csvTaskDataSource.deleteTask(TaskDeletionRequest(notFoundProjectId, firstTaskId))
        }
        coVerify { csvFileReader.readCsv(tasksFile) }
    }

    private fun csvRowsData(): List<List<String>> {
        return listOf(
            listOf(
                firstTaskId.toString(),
                "title1",
                "description1",
                "[${firstProjectId}, To-Do]",
                firstProjectId.toString(),
                firstUserID.toString(),
                "5",
                "8"
            ),
            listOf(
                secondTaskId.toString(),
                "title2",
                "description2",
                "[${secondProjectId}, In-Progress]",
                secondProjectId.toString(),
                secondUserID.toString(),
                "5",
                "8"
            )
        )
    }

    private fun tasksData(): List<Task> {
        return listOf(
            Task(
                taskId = firstTaskId,
                title = "title1",
                description = "description1",
                state = ProjectState(
                    firstProjectId,
                    "To-Do"
                ),
                projectId = firstProjectId,
                createdBy = firstUserID,
                createdAt = 5,
                updatedAt = 8
            ),
            Task(
                taskId = secondTaskId,
                title = "title2",
                description = "description2",
                state = ProjectState(
                    secondProjectId,
                    "In-Progress"
                ),
                projectId = secondProjectId,
                createdBy = secondUserID,
                createdAt = 5,
                updatedAt = 8
            )
        )
    }
}