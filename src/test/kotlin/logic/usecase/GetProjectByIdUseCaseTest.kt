package logic.usecase

import io.mockk.every
import io.mockk.mockk
import org.example.logic.entity.Project
import org.example.logic.entity.State
import org.example.logic.repository.ProjectRepository
import org.example.logic.usecase.project.GetProjectByIdUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetProjectByIdUseCaseTest{
    private lateinit var projectRepository: ProjectRepository
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase
    private val testProject = Project(1, "test", "test description", State("12", "in progress"))
    @BeforeEach
    fun setup() {
        projectRepository = mockk()
        getProjectByIdUseCase = GetProjectByIdUseCase(projectRepository)
    }

    @Test
    fun `when request specific project by id then return valid project`() {
        every { projectRepository.getProjectById(1) } returns (Result.success(testProject))
        val projectResponse = getProjectByIdUseCase.getProjectById(1)
        assertEquals(projectResponse.getOrNull(), testProject)
    }
    @Test
    fun `when request specific project by invalid id then return result failure with exception`() {
        every { projectRepository.getProjectById(2) } returns (Result.failure(Exception("Project not found")))
        val projectResponse = getProjectByIdUseCase.getProjectById(2)
        assertThrows<Exception> {
            projectResponse.getOrThrow()
        }
    }

}