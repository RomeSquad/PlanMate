package logic.usecase.project

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.entity.Project
import org.example.logic.entity.ProjectState
import org.example.logic.repository.ProjectRepository
import org.example.logic.usecase.project.GetProjectByIdUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class GetProjectByIdUseCaseTest {

    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase
    private lateinit var projectRepository: ProjectRepository

    private val projectId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")


    private val project = Project(
        projectId = projectId,
        name = "Test Project",
        description = "Test Description",
        state = ProjectState(projectId = projectId, stateName = "Active")
    )

    @BeforeEach
    fun setup() {
        projectRepository = mockk()
        getProjectByIdUseCase = GetProjectByIdUseCase(projectRepository)
    }

    @Test
    fun `get project by valid id returns project`() = runTest {
        // Given
        coEvery { projectRepository.getProjectById(projectId) } returns project

        // When
        val result = getProjectByIdUseCase.getProjectById(projectId)

        // Then
        assertEquals(project, result)
        assertEquals(projectId, result.projectId)
        assertEquals("Test Project", result.name)
        assertEquals("Active", result.state.stateName)
    }

//    @Test
//    fun `get project by zero id throws IllegalArgumentException`() = runTest {
//        // When/Then
//        val exception = assertThrows<IllegalArgumentException> {
//            getProjectByIdUseCase.getProjectById(0)
//        }
//        assertEquals("Project id must be greater than zero", exception.message)
//    }

//    @Test
//    fun `get project by negative id throws IllegalArgumentException`() = runTest {
//        // When/Then
//        val exception = assertThrows<IllegalArgumentException> {
//            getProjectByIdUseCase.getProjectById(-1)
//        }
//        assertEquals("Project id must be greater than zero", exception.message)
//    }

    @Test
    fun `get project by non-existent id throws exception`() = runTest {
        // Given
        val invalidId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001")
        val exception = Exception("Project with id $invalidId not found")
        coEvery { projectRepository.getProjectById(invalidId) } throws exception

        // When/Then
        val thrownException = assertThrows<Exception> {
            getProjectByIdUseCase.getProjectById(invalidId)
        }
        assertEquals("Project with id $invalidId not found", thrownException.message)
    }
}