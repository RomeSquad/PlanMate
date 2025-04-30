package data.repository

import io.mockk.every
import io.mockk.mockk
import org.example.data.datasource.project.ProjectDataSource
import org.example.data.repository.ProjectRepositoryImpl
import org.example.logic.entity.CreateProjectRequest
import org.example.logic.entity.CreateProjectResponse
import io.mockk.*
import org.example.logic.entity.Project
import org.example.logic.entity.State
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class ProjectRepositoryImplTest{
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
        every { projectDataSource.insertProject(projectRequest) } returns (Result.success(testProjectResponse))
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
    @Test
    fun `edit project should update and return updated project`() {
        val existing = Project(id = 1, name = "Old", description = "Old desc", changeHistory = listOf(), state = State())
        val updated = Project(id = 2, name = "Updated", description = "Updated desc")

        every { projectDataSource.getProjectById("1") } returns existing andThen updated
        every { projectDataSource.editProject(updated) } just Runs

        repository.editProject(updated)
        val result = projectDataSource.getProjectById("1")

        assertEquals("Updated", result?.name)
        assertEquals("Updated desc", result?.description)
        verify { projectDataSource.editProject(updated) }
    }

    @Test
    fun `edit project with non-existent id should not crash`() {
        val nonExistent = Project(id = "999", name = "Ghost", description = "Nothing")
        every { projectDataSource.getProjectById("999") } returns null

        repository.editProject(nonExistent)

        verify(exactly = 0) { projectDataSource.editProject(any()) }
    }

    @Test
    fun `edit project with empty name should still update`() {
        val existing = Project(id = "2", name = "Old Name", description = "Old Desc")
        val updated = Project(id = "2", name = "", description = "Still valid")

        every { projectDataSource.getProjectById("2") } returns existing andThen updated
        every { projectDataSource.editProject(updated) } just Runs

        repository.editProject(updated)
        val result = projectDataSource.getProjectById("2")

        assertEquals("", result?.name)
        assertEquals("Still valid", result?.description)
        verify { projectDataSource.editProject(updated) }
    }

    @Test
    fun `edit project with same data should do nothing`() {
        val sameProject = Project(id = "3", name = "Same", description = "Same Desc")
        every { projectDataSource.getProjectById("3") } returns sameProject

        repository.editProject(sameProject)

        verify(exactly = 0) { projectDataSource.editProject(any()) }
    }

    @Test
    fun `edit project should do nothing if ID is blank`() {
        val invalid = Project(id = "", name = "X", description = "Y")

        repository.editProject(invalid)

        verify(exactly = 0) { projectDataSource.editProject(any()) }
    }

    @Test
    fun `edit project same data again should do nothing`() {
        val project = Project(id = "10", name = "No Change", description = "Same")
        every { projectDataSource.getProjectById("10") } returns project

        val sameProject = Project(id = "10", name = "No Change", description = "Same")
        repository.editProject(sameProject)

        verify(exactly = 0) { projectDataSource.editProject(any()) }
    }
}