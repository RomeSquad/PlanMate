package data.datasource.project

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.example.data.datasource.project.CsvProjectDataSource
import org.example.data.utils.CsvFileReader
import org.example.data.utils.CsvFileWriter
import org.example.logic.entity.ChangeHistory
import org.example.logic.entity.Project
import org.example.logic.entity.State
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class CsvProjectDataSourceTest {

    private lateinit var projectDataSource: CsvProjectDataSource
    private lateinit var csvFileReader: CsvFileReader
    private lateinit var csvFileWriter: CsvFileWriter
    private val projectsFile = File("project3.csv")
    private val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
    private val date = dateFormat.parse("Thu May 01 00:25:13 GMT+3 2025")
    @BeforeEach
    fun setup() {
        csvFileReader = mockk()
        csvFileWriter = mockk()
        projectDataSource = CsvProjectDataSource(csvFileReader, csvFileWriter, projectsFile)
    }

    @Test
    fun getAllProjects_shouldReturnListOfProjectsFromCSV() = runBlocking {
          val csvRows = listOf(
            listOf("1","PlanMate","PlanMate Description",
                "[[5, , 4, , Thu May 01 00:25:13 GMT+3 2025]]",
                "[12, in progress]"
            ),
            listOf("2","PlanMate","PlanMate Description",
                "[[6, , 7, , Thu May 01 00:25:13 GMT+3 2025]]",
                "[12, in progress]"
            )
        )

        val expectedProjects = listOf(
            Project(
                name = "PlanMate",
                description = "PlanMate Description",
                changeHistory = listOf(
                    ChangeHistory("5", "", "4","inatial creation",  date)
                ),
                state = State("12", "in progress"),
                id = 1
            ),
            Project(
                name = "PlanMate",
                description = "PlanMate Description",
                changeHistory = listOf(
                    ChangeHistory("6", "", "7", "inatial creation", date)
                ),
                state = State("12", "in progress"),
                id = 2
            )
        )

        every { csvFileReader.readCsv(projectsFile) } returns csvRows

        val result = projectDataSource.getAllProjects()

        assertEquals(expectedProjects.map { it.id }, result.map { it.id })
        verify { csvFileReader.readCsv(projectsFile) }
    }

    @Test
    fun saveAllProjects_shouldWriteProjectsToCSV() = runBlocking {
          val projects = listOf(
            Project(
                name = "PlanMate",
                description = "PlanMate Description",
                changeHistory = listOf(
                    ChangeHistory("5", "", "4", "inatial creation",date)
                ),
                state = State("12", "in progress"),
                id = 1
            )
        )

        every { csvFileWriter.writeCsv(projectsFile, any()) } just Runs

        val result = projectDataSource.saveAllProjects(projects)

        assertEquals(Unit,result)
        verify { csvFileWriter.writeCsv(projectsFile, any()) }
    }
}
