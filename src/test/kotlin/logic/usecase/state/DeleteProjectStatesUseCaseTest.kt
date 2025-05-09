package logic.usecase.state

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.example.logic.repository.ProjectStateRepository
import org.example.logic.usecase.state.DeleteProjectStatesUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeleteProjectStatesUseCaseTest {
    private lateinit var projectStateRepository: ProjectStateRepository
    private lateinit var deleteProjectStatesUseCase: DeleteProjectStatesUseCase

    @BeforeEach
    fun setup() {
        projectStateRepository = mockk(relaxed = true)
        deleteProjectStatesUseCase = DeleteProjectStatesUseCase(projectStateRepository)
    }

    @Test
    fun ` should throw exception when state name is blank`() = runTest {
        coEvery { projectStateRepository.deleteProjectState(2) } just Runs

        deleteProjectStatesUseCase.execute(2)

        coVerify(exactly = 1) { projectStateRepository.deleteProjectState(2) }
    }
}
