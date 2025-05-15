package data.datasource.projectState


import kotlinx.coroutines.test.runTest
import org.example.logic.entity.ProjectState
import org.example.logic.request.ProjectStateEditRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import java.io.File
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class CsvProjectStateDataSourceTest {

    private lateinit var csvProjectStateDataSource: CsvProjectStateDataSource
    private val projectStateFile = File("stateTest.csv")


    @BeforeEach
    fun setup() {
        csvProjectStateDataSource = CsvProjectStateDataSource()

        val field = CsvProjectStateDataSource::class.java.getDeclaredField("csvFile")
        field.isAccessible = true
        field.set(csvProjectStateDataSource, projectStateFile)
    }

    @AfterTest
    fun deleteFile() {
        if (projectStateFile.exists()) projectStateFile.delete()
    }

    @Test
    fun `should getAllProjectStates returns empty when file does not exist`() = runTest {
        if (projectStateFile.exists()) projectStateFile.delete()

        val result = csvProjectStateDataSource.getAllProjectStates()

        assertTrue(result.isEmpty())
    }


    @Test
    fun `should getAllProjectStates returns empty when file is empty`() = runTest {
        val result = csvProjectStateDataSource.getAllProjectStates()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `should getAllProjectStates skips blank lines`() = runTest {
        projectStateFile.writeText("\n  \n")

        val result = csvProjectStateDataSource.getAllProjectStates()
        assertEquals(0, result.size)
    }


    @Test
    fun `should addProjectState writes to file`() = runTest {
        val state = ProjectState(UUID.randomUUID(), "pending")
        csvProjectStateDataSource.addProjectState(state)

        val lines = projectStateFile.readLines()
        assertEquals(1, lines.size)
        assertTrue(lines[0].contains(state.stateName))
    }

    @Test
    fun `addProjectState count logic fully evaluated`() = runTest {
        val id1 = UUID.randomUUID()
        val id2 = UUID.randomUUID()

        val state1 = ProjectState(id1, "State1")
        val state2 = ProjectState(id2, "State1")

        csvProjectStateDataSource.addProjectState(state1)
        csvProjectStateDataSource.addProjectState(state2)

        val duplicate = ProjectState(id1, "State1")

        val ex = assertFailsWith<Exception> {
            csvProjectStateDataSource.addProjectState(duplicate)
        }

        assertEquals("this state is already exist!", ex.message)
    }


    @Test
    fun `should editProjectState updates existing state`() = runTest {
        val id = UUID.randomUUID()
        val state = ProjectState(id, "Pending")
        csvProjectStateDataSource.addProjectState(state)

        csvProjectStateDataSource.editProjectState(ProjectStateEditRequest(id, "done"))

        val result = csvProjectStateDataSource.getStateById(id)
        assertEquals("done", result.stateName)
    }

    @Test
    fun `should editProjectState does nothing if projectId not found`() = runTest {
        val unrelatedState = ProjectState(UUID.randomUUID(), "state1")
        csvProjectStateDataSource.addProjectState(unrelatedState)

        csvProjectStateDataSource.editProjectState(ProjectStateEditRequest(UUID.randomUUID(), "state2"))

        val all = csvProjectStateDataSource.getAllProjectStates()
        assertEquals(1, all.size)
        assertEquals("state1", all.first().stateName)
    }


    @Test
    fun `should deleteProjectState removes the state`() = runTest {
        val id = UUID.randomUUID()
        val state = ProjectState(id, "Archived")
        csvProjectStateDataSource.addProjectState(state)

        val deleted = csvProjectStateDataSource.deleteProjectState(id)
        assertTrue(deleted)

        val allStates = csvProjectStateDataSource.getAllProjectStates()
        assertTrue(allStates.none { it.projectId == id })
    }

    @Test
    fun `should getStateById returns correct state`() = runTest {
        val state1 = ProjectState(UUID.randomUUID(), "Design")
        val state2 = ProjectState(UUID.randomUUID(), "Review")

        csvProjectStateDataSource.addProjectState(state1)
        csvProjectStateDataSource.addProjectState(state2)

        val result = csvProjectStateDataSource.getStateById(state2.projectId)
        assertEquals("Review", result.stateName)
    }


}


