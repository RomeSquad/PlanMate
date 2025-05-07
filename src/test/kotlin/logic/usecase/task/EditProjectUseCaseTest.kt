package logic.usecase.task

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import logic.usecase.project.EditProjectUseCase
import org.example.logic.entity.Project
import org.example.logic.entity.State
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
        changeHistory = emptyList(),
        state = State("1", "In progress")
    )

    @BeforeEach
    fun setup() {
        projectRepository = mockk(relaxed = true)
        editProjectUseCase = EditProjectUseCase(projectRepository)
    }

    @Test
    fun `when edit valid project then call repository`() = runBlocking {
        // When
        editProjectUseCase.execute(sampleProject)

        // Then
        coVerify { projectRepository.editProject(sampleProject) }
    }

    @Test
    fun `when edit fails then throw exception`() = runBlocking {
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