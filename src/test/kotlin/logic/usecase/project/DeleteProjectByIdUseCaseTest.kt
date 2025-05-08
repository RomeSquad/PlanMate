package logic.usecase.project

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk

import org.example.logic.repository.ProjectRepository
import org.example.logic.usecase.project.DeleteProjectByIdUseCase
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
    fun `when request to delete specific project by id then return success result`() = runTest {
        coEvery { projectRepository.deleteProject(1) } returns Unit
        val projectResponse = deleteProjectByIdUseCase.deleteProjectById(1)
        assertEquals(projectResponse, Unit)
    }
    @Test
    fun `when request to delete specific project by invalid id then return result failure with exception`() = runTest {
        coEvery { projectRepository.deleteProject(2) } throws NoSuchElementException("Project not found")
        val projectResponse = deleteProjectByIdUseCase.deleteProjectById(2)
        assertThrows<NoSuchElementException> {
            projectResponse
        }
    }

}