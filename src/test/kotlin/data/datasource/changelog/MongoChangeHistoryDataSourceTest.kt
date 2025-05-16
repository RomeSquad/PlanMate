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
        val history = fakeChangeHistoryData()

        coEvery { mongoCollection.insertOne(history) } returns mockk() // لا يهم القيمة الراجعة

        val result = dataSource.addChangeHistory(history)

        assertEquals(history, result)
    }

    @Test
    fun `should return list of change history by taskId`() = runBlocking {
        val expected = listOfFakeChangeHistoryData()
        val taskId = UUID.fromString("22222222-2222-2222-2222-222222222222")

        val mockDataSource = mockk<MongoChangeHistoryDataSource>()
        coEvery { mockDataSource.getByTaskId(taskId) } returns expected

        val result = mockDataSource.getByTaskId(taskId)

        assertEquals(expected, result)
    }

    @Test
    fun `should return list of change history by projectId`() = runBlocking {
        val expected = listOfFakeChangeHistoryData()
        val projectId = UUID.fromString("11111111-1111-1111-1111-111111111111")


        val mockDataSource = mockk<MongoChangeHistoryDataSource>()
        coEvery { mockDataSource.getByProjectId(projectId) } returns expected

        val result = mockDataSource.getByProjectId(projectId)

        assertEquals(expected, result)
    }

    @Test
    fun `should return empty list if no change history found by projectId`() = runBlocking {
        val projectId = UUID.fromString("11111111-1111-1111-1111-111111111199")

        coEvery { mongoCollection.find(Filters.eq("projectID", projectId)).toList() } returns emptyList()

        val result = dataSource.getByProjectId(projectId)

        assertEquals(emptyList(), result)
    }

    @Test
    fun `should return empty list if no change history found by taskId`() = runBlocking {
        val taskId = UUID.fromString("22222222-2222-2222-2222-222222222999")

        coEvery { mongoCollection.find(Filters.eq("taskID", taskId)).toList() } returns emptyList()

        val result = dataSource.getByTaskId(taskId)

        assertEquals(emptyList(), result)
    }

    private fun listOfFakeChangeHistoryData(): List<ChangeHistory> {
        return listOf(
            ChangeHistory(
                projectID = UUID.fromString("11111111-1111-1111-1111-111111111111"),
                taskID = UUID.fromString("22222222-2222-2222-2222-222222222222"),
                authorID = UUID.fromString("33333333-3333-3333-3333-333333333333"),
                changeDate = Date(11),
                changeDescription = "  "
            ), ChangeHistory(
                projectID = UUID.fromString("11111111-1111-1111-1111-111111111111"),
                taskID = UUID.fromString("22222222-2222-2222-2222-222222222222"),
                authorID = UUID.fromString("33333333-3333-3333-3333-333333333333"),
                changeDate = Date(12),
                changeDescription = "11  "
            )
        )

    }

    private fun fakeChangeHistoryData(): ChangeHistory {
        return ChangeHistory(
            projectID = UUID.fromString("11111111-1111-1111-1111-111111111111"),
            taskID = UUID.fromString("22222222-2222-2222-2222-222222222222"),
            authorID = UUID.fromString("33333333-3333-3333-3333-333333333333"),
            changeDate = Date(11),
            changeDescription = "  "
        )
    }
}


