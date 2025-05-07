package logic.usecase

import io.mockk.every
import io.mockk.mockk
import org.example.data.repository.ProjectRepositoryImpl
import org.example.logic.usecase.project.SaveAllProjectUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SaveAllProjectUseCaseTest{

    private lateinit var projectRepository: ProjectRepositoryImpl
    private lateinit var saveAllProjectUseCase: SaveAllProjectUseCase
    @BeforeEach
    fun setup() {
        projectRepository = mockk()
        saveAllProjectUseCase = SaveAllProjectUseCase(projectRepository)
    }

    @Test
    fun `when save all projects then return success`() {
        every { projectRepository.saveAllProjects() } returns Result.success(Unit)
        val response = saveAllProjectUseCase.saveProjects()
        assertEquals(response.getOrNull(), Unit)
    }
}