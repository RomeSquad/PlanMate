package logic.usecase.state

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.example.logic.repository.ProjectStateRepository
import org.example.logic.usecase.state.AddProjectStatesUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

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
            addProjectStatesUseCase.execute("", UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f"))
        }
    }

    @Test
    fun ` should add state to project successfully`() = runTest {
        coEvery { projectStateRepository.addProjectState(any()) } just Runs

        addProjectStatesUseCase.execute("todo", UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f"))

        coVerify(exactly = 1) { projectStateRepository.addProjectState(any()) }
    }

}