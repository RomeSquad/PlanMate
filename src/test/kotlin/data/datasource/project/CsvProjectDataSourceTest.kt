package data.datasource.project

import org.example.logic.entity.*
import org.example.data.datasource.project.CsvProjectDataSource
import org.junit.Assert.assertNotEquals
import org.junit.jupiter.api.*
import java.io.File
import kotlin.test.assertEquals


class CsvProjectDataSourceTest {

    private lateinit var file: File
    private lateinit var dataSource: CsvProjectDataSource

    @BeforeEach
    fun setup() {
        file = File("src/test/test_projects.csv")
        file.writeText("") // Clear file content before each test
        dataSource = CsvProjectDataSource(file)
    }

    @Test
    fun `insert project should add new project`() {
        val request = CreateProjectRequest(
            name = "New",
            userId = 1,
            userName = "Ali",
            description = "Test"
                   )

        dataSource.insertProject(request)
        val result = dataSource.getProjectById(1)
        assertEquals("New", result?.name)
        assertEquals("Test", result?.description)
    }

    @Test
    fun `insert project with blank id should do nothing`() {
        val request = CreateProjectRequest(
            name = "Invalid",
            userId = 0,
            userName = "Nobody",
            description = "Ignored"
        )

        val blankRequest = request.copy(name = "", description = "")
        val result = dataSource.insertProject(blankRequest)
        assert(result.isFailure)
    }

    @Test
    fun `insert project with same name and description should still get unique id`() {
        val req = CreateProjectRequest(name = "Same", userId = 1, userName = "A", description = "Test")

        val result1 = dataSource.insertProject(req)
        val result2 = dataSource.insertProject(req)

        assertNotEquals(result1.getOrNull()?.id, result2.getOrNull()?.id)
    }
    @Test
    fun `insert multiple projects should store all correctly`() {
        val req1 = CreateProjectRequest("One", 1, "Ali", "First")
        val req2 = CreateProjectRequest("Two", 2, "Sara", "Second")

        dataSource.insertProject(req1)
        dataSource.insertProject(req2)

        assertEquals("One", dataSource.getProjectById(1)?.name)
        assertEquals("Two", dataSource.getProjectById(2)?.name)
    }
}


