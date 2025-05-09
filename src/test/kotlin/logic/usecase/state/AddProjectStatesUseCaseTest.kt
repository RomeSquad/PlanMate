package logic.usecase.state

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.example.logic.repository.ProjectStateRepository
import org.example.logic.usecase.state.AddProjectStatesUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AddProjectStatesUseCaseTest {
    private lateinit var projectStateRepository: ProjectStateRepository
    private lateinit var addProjectStatesUseCase: AddProjectStatesUseCase

    @BeforeEach
    fun setup() {
        projectStateRepository = mockk(relaxed = true)
        addProjectStatesUseCase = AddProjectStatesUseCase(projectStateRepository)
    }

    @Test
    fun ` should throw exception when state name is blank`() = runTest {
        assertThrows<IllegalArgumentException> {
            addProjectStatesUseCase.execute("", 1)
        }
    }

    @Test
    fun ` should add state to project successfully`() = runTest {
        coEvery { projectStateRepository.addProjectState(any()) } just Runs

        addProjectStatesUseCase.execute("todo", 2)

        coVerify(exactly = 1) { projectStateRepository.addProjectState(any()) }
    }

}

