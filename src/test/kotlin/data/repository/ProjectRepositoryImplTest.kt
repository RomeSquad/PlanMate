package data.repository

import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.example.data.datasource.project.ProjectDataSource
import org.example.data.repository.ProjectRepositoryImpl
import org.example.logic.entity.Project


class ProjectRepositoryImplTest {

    private lateinit var projectDataSource: ProjectDataSource
    private lateinit var repository: ProjectRepositoryImpl

    @BeforeEach
    fun setUp() {
        projectDataSource = mockk(relaxed = true)
        repository = ProjectRepositoryImpl(projectDataSource)
    }

    @Test
    fun `edit project should update and return updated project`() {
        val existing = Project(id = "1", name = "Old", description = "Old desc")
        val updated = Project(id = "1", name = "Updated", description = "Updated desc")

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