package logic.usecase.state

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.entity.ProjectState
import org.example.logic.repository.ProjectStateRepository
import org.example.logic.usecase.state.AddProjectStatesUseCase
import org.example.logic.usecase.state.DefaultProjectStateUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultProjectProjectStateUseCaseTest {
    private lateinit var projectStateRepository: ProjectStateRepository
    private lateinit var addProjectStatesUseCase: AddProjectStatesUseCase
    private lateinit var defaultProjectStateUseCase: DefaultProjectStateUseCase

    @BeforeEach
    fun setup() {
        projectStateRepository = mockk(relaxed = true)
        addProjectStatesUseCase = AddProjectStatesUseCase(projectStateRepository)
        defaultProjectStateUseCase = DefaultProjectStateUseCase(projectStateRepository)
    }

    @Test
    fun `should initialize default states for a new project`() = runTest {
        val getStates = mutableListOf<ProjectState>()

        coEvery { projectStateRepository.addProjectState(capture(getStates)) } just Runs

        defaultProjectStateUseCase.initializeProjectState(2)
        assertTrue(getStates.any { it.stateName == "todo" || it.stateName == "in progress" || it.stateName == "done" })
    }

    @Test
    fun `should have 3 default state for a new project`() = runTest {
        val getStates = mutableListOf<ProjectState>()
        coEvery { projectStateRepository.addProjectState(capture(getStates)) } just Runs

        defaultProjectStateUseCase.initializeProjectState(3)

        assertEquals(3, getStates.size)
    }

}