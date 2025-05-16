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
    private val fakeChangeHistoryData = getDummyChangHistoryData()

    @BeforeEach
    fun setup() {
        changeHistoryDataSource = mockk()
        changeHistoryRepository = ChangeHistoryRepositoryImpl(changeHistoryDataSource)
    }

    @Test
    fun `should return change history for a valid project ID`() = runBlocking {

        //given
        val projectID = UUID.fromString("11111111-1111-1111-1111-111111111111")
        val expected = fakeChangeHistoryData.filter { it.projectID == projectID }
        coEvery { changeHistoryDataSource.getByProjectId(projectID) } returns expected

        //when
        val result = changeHistoryRepository.getHistoryByProjectID(projectID)

        //then
        assertEquals(expected, result)
    }

    @Test
    fun `should return empty list when project ID is invalid`() = runBlocking {

        //given
        val projectId = UUID.fromString("33333333-3333-3333-3333-333333333333")
        val expected = emptyList<ChangeHistory>()

        coEvery { changeHistoryDataSource.getByProjectId(projectId) } returns expected

        //when
        val result = changeHistoryRepository.getHistoryByProjectID(projectId)

        //then
        assertEquals(expected, result)
    }

    @Test
    fun `should return change history for existing task ID`() = runBlocking {
        //given
        val taskId = UUID.fromString("22222222-2222-2222-2222-222222222222")
        val expected = fakeChangeHistoryData.filter { it.taskID == taskId }
        coEvery { changeHistoryDataSource.getByTaskId(taskId) } returns expected

        //when
        val result = changeHistoryRepository.getHistoryByTaskID(taskId)

        //then
        assertEquals(expected, result)
    }

    @Test
    fun `should return empty list for invalid task ID`() = runBlocking {

        //given
        val taskId = UUID.fromString("33333333-3333-3333-3333-333333333333")
        val expected = emptyList<ChangeHistory>()

        coEvery { changeHistoryDataSource.getByTaskId(taskId) } returns expected

        //when
        val result = changeHistoryRepository.getHistoryByTaskID(taskId)

        //then
        assertEquals(expected, result)
    }

    @Test
    fun `should return empty list even if project exists but has no change history`() = runBlocking {

        //given
        val projectId = UUID.fromString("33333333-3333-3333-3333-333333333333")
        coEvery { changeHistoryDataSource.getByProjectId(projectId) } returns emptyList()

        //when
        val result = changeHistoryRepository.getHistoryByProjectID(projectId)

        //then
        assertEquals(emptyList(), result)
    }

    @Test
    fun `should throw exception if data source fails to add change history`() = runBlocking {

        //given
        coEvery { changeHistoryDataSource.addChangeHistory(badChange) } throws RuntimeException("Database error")

        //when && then
        try {
            changeHistoryRepository.addChangeHistory(badChange)
            assert(false)
        } catch (e: RuntimeException) {
            assertEquals("Database error", e.message)
        }
    }

    //helper
    private fun getDummyChangHistoryData(): List<ChangeHistory> {

        val fakeDate = Date(1234)
        return listOf(
            ChangeHistory(
                projectID = UUID.fromString("11111111-1111-1111-1111-111111111111"),
                taskID = UUID.fromString("22222222-2222-2222-2222-222222222222"),
                authorID = UUID.fromString("33333333-3333-3333-3333-333333333333"),
                changeDate = fakeDate,
                changeDescription = "Changed status"
            ),
            ChangeHistory(
                projectID = UUID.fromString("44444444-4444-4444-4444-444444444444"),
                taskID = UUID.fromString("55555555-5555-5555-5555-555555555555"),
                authorID = UUID.fromString("33333333-3333-3333-3333-333333333333"),
                changeDate = fakeDate,
                changeDescription = "something"
            )
        )
    }
    val badChange = ChangeHistory(
        projectID = UUID.fromString("11111111-1111-1111-1111-111111111111"),
        taskID = UUID.fromString("22222222-2222-2222-2222-222222222222"),
        authorID = UUID.fromString("33333333-3333-3333-3333-333333333333"),
        changeDate = Date(),
        changeDescription = ""
    )
}
