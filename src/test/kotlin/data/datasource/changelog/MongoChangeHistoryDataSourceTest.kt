import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.example.data.datasource.changelog.MongoChangeHistoryDataSource
import org.example.logic.entity.ChangeHistory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

class MongoChangeHistoryDataSourceTest {

    private lateinit var dataSource: MongoChangeHistoryDataSource
    private lateinit var mongoCollection: MongoCollection<ChangeHistory>

    @BeforeEach
    fun setup() {
        mongoCollection = mockk(relaxed = true)
        dataSource = MongoChangeHistoryDataSource(mongoCollection)
    }

    @Test
    fun `should add change history successfully`() = runBlocking {
        // Given
        val history = fakeChangeHistoryData()

        // Mock insertOne to just succeed
        coEvery { mongoCollection.insertOne(history) } returns mockk() // لا يهم القيمة الراجعة

        // When
        val result = dataSource.addChangeHistory(history)

        // Then
        assertEquals(history, result)
    }

    @Test
    fun `should return list of change history by taskId`() = runBlocking {
        // given
        val expected = listOfFakeChangeHistoryData()
        val taskId = 1

        val mockDataSource = mockk<MongoChangeHistoryDataSource>()
        coEvery { mockDataSource.getByTaskId(taskId) } returns expected

        // when
        val result = mockDataSource.getByTaskId(taskId)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `should return list of change history by projectId`() = runBlocking {
        // given
        val expected = listOfFakeChangeHistoryData()
        val projectId = 1


        val mockDataSource = mockk<MongoChangeHistoryDataSource>()
        coEvery { mockDataSource.getByProjectId(projectId) } returns expected

        // when
        val result = mockDataSource.getByProjectId(projectId)

        // then
        assertEquals(expected, result)
    }
    @Test
    fun `should return empty list if no change history found by projectId`() = runBlocking {
        val projectId = 999

        coEvery { mongoCollection.find(Filters.eq("projectID", projectId)).toList() } returns emptyList()

        val result = dataSource.getByProjectId(projectId)

        assertEquals(emptyList(), result)
    }
    @Test
    fun `should return empty list if no change history found by taskId`() = runBlocking {
        val taskId = 999

        coEvery { mongoCollection.find(Filters.eq("taskID", taskId)).toList() } returns emptyList()

        val result = dataSource.getByTaskId(taskId)

        assertEquals(emptyList(), result)
    }

    private fun listOfFakeChangeHistoryData(): List<ChangeHistory> {
        return listOf(
            ChangeHistory(
                projectID = 1,
                taskID = 1,
                authorID = 1,
                changeDate = Date(11),
                changeDescription = "  "
            ), ChangeHistory(
                projectID = 1,
                taskID = 1,
                authorID = 111,
                changeDate = Date(11),
                changeDescription = "11  "
            )
        )

    }

    private fun fakeChangeHistoryData(): ChangeHistory {
        return ChangeHistory(
            projectID = 1,
            taskID = 1,
            authorID = 1,
            changeDate = Date(11),
            changeDescription = "  "
        )
    }
}


