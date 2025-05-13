package logic.usecase.state

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.example.logic.repository.ProjectStateRepository
import org.example.logic.usecase.state.EditProjectStateUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*


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
        val projectId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
        assertThrows<IllegalArgumentException> {
            editProjectStateUseCase.execute(projectId, "")
        }
    }

    @Test
    fun ` should edit state successfully`() = runTest {
        val projectId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
        val newStateName = "code review"
        coEvery { projectStateRepository.editProjectState(projectId, newStateName) } just Runs

        editProjectStateUseCase.execute(projectId, "code review")

        coVerify(exactly = 1) { editProjectStateUseCase.execute(projectId, newStateName) }
    }

}