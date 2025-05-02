package data.repository

import io.mockk.every
import io.mockk.mockk
import org.example.data.datasource.project.ProjectDataSource
import org.example.data.repository.ProjectRepositoryImpl
import org.example.logic.entity.CreateProjectRequest
import org.example.logic.entity.CreateProjectResponse
import org.example.logic.entity.toProject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProjectRepositoryImplTest {
    private lateinit var projectRepository: ProjectRepositoryImpl
    private lateinit var projectDataSource: ProjectDataSource
    private val testProjectRequest = CreateProjectRequest(
        userId = 1,
        userName = "Mohamed",
        name = "test",
        description = "test description",
    )
    private val testProjectResponse = CreateProjectResponse(
        id = 1,
    )

    @BeforeEach
    fun setup() {
        projectDataSource = mockk()
        every { projectDataSource.getAllProjects() } returns Result.success(emptyList())
        projectRepository = ProjectRepositoryImpl(projectDataSource)
    }

    @Test
    fun `when insert valid project request then return valid project response`() {
        val projectResponse = projectRepository.insertProject(testProjectRequest)
        assertEquals(projectResponse.getOrNull(), testProjectResponse)
    }
    @Test
    fun `when request specific project by id then return valid project`() {
        every { projectDataSource.getAllProjects() } returns Result.success(listOf(testProjectRequest.toProject(0)))
        val projectResponse = projectRepository.getProjectById(1)
        assertEquals(projectResponse.getOrNull()?.id, testProjectRequest.toProject(0).id)
    }
    @Test
    fun `when request all projects then return valid projects`() {
        val projectResponse = projectRepository.getAllProjects()
        assertEquals(projectResponse.getOrNull(), emptyList<CreateProjectRequest>())
    }
    @Test
    fun `when request specific project by invalid id then return exception`() {
        assertThrows(Exception::class.java) {
            projectRepository.getProjectById(2).getOrThrow()
        }
    }

}