package data.repository

import io.mockk.every
import io.mockk.mockk
import org.example.data.datasource.project.ProjectDataSource
import org.example.data.repository.ChangeHistoryRepositoryImpl
import org.example.logic.entity.ChangeHistory
import org.example.logic.entity.Project
import org.example.logic.repository.ChangeHistoryRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

class ChangeHistoryRepositoryImplTest() {

    private lateinit var projectDataSource: ProjectDataSource
    private lateinit var changeHistoryRepository: ChangeHistoryRepository
    private val FakeChangeHistoryData = getFakeChangeHistoryData()

    @BeforeEach
    fun setup() {
        projectDataSource = mockk()
        changeHistoryRepository = ChangeHistoryRepositoryImpl(projectDataSource)
    }

    @Test
    fun `should return change history for a valid project ID`() {
        // Given
        val projectID = 1
        val expected = FakeChangeHistoryData.firstOrNull { it.id == projectID }
        every { projectDataSource.getAllProjects() } returns Result.success(FakeChangeHistoryData)

        //when
        val result = changeHistoryRepository.getHistoryByProjectId(projectID)
        //then
        assertEquals(expected, result.getOrNull())

    }
    @Test
    fun ` should return failure when project id is not exist`(){
        //given
        val  projectID= 123
        val expected = FakeChangeHistoryData.firstOrNull { it.id == projectID }
        every { projectDataSource.getAllProjects() } returns Result.success(FakeChangeHistoryData)

        //when
        val result = changeHistoryRepository.getHistoryByProjectId(projectID)

        //then
        assertEquals(expected,result.getOrNull())
    }

    @Test
    fun `should return failure exception when data source faile`(){

        //given
        val projectID =1
        val exception =NoSuchElementException()
        every { projectDataSource.getAllProjects() } returns Result.failure(exception)

        //when
        val result =changeHistoryRepository.getHistoryByProjectId(projectID)

        //then
        assertEquals(exception,result.exceptionOrNull())

    }

    //helper
    fun getFakeChangeHistoryData(): List<Project> {

        return listOf(
            Project(
                id = 1,
                name = "Project One",
                description = "First test project",
                changeHistory = listOf(
                    ChangeHistory("1", "T1", "U1", Date(), "Created project"),
                    ChangeHistory("1", "T2", "U1", Date(), "Edited project")
                ),
                state = org.example.logic.entity.State("1", "InProgress")
            ),
            Project(
                id = 2,
                name = "Project Two",
                description = "Second test project",
                changeHistory = listOf(
                    ChangeHistory("2", "T3", "U2", Date(), "Created second project")
                ),
                state = org.example.logic.entity.State("2", "Completed")
            ),
            Project(
                id = 3,
                name = "Project Three",
                description = "Third test project",
                changeHistory = emptyList(),
                state = org.example.logic.entity.State("3", "NotStarted")
            )
        )
    }
}
