package logic.usecase

import io.mockk.every
import io.mockk.mockk
import org.example.logic.entity.ChangeHistory
import org.example.logic.entity.Project
import org.example.logic.entity.State
import org.example.logic.repository.ChangeHistoryRepository
import org.example.logic.usecase.ShowProjectHistoryUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.Date
import kotlin.test.assertEquals

class ShowProjectHistoryUseCaseTest {

    private lateinit var changeHistoryRepository: ChangeHistoryRepository
    private lateinit var useCase: ShowProjectHistoryUseCase

    @BeforeEach
    fun setup() {
        changeHistoryRepository = mockk()
        useCase = ShowProjectHistoryUseCase(changeHistoryRepository)
    }

    @Test
    fun `should return project change history when project ID exist`() {

        //given
        val Id = 12355
        every { useCase.execute(Id) } returns Result.success(fakeProjectData())

        //when
        val result = useCase.execute(Id)

        //then
        assertEquals(result.getOrNull(), fakeProjectData())
    }

    @Test
    fun `should return failure when the project id is not exist`() {

        //given
        val ID = 11111
        every { useCase.execute(ID) } returns Result.failure(NoSuchElementException("Project 11111 not found"))

        //when
        val result = useCase.execute(ID)

        //then
        assertEquals("Project 11111 not found", result.exceptionOrNull()?.message)

    }


    //helper
    private fun fakeProjectData(): Project {
        val fixedDate = Date(12355)
        return Project(
            id = 123,
            name = "ahmed",
            description = "Testing history",
            changeHistory = listOf(
                ChangeHistory("1", "T1", "U1", fixedDate, "Created project")
            ),
            state = State("1", "InProgress")
        )
    }
}