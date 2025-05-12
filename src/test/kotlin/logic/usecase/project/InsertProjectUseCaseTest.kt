//package logic.usecase.project
//
//import io.mockk.coEvery
//import io.mockk.mockk
//import kotlinx.coroutines.test.runTest
//import org.example.logic.entity.CreateProjectRequest
//import org.example.logic.entity.CreateProjectResponse
//import org.example.logic.repository.ProjectRepository
//import org.example.logic.usecase.project.InsertProjectUseCase
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.assertThrows
//import java.util.UUID
//
//class InsertProjectUseCaseTest {
//
//    private lateinit var insertProjectUseCase: InsertProjectUseCase
//    private lateinit var projectRepository: ProjectRepository
//
//    private val userId = UUID.randomUUID()
//
//    private val validRequest = CreateProjectRequest(
//        name = "Test Project",
//        userId = userId,
//        userName = "Mohamed",
//        description = "Test Description"
//    )
//
//    private val emptyNameRequest = CreateProjectRequest(
//        name = "",
//        userId = userId,
//        userName = "Mohamed",
//        description = "Test Description"
//    )
//
//    private val whitespaceNameRequest = CreateProjectRequest(
//        name = "   ",
//        userId = userId,
//        userName = "Mohamed",
//        description = "Test Description"
//    )
//
//    @BeforeEach
//    fun setup() {
//        projectRepository = mockk()
//        insertProjectUseCase = InsertProjectUseCase(projectRepository)
//    }
//
//    @Test
//    fun `insert project with valid request returns response`() = runTest {
//        // Given
//        val response = CreateProjectResponse(id = userId)
//        coEvery { projectRepository.insertProject(validRequest) } returns response
//
//        // When
//        val result = insertProjectUseCase.insertProject(validRequest)
//
//        // Then
//        assertEquals(response, result)
//        assertEquals(userId, result.id)
//    }
//
//    @Test
//    fun `insert project with empty name throws IllegalArgumentException`() = runTest {
//        // When/Then
//        val exception = assertThrows<IllegalArgumentException> {
//            insertProjectUseCase.insertProject(emptyNameRequest)
//        }
//        assertEquals("Project name cannot be blank", exception.message)
//    }
//
//    @Test
//    fun `insert project with whitespace name throws IllegalArgumentException`() = runTest {
//        // When/Then
//        val exception = assertThrows<IllegalArgumentException> {
//            insertProjectUseCase.insertProject(whitespaceNameRequest)
//        }
//        assertEquals("Project name cannot be blank", exception.message)
//    }
//
//    @Test
//    fun `insert project throws exception when repository fails`() = runTest {
//        // Given
//        val exception = RuntimeException("Failed to insert project")
//        coEvery { projectRepository.insertProject(validRequest) } throws exception
//
//        // When/Then
//        val thrownException = assertThrows<RuntimeException> {
//            insertProjectUseCase.insertProject(validRequest)
//        }
//        assertEquals("Failed to insert project", thrownException.message)
//    }
//}