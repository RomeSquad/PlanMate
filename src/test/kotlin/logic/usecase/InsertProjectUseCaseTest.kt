package logic.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
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
        projectRepository = mockk(relaxed = true)
        insertProjectUseCase = InsertProjectUseCase(projectRepository)
    }

    @Test
    fun `when insert valid project request then return valid project response with its project id`() = runTest {
        coEvery { projectRepository.insertProject(createProjectRequest) } returns createProjectResponse

        val projectResponse = insertProjectUseCase.insertProject(createProjectRequest)

        assertEquals(createProjectResponse, projectResponse)
    }

   /*/ @Test
    fun `when insert invalid project request then return result failure with exception`() = runTest {
        val invalidRequest = createProjectRequest.copy(name = "")
        coEvery { projectRepository.insertProject(invalidRequest) } returns createProjectResponse

        val projectResponse = insertProjectUseCase.insertProject(invalidRequest)

      assertEquals("Project name cannot be blank", projectResponse)
      val projectRequest= CreateProjectRequest(name = "", 5, "Mohamed", "test description")
    }*/
}
