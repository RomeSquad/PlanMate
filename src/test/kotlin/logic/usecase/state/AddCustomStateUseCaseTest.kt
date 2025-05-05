package logic.usecase.state

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.example.logic.entity.State
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.repository.StateRepository
import org.example.logic.usecase.state.AddCustomStateUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertFailsWith

class AddCustomStateUseCaseTest {
    private lateinit var stateRepository: StateRepository
    private lateinit var addCustomStateUseCase: AddCustomStateUseCase

    @BeforeEach
    fun setup() {
        stateRepository = mockk(relaxed = true)
        addCustomStateUseCase = AddCustomStateUseCase(stateRepository)
    }

    @Test
    fun `should add custom state if user is admin`() {
        val currentUser = User(userId = 1, username = "Zinah", password = "1234" , userRole = UserRole.ADMIN)
        val projectId = "4"
        val stateName = "to do"

        val capturedState = slot<State>()

        every { stateRepository.addState(capture(capturedState)) } returns true

        val result = addCustomStateUseCase.executeAddCustomStateUseCase(currentUser, stateName, projectId)

        assertTrue(result)
        assertEquals(stateName, capturedState.captured.stateName)
        assertEquals(projectId, capturedState.captured.projectId)
    }

    @Test
    fun `should throw an exception when user role is mate`() {
        val currentUser = User(userId = 1, username = "Zinah", password = "1234" , userRole = UserRole.MATE)
        val stateName = "todo"
        val projectId = "1"
        assertFailsWith<IllegalAccessException> {
            addCustomStateUseCase.executeAddCustomStateUseCase(currentUser, stateName, projectId)
        }
    }

    @Test
    fun ` should throw an exception when state name is blank`(){
        val currentUser = User(userId = 1, username = "Zinah", password = "1234" , userRole = UserRole.ADMIN)
        assertThrows<IllegalArgumentException> {
            addCustomStateUseCase.executeAddCustomStateUseCase(currentUser,"","1")
        }
    }

    @Test
    fun ` should throw an exception when project id is blank`(){
        val currentUser = User(userId = 1, username = "Zinah", password = "1234" , userRole = UserRole.ADMIN)
        assertThrows<IllegalArgumentException> {
            addCustomStateUseCase.executeAddCustomStateUseCase(currentUser,"todo","")
        }
    }
}
