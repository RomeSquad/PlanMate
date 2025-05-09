package logic.usecase.task

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.usecase.project.EditProjectUseCase
import org.example.logic.entity.Project
import org.example.logic.entity.ProjectState
import org.example.logic.repository.ProjectRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class EditProjectUseCaseTest {

    private lateinit var projectRepository: ProjectRepository
    private lateinit var editProjectUseCase: EditProjectUseCase

    private val sampleProject = Project(
        id = 1,
        name = "Updated Project",
        description = "Updated Description",
        state = ProjectState(
            projectId = 15,
            stateName = "pending"
        )
    )

    @BeforeEach
    fun setup() {
        projectRepository = mockk(relaxed = true)
        editProjectUseCase = EditProjectUseCase(projectRepository)
    }

    @Test
    fun `when edit valid project then call repository`() = runTest {
        // When
        editProjectUseCase.execute(sampleProject)

        // Then
        coVerify { projectRepository.editProject(sampleProject) }
    }

    @Test
    fun `when edit fails then throw exception`() = runTest {
        // Given
        val error = Exception("edit failed")
        coEvery { projectRepository.editProject(sampleProject) } throws error

        // Then
        val thrown = assertFailsWith<Exception> {
            editProjectUseCase.execute(sampleProject)
        }
        assertEquals("edit failed", thrown.message)
    }
}