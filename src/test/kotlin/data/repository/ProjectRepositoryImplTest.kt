package data.repository

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.data.datasource.project.ProjectDataSource
import org.example.data.repository.ProjectRepositoryImpl
import org.example.logic.entity.CreateProjectRequest
import org.example.logic.entity.CreateProjectResponse
import org.example.logic.entity.Project
import org.example.logic.entity.toProject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertFailsWith

class ProjectRepositoryImplTest {

    private lateinit var repository: ProjectRepositoryImpl
    private lateinit var fakeDataSource: ProjectDataSource

    private val request = CreateProjectRequest(
        userId = 1,
        userName = "Mohamed",
        name = "test",
        description = "desc"
    )

    private val project = request.toProject(1)


    @BeforeEach
    fun setup() = runTest {
        fakeDataSource = mockk(relaxed = true)
        repository = ProjectRepositoryImpl(fakeDataSource)

       }


    @Test
    fun `insert project returns valid response`() = runTest {
       coEvery { fakeDataSource.saveAllProjects(any()) } returns Unit
        val result = repository.insertProject(request)

        assertEquals(CreateProjectResponse(1), result)
    }

   @Test
    fun `get project by id returns correct project`() = runTest {
        val project = request.toProject(1)
        coEvery { fakeDataSource.getAllProjects() } returns listOf(project)
        repository = ProjectRepositoryImpl(fakeDataSource)
        val result = repository.getProjectById(2)
        assertEquals(project.id, result.id)
    }

    @Test
    fun `get all projects returns list`() = runTest {
        val result = repository.getAllProjects()
        assertEquals(emptyList<Project>(), result)
    }

  @Test
    fun `get project by invalid id throws exception`() = runTest {
        coEvery { fakeDataSource.getAllProjects() } returns listOf()
        assertFailsWith<Exception> {
            repository.getProjectById(99)
        }
    }

    @Test
    fun `save all projects returns unit`() = runTest {
        coEvery { fakeDataSource.saveAllProjects(any()) } returns Unit
        val result = repository.saveAllProjects()
        assertEquals(Unit, result)
    }
   @Test
    fun `when delete project by valid id then return success`() = runTest {
        coEvery { repository.deleteProject(0) } returns Unit

        val result =  repository.deleteProject(1)

        assertEquals(Unit, result)
    }


    @Test
    fun `when delete project by invalid id then return failure`() = runTest {
        coEvery { fakeDataSource.getAllProjects() } returns listOf(request.toProject(0))

        assertThrows<NoSuchElementException> {
            repository.deleteProject(999)
        }
    }


}