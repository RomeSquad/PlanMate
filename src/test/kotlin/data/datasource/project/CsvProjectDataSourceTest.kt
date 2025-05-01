package data.datasource.project

import junit.framework.TestCase.assertNull
import org.example.logic.entity.Project
import org.example.data.datasource.project.CsvProjectDataSource
import org.junit.jupiter.api.*
import java.io.File
import org.example.logic.entity.State
import kotlin.test.assertEquals


class CsvProjectDataSourceTest {

    private lateinit var file: File
    private lateinit var dataSource: CsvProjectDataSource

    @BeforeEach
    fun setup() {
        file = File("src/test/resources/test_projects.csv")
        file.writeText("") // Clear content before each test
        dataSource = CsvProjectDataSource(file)
    }


        @Test
    fun `insert project should add new project`() {
        val project = Project(id = 1, name = "New", description = "Test", changeHistory = listOf(), state = State())
        dataSource.insertProject(project)
        val result = dataSource.getProjectById(1)
        assertEquals("New", result?.name)
    }

    @Test
    fun `insert project should overwrite project with same id`() {
        val first = Project(id = 1, name = "Old", description = "Old desc", changeHistory = listOf(), state = State())
        val second = Project(id = 1, name = "New", description = "New desc", changeHistory = listOf(), state = State())
        dataSource.insertProject(first)
        dataSource.insertProject(second)
        val result = dataSource.getProjectById(1)
        assertEquals("New", result?.name)
        assertEquals("New desc", result?.description)
    }

    @Test
    fun `insert project with blank id should do nothing`() {
        val project =
            Project(id = 0, name = "Invalid", description = "Ignored", changeHistory = listOf(), state = State())
        dataSource.insertProject(project)
        val result = dataSource.getProjectById(0)
        assertNull(result)
    }
    @Test
    fun `insert multiple projects should store all correctly`() {
        val p1 = Project(id = 1, name = "One", description = "First", changeHistory = listOf(), state = State())
        val p2 = Project(id = 2, name = "Two", description = "Second", changeHistory = listOf(), state = State())
        dataSource.insertProject(p1)
        dataSource.insertProject(p2)
        assertEquals("One", dataSource.getProjectById(1)?.name)
        assertEquals("Two", dataSource.getProjectById(2)?.name)
    }
}

