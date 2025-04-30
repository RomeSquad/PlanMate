package data.repository

import io.mockk.every
import io.mockk.mockk
import org.example.data.datasource.project.ProjectDataSource
import org.example.data.repository.ProjectRepositoryImpl
import org.example.logic.entity.CreateProjectRequest
import org.example.logic.entity.CreateProjectResponse
import org.junit.jupiter.api.Assertions.*
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
        projectRepository = ProjectRepositoryImpl(projectDataSource)
    }

    @Test
    fun `when insert valid project request then return valid project response`() {
        val projectRequest = testProjectRequest
        every { projectDataSource.insertProject(projectRequest) } returns (testProjectResponse)
        val projectResponse = projectRepository.insertProject(projectRequest)
        assertEquals(projectResponse.id, testProjectResponse.id)
    }
    @Test
    fun `when insert empty project name then throw IllegalArgumentException`() {
        val projectRequest = testProjectRequest.copy(name = "")
        every { projectDataSource.insertProject(projectRequest) } throws (IllegalArgumentException("Project name cannot be empty"))
        assertThrows(IllegalArgumentException::class.java) {
            projectRepository.insertProject(projectRequest)
        }
    }
    @Test
    fun `when insert empty project description then throw IllegalArgumentException`() {
        val projectRequest = testProjectRequest.copy(description = "")
        every { projectDataSource.insertProject(projectRequest) } throws (IllegalArgumentException("Project description cannot be empty"))
        assertThrows(IllegalArgumentException::class.java) {
            projectRepository.insertProject(projectRequest)
        }
    }
    @Test
    fun `when insert empty user name then throw IllegalArgumentException`() {
        val projectRequest = testProjectRequest.copy(userName = "")
        every { projectDataSource.insertProject(projectRequest) } throws (IllegalArgumentException("User name cannot be empty"))
        assertThrows(IllegalArgumentException::class.java) {
            projectRepository.insertProject(projectRequest)
        }
    }
    @Test
    fun `when insert negative user id then throw IllegalArgumentException`() {
        val projectRequest = testProjectRequest.copy(userId = -1)
        every { projectDataSource.insertProject(projectRequest) } throws (IllegalArgumentException("User id cannot be negative"))
        assertThrows(IllegalArgumentException::class.java) {
            projectRepository.insertProject(projectRequest)
        }
    }
    @Test
    fun `when insert invalid user id then throw IllegalArgumentException`() {
        val projectRequest = testProjectRequest.copy(userId = 0)
        every { projectDataSource.insertProject(projectRequest) } throws (IllegalArgumentException("User id cannot be zero"))
        assertThrows(IllegalArgumentException::class.java) {
            projectRepository.insertProject(projectRequest)
        }
    }
    @Test
    fun `when insert valid user id that not exist then throw NoSuchElementException`() {
        val projectRequest = testProjectRequest.copy(userId = 2)
        every { projectDataSource.insertProject(projectRequest) } throws (NoSuchElementException("User not found"))
        assertThrows(NoSuchElementException::class.java) {
            projectRepository.insertProject(projectRequest)
        }
    }


}