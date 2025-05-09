package logic.usecase.state

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.entity.ProjectState
import org.example.logic.repository.ProjectStateRepository
import org.example.logic.usecase.state.GetAllProjectStatesUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetAllProjectStatesUseCaseTest {
    private lateinit var projectStateRepository: ProjectStateRepository
    private lateinit var getAllProjectStatesUseCase: GetAllProjectStatesUseCase

    @BeforeEach
    fun setup() {
        projectStateRepository = mockk(relaxed = true)
        getAllProjectStatesUseCase = GetAllProjectStatesUseCase(projectStateRepository)
    }

    @Test
    fun `should return all states of the project from repository`() = runTest {
        val allStates = listOf(
            ProjectState(projectId = 1, stateName = "todo"),
            ProjectState(projectId = 2, stateName = "pending")
        )
        coEvery { projectStateRepository.getAllProjectStates() } returns allStates

        val result = getAllProjectStatesUseCase.execute()

        assertEquals("todo", result[0].stateName)
    }

}

