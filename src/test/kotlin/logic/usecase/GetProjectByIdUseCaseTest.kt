package logic.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.example.logic.entity.Project
import org.example.logic.entity.ProjectState
import org.example.logic.repository.ProjectRepository
import org.example.logic.usecase.project.GetProjectByIdUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetProjectByIdUseCaseTest {

    private lateinit var projectRepository: ProjectRepository
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase
    @BeforeEach
    fun setup() {
        projectRepository = mockk()
        getProjectByIdUseCase = GetProjectByIdUseCase(projectRepository)
    }

    @Test
    fun `when request specific project by id then return valid project`() = runBlocking {
        coEvery { projectRepository.getProjectById(1) }

        val projectResponse = getProjectByIdUseCase.getProjectById(1)

        assertEquals(testProject, projectResponse)
    }

    @Test
    fun `when request specific project by invalid id then return result failure with exception`() = runBlocking {
        coEvery { projectRepository.getProjectById(2) }

        val result = getProjectByIdUseCase.getProjectById(2)

        assertEquals(Exception::class.java) {
            result
        }
    }
}
