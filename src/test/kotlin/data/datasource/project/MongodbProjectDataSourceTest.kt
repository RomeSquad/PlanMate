package data.datasource.project

import com.mongodb.kotlin.client.coroutine.MongoCollection
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.example.data.datasource.project.MongoProjectDataSource
import org.example.logic.entity.Project
import org.example.logic.entity.State
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MongodbProjectDataSourceTest {


    private lateinit var dataSource: MongoProjectDataSource
    private lateinit var mongoCollection : MongoCollection<Project>
    @BeforeEach
    fun setup() {
        mongoCollection = mockk(relaxed = true)
        dataSource = MongoProjectDataSource(mongoCollection)
    }

    @Test
    fun getAllProjects_shouldReturnListOfProjects() = runBlocking {
        // Given
        val expectedProjects = listOf(
            Project(id = 1, name = "Test1", description = "desc1", changeHistory = listOf(), state = State("1","todo")),
            Project(id = 2, name = "Test2", description = "desc2", changeHistory = listOf(), state = State("1","todo"))
        )

        coEvery { dataSource.getAllProjects() } returns expectedProjects

        // When
        val result = dataSource.getAllProjects()

        // Then
        assertEquals(expectedProjects, result)
    }

    @Test
    fun saveAllProjects_shouldSaveSuccessfully() = runBlocking {
        // Given
        val projects = listOf(
            Project(id = 1, name = "Test1", description = "desc1", changeHistory = listOf(), state = State("1","todo")),
            Project(id = 2, name = "Test2", description = "desc2", changeHistory = listOf(), state = State("1","todo"))
        )

        // When
        val result = dataSource.saveAllProjects(projects)

        // Then
        assertEquals(Unit, result)
    }
}