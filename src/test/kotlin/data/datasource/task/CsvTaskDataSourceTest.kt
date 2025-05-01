package data.datasource.task

import data.utils.CustomFile
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.example.data.datasource.task.CsvTaskDataSource
import org.example.data.utils.CsvFileReader
import org.example.data.utils.CsvFileWriter
import org.example.logic.entity.State
import org.example.logic.entity.Task
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CsvTaskDataSourceTest {

    private lateinit var csvTaskDataSource: CsvTaskDataSource
    private lateinit var csvFileReader: CsvFileReader
    private lateinit var csvFileWriter: CsvFileWriter
    private val tasksFile = CustomFile("tasks.csv")

    @BeforeEach
    fun setup() {
        csvFileReader = mockk()
        csvFileWriter = mockk()
        csvTaskDataSource = CsvTaskDataSource(csvFileReader, csvFileWriter)
    }

    @Test
    fun `when use getTaskByIdFromFile should return task when found it`() {
        val csvData = csvRowsData()
        every { csvFileReader.readCsv(tasksFile) } returns csvData

        val result = csvTaskDataSource.getTaskByIdFromFile("task1")

        assertTrue(result.isSuccess)
        assertEquals("task1", result.getOrNull()?.id)
        verify { csvFileReader.readCsv(tasksFile) }
    }

    @Test
    fun `when use getTaskByIdFromFile should not return task when not found it`() {
        val csvData = csvRowsData()
        every { csvFileReader.readCsv(tasksFile) } returns csvData

        val result = csvTaskDataSource.getTaskByIdFromFile("task3")

        assertTrue(result.isFailure)
        verify { csvFileReader.readCsv(tasksFile) }
    }

    @Test
    fun `when use getAllTasks should return list of tasks from CSV data`() {
        val csvData = csvRowsData()
        val tasksData = tasksData()
        every { csvFileReader.readCsv(tasksFile) } returns csvData

        val result = csvTaskDataSource.getAllTasks()

        assertEquals(tasksData.map { it.id }, result.getOrNull()?.map { it.id })
        verify { csvFileReader.readCsv(tasksFile) }
    }

    @Test
    fun `when use setAllTasks should set list of tasks to CSV file`() {
        val tasksData = tasksData()
        every { csvFileWriter.writeCsv(tasksFile, any()) } just Runs

        val result = csvTaskDataSource.setAllTasks(tasksData)
        assertTrue(result.isSuccess)
    }

    private fun csvRowsData(): List<List<String>> {
        return listOf (
            listOf(
                "task1",
                "title1",
                "description1",
                "[P1, To-Do]",
                "P1",
                "U1",
                "5",
                "8"
            ),
            listOf(
                "task2",
                "title2",
                "description2",
                "[P2, In-Progress]",
                "P2",
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
                state = State(
                    "P1",
                    "To-Do"
                ),
                projectId = "P1",
                createdBy = "U1",
                createdAt = 5,
                updatedAt = 8
            ),
            Task(
                id = "task2",
                title = "title2",
                description = "description2",
                state = State(
                    "P2",
                    "In-Progress"
                ),
                projectId = "P2",
                createdBy = "U2",
                createdAt = 5,
                updatedAt = 8
            )
        )
    }

}