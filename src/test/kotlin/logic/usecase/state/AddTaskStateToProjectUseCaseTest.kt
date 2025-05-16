package logic.usecase.state

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.entity.ChangeHistory
import org.example.logic.entity.ProjectState
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.exception.CantAddStateWithNoNameException
import org.example.logic.exception.NotAccessException
import org.example.logic.repository.AuthRepository
import org.example.logic.repository.ChangeHistoryRepository
import org.example.logic.repository.ProjectStateRepository
import org.example.logic.usecase.state.AddTaskStateToProjectUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertEquals

class AddTaskStateToProjectUseCaseTest {
    private val projectStatesRepository: ProjectStateRepository = mockk()
    private val changeHistoryRepository: ChangeHistoryRepository = mockk()
    private val authRepository: AuthRepository = mockk()
    private lateinit var useCase: AddTaskStateToProjectUseCase

    private val userId = UUID.randomUUID()
    private val projectId = UUID.randomUUID()
    private val adminUser = User(
        userId = userId,
        userRole = UserRole.ADMIN,
        username = "admin user",
    )
    private val nonAdminUser = User(
        userId = UUID.randomUUID(),
        userRole = UserRole.MATE,
        username = "non-admin user",

    )
    private val validState = ProjectState(
        projectId = projectId,
        stateName = "InProgress"
    )
    private val blankState = ProjectState(
        projectId = projectId,
        stateName = ""
    )

    @BeforeEach
    fun setUp() {
        useCase = AddTaskStateToProjectUseCase(
            projectStatesRepository,
            changeHistoryRepository,
            authRepository
        )
        coEvery { projectStatesRepository.addProjectState(any()) } returns Unit
        coEvery { changeHistoryRepository.addChangeHistory(any()) } returns ChangeHistory(
            projectID = projectId,
            taskID = projectId,
            authorID = userId,
            changeDate = Date(),
            changeDescription = "create ${validState.stateName} state is created successfully"
        )
    }

    @Test
    fun `execute adds state and audit entry for admin user with valid state`() = runTest {
        coEvery { authRepository.getUserById(userId) } returns adminUser

        useCase.execute(validState, userId)

        coVerify { projectStatesRepository.addProjectState(validState) }
        coVerify {
            changeHistoryRepository.addChangeHistory(
                match {
                    it.projectID == projectId &&
                            it.taskID == projectId &&
                            it.authorID == userId &&
                            it.changeDescription == "create ${validState.stateName} state is created successfully"
                }
            )
        }
    }

    @Test
    fun `execute throws NotAccessException for non-admin user`() = runTest {
        coEvery { authRepository.getUserById(nonAdminUser.userId) } returns nonAdminUser

        assertThrows<NotAccessException> {
            useCase.execute(validState, nonAdminUser.userId)
        }.also {
            assertEquals("Only Admin can add states", it.message)
        }
        coVerify(exactly = 0) { projectStatesRepository.addProjectState(any()) }
        coVerify(exactly = 0) { changeHistoryRepository.addChangeHistory(any()) }
    }

    @Test
    fun `execute throws CantAddStateWithNoNameException for blank state name`() = runTest {
        coEvery { authRepository.getUserById(userId) } returns adminUser

        assertThrows<CantAddStateWithNoNameException> {
            useCase.execute(blankState, userId)
        }.also {
            assertEquals("state name can't be empty", it.message)
        }
        coVerify(exactly = 0) { projectStatesRepository.addProjectState(any()) }
        coVerify(exactly = 0) { changeHistoryRepository.addChangeHistory(any()) }
    }

    @Test
    fun `execute throws exception when user not found`() = runTest {
        val invalidUserId = UUID.randomUUID()
        coEvery { authRepository.getUserById(invalidUserId) } returns null

        assertThrows<NotAccessException> {
            useCase.execute(validState, invalidUserId)
        }.also {
            assertEquals("Only Admin can add states", it.message) // Null user treated as non-admin
        }
        coVerify(exactly = 0) { projectStatesRepository.addProjectState(any()) }
        coVerify(exactly = 0) { changeHistoryRepository.addChangeHistory(any()) }
    }

    @Test
    fun `createAudit generates correct audit entry`() = runTest {
        coEvery { authRepository.getUserById(userId) } returns adminUser
        val createAuditMethod = AddTaskStateToProjectUseCase::class.java
            .getDeclaredMethod("createAudit", ProjectState::class.java, User::class.java)
        createAuditMethod.isAccessible = true

        val audit = createAuditMethod.invoke(useCase, validState, adminUser) as ChangeHistory

        assertEquals(projectId, audit.projectID)
        assertEquals(projectId, audit.taskID)
        assertEquals(userId, audit.authorID)
        assertEquals("create ${validState.stateName} state is created successfully", audit.changeDescription)

        val currentTime = Date().time
        assert(currentTime - audit.changeDate.time <= 1000) { "changeDate should be recent" }
    }
}