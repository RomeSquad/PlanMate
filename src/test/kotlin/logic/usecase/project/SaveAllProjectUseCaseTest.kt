package logic.usecase.project

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.repository.ProjectRepository
import org.example.logic.usecase.project.SaveAllProjectUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SaveAllProjectUseCaseTest {

    private lateinit var saveAllProjectUseCase: SaveAllProjectUseCase
    private lateinit var projectRepository: ProjectRepository

    @BeforeEach
    fun setup() {
        projectRepository = mockk()
        saveAllProjectUseCase = SaveAllProjectUseCase(projectRepository)
    }

    @Test
    fun `save projects calls repository and returns unit on success`() = runTest {
        // Given
        coEvery { projectRepository.saveAllProjects() } returns Unit

        // When
        val result = saveAllProjectUseCase.saveProjects()

        // Then
        assertEquals(Unit, result)
        coVerify(exactly = 1) { projectRepository.saveAllProjects() }
    }

    @Test
    fun `save projects throws exception when repository fails`() = runTest {
        // Given
        val exception = RuntimeException("Failed to save projects")
        coEvery { projectRepository.saveAllProjects() } throws exception

        // When/Then
        val thrownException = assertThrows<RuntimeException> {
            saveAllProjectUseCase.saveProjects()
        }
        assertEquals("Failed to save projects", thrownException.message)
        coVerify(exactly = 1) { projectRepository.saveAllProjects() }
    }
}