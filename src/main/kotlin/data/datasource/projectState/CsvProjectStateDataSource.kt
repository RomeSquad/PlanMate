import org.example.data.datasource.state.ProjectStateDataSource
import org.example.logic.entity.State
import java.io.File

class CsvProjectStateDataSource : ProjectStateDataSource {
    private val csvFile = File("state.csv")

    override fun getAllStatesProject(): List<State> {
        val allStates = mutableListOf<State>()

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

    private fun parseOneLine(line: String): State {
        val stateIndex = line.split(",")
        return State(
            projectId = stateIndex[0].toInt(),
            stateName = stateIndex[1],
        )
    }

    override fun addState(state: State) {
        return csvFile.appendText("${state.projectId},${state.stateName}\n")

    }

    override fun editState(projectId: Int, newStateName: String) {
        val allStates = getAllStatesProject().toMutableList()
        val index = allStates.indexOfFirst { it.projectId == projectId }

        if (index != -1) {
            allStates[index] = allStates[index].copy(stateName = newStateName)
            saveAllStates(allStates)
        }
    }

    override fun deleteState(projectId: Int) {
        val updatedStates = getAllStatesProject().filterNot { it.projectId == projectId }
        return saveAllStates(updatedStates)

    }

    override fun getStateById(projectId: Int): State {
        return getAllStatesProject().first { it.projectId == projectId }

    }

    private fun saveAllStates(states: List<State>) {
        csvFile.writeText("")
        states.forEach { state ->
            csvFile.appendText("${state.projectId},${state.stateName}\n")
        }
    }
}
