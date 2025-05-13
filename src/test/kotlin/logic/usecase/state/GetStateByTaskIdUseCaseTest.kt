package logic.usecase.state

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.entity.ProjectState
import org.example.logic.repository.ProjectStateRepository
import org.example.logic.usecase.state.GetStateByTaskIdUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class GetProjectStateByTaskIdUseCaseTest {
    private lateinit var projectStateRepository: ProjectStateRepository
    private lateinit var getStateByTaskIdUseCase: GetStateByTaskIdUseCase

    private val projectId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
    private val taskId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")

    @BeforeEach
    fun setup() {
        projectStateRepository = mockk(relaxed = true)
        getStateByTaskIdUseCase = GetStateByTaskIdUseCase(projectStateRepository)
    }

    @Test
    fun `should return state name of the task`() = runTest {
        val mockState = ProjectState(projectId = projectId, stateName = "todo")
        val taskId = taskId
        coEvery { projectStateRepository.getProjectStateByTaskId(taskId) } returns mockState

        val result = getStateByTaskIdUseCase.execute(taskId)

        assertEquals("todo", result.stateName)
    }

//    @Test
//    fun ` should throw exception when task id equal to zero`() = runTest {
//        assertThrows<IllegalArgumentException> {
//            getStateByTaskIdUseCase.execute(0)
//        }
//    }

}