//package logic.usecase.state
//
//import io.mockk.*
//import kotlinx.coroutines.test.runTest
//import org.example.logic.repository.ProjectStateRepository
//import org.example.logic.usecase.state.DeleteProjectStatesUseCase
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import java.util.UUID
//
//class DeleteProjectStatesUseCaseTest {
//    private lateinit var projectStateRepository: ProjectStateRepository
//    private lateinit var deleteProjectStatesUseCase: DeleteProjectStatesUseCase
//
//    @BeforeEach
//    fun setup() {
//        projectStateRepository = mockk(relaxed = true)
//        deleteProjectStatesUseCase = DeleteProjectStatesUseCase(projectStateRepository)
//    }
//
//    @Test
//    fun ` should throw exception when state name is blank`() = runTest {
//        val projectId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
//        coEvery { projectStateRepository.deleteProjectState(projectId) } just Runs
//
//        deleteProjectStatesUseCase.execute(projectId)
//
//        coVerify(exactly = 1) { projectStateRepository.deleteProjectState(projectId) }
//    }
//}