package logic.usecase.project

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.entity.Project
import org.example.logic.entity.ProjectState
import org.example.logic.repository.ProjectRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class EditProjectUseCaseTest {

    private lateinit var editProjectUseCase: EditProjectUseCase
    private lateinit var projectRepository: ProjectRepository

    private val projectId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
    private val invalidProjectId = UUID.fromString("999e4567-e89b-12d3-a456-426614174000")

    private val updatedProject = Project(
        projectId = projectId,
        name = "Updated Project",
        description = "Updated Description",
        state = ProjectState(projectId = projectId, stateName = "InProgress")
    )

    @BeforeEach
    fun setup() {
        projectRepository = mockk()
        editProjectUseCase = EditProjectUseCase(projectRepository)
    }

    @Test
    fun `execute updates project successfully`() = runTest {
        // Given
        coEvery { projectRepository.editProject(updatedProject) } returns Unit

        // When
        val result = editProjectUseCase.execute(updatedProject)

        // Then
        assertEquals(Unit, result)
        coVerify(exactly = 2) { projectRepository.editProject(updatedProject) }
    }

    @Test
    fun `execute throws exception when project ID is invalid`() = runTest {
        // Given
        val invalidProject = updatedProject.copy(projectId = invalidProjectId)
        val exception = Exception("Project with id 99 not found")
        coEvery { projectRepository.editProject(invalidProject) } throws exception

        // When/Then
        val thrownException = assertThrows<Exception> {
            editProjectUseCase.execute(invalidProject)
        }
        assertEquals("Project with id 99 not found", thrownException.message)
        coVerify(exactly = 1) { projectRepository.editProject(invalidProject) }
    }
}