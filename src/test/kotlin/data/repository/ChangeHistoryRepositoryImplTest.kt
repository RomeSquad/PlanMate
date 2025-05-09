package data.repository

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.example.data.datasource.changelog.MongoChangeHistoryDataSource
import org.example.data.repository.ChangeHistoryRepositoryImpl
import org.example.logic.entity.ChangeHistory
import org.example.logic.repository.ChangeHistoryRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

class ChangeHistoryRepositoryImplTest {

    private lateinit var changeHistoryDataSource: MongoChangeHistoryDataSource
    private lateinit var changeHistoryRepository: ChangeHistoryRepository
    private val fakeChangeHistoryData = getFakeChangeHistoryData()

    @BeforeEach
    fun setup() {
        changeHistoryDataSource = mockk()
        changeHistoryRepository = ChangeHistoryRepositoryImpl(changeHistoryDataSource)
    }

    @Test
    fun `should return change history for a valid project ID`() = runBlocking {

        //given
        val projectID = 123
        val expected = fakeChangeHistoryData.filter { it.projectID == projectID }
        coEvery { changeHistoryDataSource.getByProjectId(projectID) } returns expected

        //when
        val result = changeHistoryRepository.getHistoryByProjectID(projectID)
        //then
        assertEquals(expected, result)
    }

    @Test
    fun `should return empty list when project ID is invalid`() = runBlocking {
        //then
        val projectId = 0
        val expected = emptyList<ChangeHistory>()
        coEvery { changeHistoryDataSource.getByProjectId(projectId) } returns expected
        //when
        val result = changeHistoryRepository.getHistoryByProjectID(projectId)
        //then
        assertEquals(expected, result)
    }

    @Test
    fun `should return change history for existing task ID`() = runBlocking {
        val taskId = 1
        val expected = fakeChangeHistoryData.filter { it.taskID == taskId }
        coEvery { changeHistoryDataSource.getByTaskId(taskId) } returns expected

        val result = changeHistoryRepository.getHistoryByTaskID(taskId)

        assertEquals(expected, result)
    }

    @Test
    fun `should return empty list for invalid task ID`() = runBlocking {
        //given
        val taskId = 999
        val expected = emptyList<ChangeHistory>()

        coEvery { changeHistoryDataSource.getByTaskId(taskId) } returns expected

        //when
        val result = changeHistoryRepository.getHistoryByTaskID(taskId)

        //then
        assertEquals(expected, result)
    }

    @Test
    fun `should return empty list even if project exists but has no change history`() = runBlocking {
        val projectId = 123456
        coEvery { changeHistoryDataSource.getByProjectId(projectId) } returns emptyList()

        val result = changeHistoryRepository.getHistoryByProjectID(projectId)

        assertEquals(emptyList(), result)
    }

    @Test
    fun `should throw exception if data source fails to add change history`() = runBlocking {
        val badChange = ChangeHistory(
            projectID = 0,
            taskID = 1001,
            authorID = -1,
            changeDate = Date(),
            changeDescription = ""
        )

        coEvery { changeHistoryDataSource.addChangeHistory(badChange) } throws RuntimeException("Database error")

        try {
            changeHistoryRepository.addChangeHistory(badChange)
            assert(false)
        } catch (e: RuntimeException) {
            assertEquals("Database error", e.message)
        }
    }

    //helper
    private fun getFakeChangeHistoryData(): List<ChangeHistory> {
        val fakeDate = Date(1234)
        return listOf(
            ChangeHistory(
                projectID = 123,
                taskID = 1,
                authorID = 1,
                changeDate = fakeDate,
                changeDescription = "Changed status"
            ),
            ChangeHistory(
                projectID = 111111,
                taskID = 13,
                authorID = 1,
                changeDate = fakeDate,
                changeDescription = "something"
            )
        )
    }
}
