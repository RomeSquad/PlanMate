package data.repository

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.data.datasource.project.ProjectDataSource
import org.example.data.repository.ProjectRepositoryImpl
import org.example.logic.entity.Project
import org.example.logic.entity.ProjectState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ProjectRepositoryImplTest {

    private lateinit var repository: ProjectRepositoryImpl
    private lateinit var fakeDataSource: ProjectDataSource

    private val project = Project(
        id = 1,
        name = "test",
        description = "desc",
        state = ProjectState(projectId = 1, stateName = "Active"),
    )

    @BeforeEach
    fun setup() = runTest {
        fakeDataSource = mockk()
        coEvery { fakeDataSource.getAllProjects() } returns emptyList()
        coEvery { fakeDataSource.saveAllProjects(any()) } returns Unit
        repository = ProjectRepositoryImpl(fakeDataSource)
    }

    @Test
    fun `get project by id returns correct project`() = runTest {
        // Given
        coEvery { fakeDataSource.getAllProjects() } returns listOf(project)
        repository = ProjectRepositoryImpl(fakeDataSource)

        // When
        val result = repository.getProjectById(1)

        // Then
        assertEquals(project, result)
        assertEquals(1, result.id)
        assertEquals("test", result.name)
        assertEquals("Active", result.state.stateName)
    }

    @Test
    fun `get project by invalid id throws exception`() = runTest {
        // Given
        coEvery { fakeDataSource.getAllProjects() } returns emptyList()
        repository = ProjectRepositoryImpl(fakeDataSource)

        // When/Then
        val exception = assertThrows<Exception> {
            repository.getProjectById(99)
        }
        assertEquals("Project with id 99 not found", exception.message)
    }

    @Test
    fun `get all projects returns list from data source`() = runTest {
        // Given
        val project2 = project.copy(id = 2, name = "test2", state = ProjectState(projectId = 2, stateName = "Active"))
        val projects = listOf(project, project2)
        coEvery { fakeDataSource.getAllProjects() } returns projects
        repository = ProjectRepositoryImpl(fakeDataSource)

        // When
        val result = repository.getAllProjects()

        // Then
        assertEquals(projects, result)
        assertEquals(2, result.size)
        assertEquals("test", result[0].name)
        assertEquals("test2", result[1].name)
    }

    @Test
    fun `get all projects returns empty list when no projects exist`() = runTest {
        // When
        val result = repository.getAllProjects()

        // Then
        assertEquals(emptyList<Project>(), result)
    }

    @Test
    fun `edit project updates existing project`() = runTest {
        // Given
        coEvery { fakeDataSource.getAllProjects() } returns listOf(project)
        coEvery { fakeDataSource.saveAllProjects(any()) } returns Unit
        repository = ProjectRepositoryImpl(fakeDataSource)
        val updatedProject = project.copy(
            name = "updated",
            description = "new desc",
            state = ProjectState(projectId = 1, stateName = "InProgress")
        )

        // When
        repository.editProject(updatedProject)

        // Then
        val result = repository.getProjectById(1)
        assertEquals(updatedProject, result)
        assertEquals("InProgress", result.state.stateName)
        coVerify { fakeDataSource.saveAllProjects(listOf(updatedProject)) }
    }

    @Test
    fun `edit project with invalid id throws exception`() = runTest {
        // Given
        coEvery { fakeDataSource.getAllProjects() } returns emptyList()
        repository = ProjectRepositoryImpl(fakeDataSource)
        val invalidProject = project.copy(id = 99)

        // When/Then
        val exception = assertThrows<Exception> {
            repository.editProject(invalidProject)
        }
        assertEquals("Project with id 99 not found", exception.message)
    }

    @Test
    fun `save all projects calls data source`() = runTest {
        // Given
        coEvery { fakeDataSource.getAllProjects() } returns listOf(project)
        coEvery { fakeDataSource.saveAllProjects(any()) } returns Unit
        repository = ProjectRepositoryImpl(fakeDataSource)

        // When
        repository.saveAllProjects()

        // Then
        coVerify { fakeDataSource.saveAllProjects(listOf(project)) }
    }



    @Test
    fun `delete project by invalid id throws exception`() = runTest {
        // Given
        coEvery { fakeDataSource.getAllProjects() } returns emptyList()
        repository = ProjectRepositoryImpl(fakeDataSource)

        // When/Then
        val exception = assertThrows<NoSuchElementException> {
            repository.deleteProject(999)
        }
        assertEquals("Project with id 999 not found", exception.message)
    }
}