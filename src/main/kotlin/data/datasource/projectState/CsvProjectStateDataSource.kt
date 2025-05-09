import org.example.data.datasource.state.ProjectStateDataSource
import org.example.logic.entity.ProjectState
import java.io.File

class CsvProjectStateDataSource : ProjectStateDataSource {
    private val csvFile = File("state.csv")

    override suspend fun getAllProjectStates(): List<ProjectState> {
        val allStates = mutableListOf<ProjectState>()

        if (!csvFile.exists()) {
            println("CSV file not found")
            return allStates
        }

        csvFile.readLines().forEach { line ->
            if (line.isNotBlank()) {
                val newState = parseOneLine(line)
                allStates.add(newState)
            }
        }

        return allStates
    }

    private  fun parseOneLine(line: String): ProjectState {
        val stateIndex = line.split(",")
        return ProjectState(
            projectId = stateIndex[0].toInt(),
            stateName = stateIndex[1],
        )
    }

    override suspend fun addProjectState(state: ProjectState) {
        val result = getAllProjectStates()
            .filter { it.stateName == state.stateName && it.projectId == state.projectId }
            .count()
        return if (result != 0) {
            throw Exception("this state is already exist!")
        } else {
            csvFile.appendText("${state.projectId},${state.stateName}\n")
        }
    }

    override suspend fun editProjectState(projectId: Int, newStateName: String) {
        val allStates = getAllProjectStates().toMutableList()
        val index = allStates.indexOfFirst { it.projectId == projectId }

        if (index != -1) {
            allStates[index] = allStates[index].copy(stateName = newStateName)
            saveAllStates(allStates)
        }
    }

    override suspend fun deleteProjectState(projectId: Int) {
        val updatedStates = getAllProjectStates().filterNot { it.projectId == projectId }
        return saveAllStates(updatedStates)

    }

    override suspend fun getStateById(projectId: Int): ProjectState {
        return getAllProjectStates().first { it.projectId == projectId }

    }

    private  fun saveAllStates(states: List<ProjectState>) {
        csvFile.writeText("")
        states.forEach { state ->
            csvFile.appendText("${state.projectId},${state.stateName}\n")
        }
    }
}
