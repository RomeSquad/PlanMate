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
        // Given
        val updated = Project(id = "1", name = "Updated", description = "Updated desc")

        every { projectDataSource.editProject(updated) } just Runs
        every { projectDataSource.getProjectById("1") } returns updated

        // When
        repository.editProject(updated)
        val result = repository.getProjectById("1")

        // Then
        assertEquals("Updated", result?.name)
        assertEquals("Updated desc", result?.description)
    }

    @Test
    fun `edit project with non-existent id should not crash`() {
        // Given
        val nonExistent = Project(id = "999", name = "Ghost", description = "Does not exist")
        every { projectDataSource.editProject(nonExistent) } just Runs
        every { projectDataSource.getProjectById("999") } returns null

        // When
        repository.editProject(nonExistent)
        val result = repository.getProjectById("999")

        // Then
        verify { projectDataSource.editProject(nonExistent) }
        assertNull(result)
    }

    @Test
    fun `edit project with empty name should still update`() {
        // Given
        val updated = Project(id = "2", name = "", description = "No name")
        every { projectDataSource.editProject(updated) } just Runs
        every { projectDataSource.getProjectById("2") } returns updated

        // When
        repository.editProject(updated)
        val result = repository.getProjectById("2")

        // Then
        assertEquals("", result?.name)
        assertEquals("No name", result?.description)
    }

    @Test
    fun `edit project with same data should keep project unchanged`() {
        // Given
        val project = Project(id = "3", name = "Same", description = "Same Desc")
        every { projectDataSource.editProject(project) } just Runs
        every { projectDataSource.getProjectById("3") } returns project

        // When
        repository.editProject(project)
        val result = repository.getProjectById("3")

        // Then
        assertEquals("Same", result?.name)
        assertEquals("Same Desc", result?.description)
    }

    @Test
    fun `edit project should do nothing if project ID is blank`() {
        // Given
        val invalidProject = Project(id = "", name = "Something", description = "Invalid")
        every { projectDataSource.editProject(invalidProject) } just Runs
        every { projectDataSource.getProjectById("") } returns null

        // When
        repository.editProject(invalidProject)
        val result = repository.getProjectById("")

        // Then
        assertNull(result)
    }

    @Test
    fun `edit project with same data should keep it unchanged`() {
        // Given
        val project = Project(id = "10", name = "No Change", description = "Same")
        every { projectDataSource.editProject(project) } just Runs
        every { projectDataSource.getProjectById("10") } returns project

        val sameProject = Project(id = "10", name = "No Change", description = "Same")

        // When
        repository.editProject(sameProject)
        val result = repository.getProjectById("10")

        // Then
        assertEquals("No Change", result?.name)
        assertEquals("Same", result?.description)
    }

}