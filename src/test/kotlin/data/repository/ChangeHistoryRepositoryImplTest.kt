package data.repository

import io.mockk.every
import io.mockk.mockk
import org.example.data.datasource.changelog.ChangeHistoryDataSource
import org.example.data.repository.ChangeHistoryRepositoryImpl
import org.example.logic.entity.ChangeHistory
import org.example.logic.repository.ChangeHistoryRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

class ChangeHistoryRepositoryImplTest() {

    private lateinit var changeHistoryDataSource: ChangeHistoryDataSource
    private lateinit var changeHistoryRepository: ChangeHistoryRepository
    private val FakeChangeHistoryData = getFakeChangeHistoryData()

    @BeforeEach
    fun setup() {
        changeHistoryDataSource = mockk()
        changeHistoryRepository = ChangeHistoryRepositoryImpl(changeHistoryDataSource)
    }

    @Test
    fun `should return change history for a valid project ID`() {
        // Given
        val projectID = 123
        val expected = FakeChangeHistoryData.filter { it.projectID == projectID }
        every { changeHistoryDataSource.getByProjectId(projectID) } returns expected

        //when
        val result = changeHistoryRepository.getHistoryByProjectID(projectID)

        //then
        assertEquals( expected, result )

    }

    @Test
    fun ` should return failure when project id is not exist`() {
        //given
        val projectID = 123
        val expected = FakeChangeHistoryData.firstOrNull { it.id == projectID }
        every { changeHistoryDataSource.getAllProjects() } returns Result.success(FakeChangeHistoryData)

        //when
        val result = changeHistoryRepository.getHistoryByTaskID(projectID)

        //then
        assertEquals(expected, result.getOrNull())
    }

    @Test
    fun `should return failure exception when data source failure`() {

        //given
        val projectID = 1
        val exception = NoSuchElementException()
        every { changeHistoryDataSource.getAllProjects() } returns Result.failure(exception)

        //when
        val result = changeHistoryRepository.getHistoryByTaskID(projectID)

        //then
        assertEquals(exception, result.exceptionOrNull())

    }

    //helper
    fun getFakeChangeHistoryData(): List<ChangeHistory> {
        val fakeDate = Date(1234)
        return listOf(
            ChangeHistory(
                projectID = 123,
                taskID = "1",
                authorID = "User",
                changeDate = fakeDate,
                changeDescription = "some thing"
            ),
            ChangeHistory(
                projectID = 111111,
                taskID = "13",
                authorID = "User0",
                changeDate = fakeDate,
                changeDescription = "some thing"
            )
        )
    }
}
