package logic.usecase.project

import io.mockk.every
import io.mockk.mockk
import org.example.logic.entity.CreateProjectRequest
import org.example.logic.entity.CreateProjectResponse
import org.example.logic.repository.ProjectRepository
import org.example.logic.usecase.project.InsertProjectUseCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InsertProjectUseCaseTest{
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
    fun `when insert valid project request then return valid project response with its project id`() {
        every { projectRepository.insertProject(createProjectRequest) } returns (Result.success(createProjectResponse))
        val projectResponse = insertProjectUseCase.insertProject(createProjectRequest)
        Assertions.assertEquals(projectResponse.getOrNull(), createProjectResponse)
    }
    @Test
    fun `when insert invalid project request then return result failure with exception`() {
        every { projectRepository.insertProject(createProjectRequest.copy(name = "")) } returns (Result.failure(Exception("Project name cannot be blank")))
        val projectResponse = insertProjectUseCase.insertProject(createProjectRequest.copy(name = ""))
        assertThrows<Exception> {
            projectResponse.getOrThrow()
        }
    }
}