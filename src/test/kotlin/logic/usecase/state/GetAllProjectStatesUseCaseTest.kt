package logic.usecase.state

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.entity.ProjectState
import org.example.logic.repository.ProjectStateRepository
import org.example.logic.usecase.state.GetAllProjectStatesUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class GetAllProjectStatesUseCaseTest {
    private lateinit var projectStateRepository: ProjectStateRepository
    private lateinit var getAllProjectStatesUseCase: GetAllProjectStatesUseCase

    private val projectState = ProjectState(
        projectId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f"),
        stateName = "todo"
    )

    @BeforeEach
    fun setup() {
        projectStateRepository = mockk(relaxed = true)
        getAllProjectStatesUseCase = GetAllProjectStatesUseCase(projectStateRepository)
    }

    @Test
    fun `should return all states of the project from repository`() = runTest {
        val projectId1 = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
        val projectId2 = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
        val allStates = listOf(
            ProjectState(projectId = projectId1, stateName = "todo"),
            ProjectState(projectId = projectId2, stateName = "pending")
        )
        coEvery { projectStateRepository.getAllProjectStates() } returns allStates

        val result = getAllProjectStatesUseCase.execute(
            state = projectState,
        )

        assertEquals("todo", result[0].stateName)
    }

}