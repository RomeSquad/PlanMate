package logic.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.example.logic.entity.CreateProjectRequest
import org.example.logic.entity.CreateProjectResponse
import org.example.logic.repository.ProjectRepository
import org.example.logic.usecase.project.InsertProjectUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class InsertProjectUseCaseTest {

    private lateinit var projectRepository: ProjectRepository
    private lateinit var insertProjectUseCase: InsertProjectUseCase

    private val createProjectRequest = CreateProjectRequest("test", 5, "Mohamed", "test description")
    private val createProjectResponse = CreateProjectResponse(1)

    @BeforeEach
    fun setup() {
        projectRepository = mockk()
        insertProjectUseCase = InsertProjectUseCase(projectRepository)
    }

    @Test
    fun `when insert valid project request then return valid project response with its project id`() = runBlocking {
        coEvery { projectRepository.insertProject(createProjectRequest) }

        val projectResponse = insertProjectUseCase.insertProject(createProjectRequest)

        assertEquals(createProjectResponse, projectResponse)
    }

    @Test
    fun `when insert invalid project request then return result failure with exception`() = runBlocking {
        val invalidRequest = createProjectRequest.copy(name = "")
        coEvery { projectRepository.insertProject(invalidRequest) }

        val projectResponse = insertProjectUseCase.insertProject(invalidRequest)

      assertEquals(createProjectRequest, projectResponse)
    }
}