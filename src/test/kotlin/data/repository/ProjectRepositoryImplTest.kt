package data.repository

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.data.datasource.project.ProjectDataSource
import org.example.data.repository.ProjectRepositoryImpl
import org.example.logic.entity.Project
import org.example.logic.entity.ProjectState
import org.example.logic.request.ProjectCreationRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class ProjectRepositoryImplTest {
    private lateinit var repository: ProjectRepositoryImpl
    private lateinit var projectDataSource: ProjectDataSource
    private val projectId1 = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
    private val projectId2 = UUID.fromString("123e4567-e89b-12d3-a456-426614174001")
    private val notExistProjectId = UUID.fromString("123e4567-e89b-12d3-a456-426614174002")
    private val project = Project(
        projectId = projectId1,
        name = "test",
        description = "desc",
        state = ProjectState(projectId = projectId1, stateName = "Active")
    )

    @BeforeEach
    fun setup() = runTest {
        projectDataSource = mockk()
        coEvery { projectDataSource.getAllProjects() } returns emptyList()
        coEvery { projectDataSource.saveAllProjects() } returns Unit
        repository = ProjectRepositoryImpl(projectDataSource)
    }

    @Test
    fun `create project calls data source and returns project ID`() = runTest {
        coEvery { projectDataSource.createProject(ProjectCreationRequest(project)) } returns projectId1

        val result = repository.createProject(ProjectCreationRequest(project))

        assertEquals(projectId1, result)
        coVerify { projectDataSource.createProject(ProjectCreationRequest(project)) }
    }

    @Test
    fun `get project by id returns correct project`() = runTest {
        coEvery { projectDataSource.getProjectById(projectId1) } returns project

        val result = repository.getProjectById(projectId1)

        assertEquals(project, result)
        coVerify { projectDataSource.getProjectById(projectId1) }
    }

    @Test
    fun `get project by invalid id throws exception`() = runTest {
        coEvery { projectDataSource.getProjectById(notExistProjectId) } throws NoSuchElementException("Project with id $notExistProjectId not found")

        val exception = assertThrows<NoSuchElementException> {
            repository.getProjectById(notExistProjectId)
        }
        assertEquals("Project with id $notExistProjectId not found", exception.message)
        coVerify { projectDataSource.getProjectById(notExistProjectId) }
    }

    @Test
    fun `get all projects returns list from data source`() = runTest {
        val project2 = project.copy(
            projectId = projectId2,
            name = "test2",
            state = ProjectState(projectId = projectId2, stateName = "Active")
        )
        val projects = listOf(project, project2)
        coEvery { projectDataSource.getAllProjects() } returns projects

        val result = repository.getAllProjects()

        assertEquals(projects, result)
        assertEquals(2, result.size)
        assertEquals("test", result[0].name)
        assertEquals("test2", result[1].name)
        coVerify { projectDataSource.getAllProjects() }
    }

    @Test
    fun `get all projects returns empty list when no projects exist`() = runTest {
        coEvery { projectDataSource.getAllProjects() } returns emptyList()

        val result = repository.getAllProjects()

        assertEquals(emptyList<Project>(), result)
        coVerify { projectDataSource.getAllProjects() }
    }

    @Test
    fun `edit project calls data source with updated project`() = runTest {
        val updatedProject = project.copy(
            name = "updated",
            description = "new desc",
            state = ProjectState(projectId = projectId1, stateName = "InProgress")
        )
        coEvery { projectDataSource.editProject(updatedProject) } returns Unit

        repository.editProject(updatedProject)

        coVerify { projectDataSource.editProject(updatedProject) }
    }

    @Test
    fun `edit project with invalid id throws exception`() = runTest {
        val invalidProject = project.copy(projectId = notExistProjectId)
        coEvery { projectDataSource.editProject(invalidProject) } throws NoSuchElementException("Project with id $notExistProjectId not found")

        val exception = assertThrows<NoSuchElementException> {
            repository.editProject(invalidProject)
        }
        assertEquals("Project with id $notExistProjectId not found", exception.message)
        coVerify { projectDataSource.editProject(invalidProject) }
    }

    @Test
    fun `save all projects calls data source`() = runTest {
        coEvery { projectDataSource.saveAllProjects() } returns Unit

        repository.saveAllProjects()

        coVerify { projectDataSource.saveAllProjects() }
    }

    @Test
    fun `delete project calls data source with correct id`() = runTest {
        coEvery { projectDataSource.deleteProject(projectId1) } returns Unit

        repository.deleteProject(projectId1)

        coVerify { projectDataSource.deleteProject(projectId1) }
    }

    @Test
    fun `delete project by invalid id throws exception`() = runTest {
        coEvery { projectDataSource.deleteProject(notExistProjectId) } throws NoSuchElementException("Project with id $notExistProjectId not found")

        val exception = assertThrows<NoSuchElementException> {
            repository.deleteProject(notExistProjectId)
        }
        assertEquals("Project with id $notExistProjectId not found", exception.message)
        coVerify { projectDataSource.deleteProject(notExistProjectId) }
    }
}