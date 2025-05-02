import org.example.data.datasource.state.StateDataSource
import org.example.logic.entity.State
import java.io.File

class CsvStateDataSource : StateDataSource {
    private val csvFile = File("state.csv")

    override fun getAllStates(): List<State> {
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
            projectId = stateIndex.get(0),
            stateName = stateIndex.get(1)
        )
    }

    override fun addState(state: State) {
        csvFile.appendText("${state.projectId},${state.stateName}\n")
    }

    override fun editState(id: String, newStateName: String) {
        val allStates = getAllStates().toMutableList()
        val index = allStates.indexOfFirst { it.projectId == id }

        if (index != -1) {
            val newName = "EditedName"
            allStates[index] = allStates[index].copy(stateName = newName)
            saveAllStates(allStates)
        }
    }

    override fun deleteState(id: String) {
        val updatedStates = getAllStates().filterNot { it.projectId == id }
        saveAllStates(updatedStates)
    }

    private fun saveAllStates(states: List<State>) {
        csvFile.writeText("")
        states.forEach { state ->
            csvFile.appendText("${state.projectId},${state.stateName}\n")
        }
    }
}
