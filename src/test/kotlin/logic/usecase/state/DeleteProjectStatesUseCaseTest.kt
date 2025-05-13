package logic.usecase.state

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.repository.ProjectStateRepository
import org.example.logic.usecase.state.DeleteProjectStatesUseCase
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertEquals

class DeleteProjectStatesUseCaseTest {

    private lateinit var projectStateRepository: ProjectStateRepository
    private lateinit var deleteProjectStatesUseCase: DeleteProjectStatesUseCase

    private val stateId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
    private val nonExistentStateId = UUID.fromString("a1b2c3d4-5e6f-7a8b-9c0d-1e2f3a4b5c6d")

    @BeforeEach
    fun setup() {
        projectStateRepository = mockk()
        deleteProjectStatesUseCase = DeleteProjectStatesUseCase(projectStateRepository)
    }

    @Test
    fun `execute returns true when state is deleted successfully`() = runTest {
        // Given
        coEvery { projectStateRepository.deleteProjectState(stateId) } returns true

        // When
        val result = deleteProjectStatesUseCase.execute(stateId)

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { projectStateRepository.deleteProjectState(stateId) }
    }

    @Test
    fun `execute returns false when state does not exist`() = runTest {
        // Given
        coEvery { projectStateRepository.deleteProjectState(nonExistentStateId) } returns false

        // When
        val result = deleteProjectStatesUseCase.execute(nonExistentStateId)

        // Then
        assertFalse(result)
        coVerify(exactly = 1) { projectStateRepository.deleteProjectState(nonExistentStateId) }
    }

    @Test
    fun `execute propagates exception when repository fails`() = runTest {
        // Given
        val exception = RuntimeException("Database error")
        coEvery { projectStateRepository.deleteProjectState(stateId) } throws exception

        // When/Then
        val thrownException = assertThrows<RuntimeException> {
            deleteProjectStatesUseCase.execute(stateId)
        }
        assertEquals("Database error", thrownException.message)
        coVerify(exactly = 1) { projectStateRepository.deleteProjectState(stateId) }
    }
}