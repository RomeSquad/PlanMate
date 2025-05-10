package logic.usecase.task


import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import logic.usecase.project.EditProjectUseCase
import org.example.logic.entity.Project
import org.example.logic.repository.ProjectRepository
import org.junit.jupiter.api.BeforeEach
import kotlin.test.assertEquals
import kotlin.test.Test
import kotlin.Result



class EditProjectUseCaseTest {
    private lateinit var projectRepository: ProjectRepository
    private lateinit var editProjectUseCase: EditProjectUseCase

    private val sampleProject = Project(
        id = 1,
        name = "Updated Project",
        description = "Updated Description",
        state = org.example.logic.entity.ProjectState(1, "In progress")
    )

    @BeforeEach
    fun setup() {
        projectRepository = mockk()
        editProjectUseCase = EditProjectUseCase(projectRepository)
    }

    @Test
    fun `when edit valid project then return success`() {
        every { projectRepository.editProject(sampleProject) } returns Result.success(Unit)

        val result: Result<Unit> = editProjectUseCase.execute(sampleProject)

        assertEquals(true, result.isSuccess)
    }


    @Test
    fun `when edit invalid project then return failure`() {
        val error = Exception("edit failed")
        every { projectRepository.editProject(sampleProject) } returns Result.failure(error)

        val result = editProjectUseCase.execute(sampleProject)

        assertTrue(result.isFailure)
        assertEquals(error, result.exceptionOrNull())
    }
}


