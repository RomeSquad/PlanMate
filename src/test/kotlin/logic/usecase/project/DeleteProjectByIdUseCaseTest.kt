package logic.usecase.project

import io.mockk.every
import io.mockk.mockk
import org.example.logic.entity.Project
import org.example.logic.entity.State
import org.example.logic.repository.ProjectRepository
import org.example.logic.usecase.project.DeleteProjectByIdUseCase
import org.example.logic.usecase.project.GetProjectByIdUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeleteProjectByIdUseCaseTest{
    private lateinit var projectRepository: ProjectRepository
    private lateinit var deleteProjectByIdUseCase: DeleteProjectByIdUseCase
    @BeforeEach
    fun setup() {
        projectRepository = mockk()
        deleteProjectByIdUseCase = DeleteProjectByIdUseCase(projectRepository)
    }

    @Test
    fun `when request to delete specific project by id then return success result`() {
        every { projectRepository.deleteProject(1) } returns (Result.success(Unit))
        val projectResponse = deleteProjectByIdUseCase.deleteProjectById(1)
        assertEquals(projectResponse.isSuccess, true)
    }
    @Test
    fun `when request to delete specific project by invalid id then return result failure with exception`() {
        every { projectRepository.deleteProject(2) } returns (Result.failure(NoSuchElementException("Project not found")))
        val projectResponse = deleteProjectByIdUseCase.deleteProjectById(2)
        assertThrows<NoSuchElementException> {
            projectResponse.getOrThrow()
        }
    }

}