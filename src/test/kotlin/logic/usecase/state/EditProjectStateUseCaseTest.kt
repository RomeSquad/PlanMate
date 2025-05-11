package logic.usecase.state

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.example.logic.repository.ProjectStateRepository
import org.example.logic.usecase.state.EditProjectStateUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class EditProjectProjectStateUseCaseTest {
    private lateinit var projectStateRepository: ProjectStateRepository
    private lateinit var editProjectStateUseCase: EditProjectStateUseCase

    @BeforeEach
    fun setup() {
        projectStateRepository = mockk(relaxed = true)
        editProjectStateUseCase = EditProjectStateUseCase(projectStateRepository)
    }


    @Test
    fun ` should throw exception when new state name is blank`() = runTest {
        assertThrows<IllegalArgumentException> {
            editProjectStateUseCase.execute(1, "")
        }
    }

    @Test
    fun ` should edit state successfully`() = runTest {
        val projectId = 1
        val newStateName = "code review"
        coEvery { projectStateRepository.editProjectState(projectId, newStateName) } just Runs

        editProjectStateUseCase.execute(1, "code review")

        coVerify(exactly = 1) { editProjectStateUseCase.execute(projectId, newStateName) }
    }

}