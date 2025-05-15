package data.datasource.projectState

import org.example.logic.entity.ProjectState
import org.example.logic.request.ProjectStateEditRequest
import java.io.File
import java.util.*

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

    private fun parseOneLine(line: String): ProjectState {
        val stateIndex = line.split(",")
        return ProjectState(
            projectId = UUID.fromString(stateIndex[0]),
            stateName = stateIndex[1],
        )
    }

    override suspend fun addProjectState(state: ProjectState) {
        val result = getAllProjectStates().count { it.stateName == state.stateName && it.projectId == state.projectId }
        return if (result != 0) {
            throw Exception("this state is already exist!")
        } else {
            csvFile.appendText("${state.projectId},${state.stateName}\n")
        }
    }

    override suspend fun editProjectState(request : ProjectStateEditRequest) {
        val allStates = getAllProjectStates().toMutableList()
        val index = allStates.indexOfFirst { it.projectId == request.projectId }

        if (index != -1) {
            allStates[index] = allStates[index].copy(stateName = request.newStateName)
            saveAllStates(allStates)
        }
    }

    override suspend fun deleteProjectState(projectId: UUID): Boolean {
        val updatedStates = getAllProjectStates().filterNot { it.projectId == projectId }
        return saveAllStates(updatedStates)

    }

    override suspend fun getStateById(projectId: UUID): ProjectState {
        return getAllProjectStates().first { it.projectId == projectId }

    }

    private fun saveAllStates(states: List<ProjectState>): Boolean {
        csvFile.writeText("")
        states.forEach { state ->
            csvFile.appendText("${state.projectId},${state.stateName}\n")
        }
        return true
    }
}
