package org.example.data.datasource.mapper

import org.example.data.utils.ParserImpl
import org.example.logic.entity.CreateProjectRequest
import org.example.logic.entity.ModificationLog
import org.example.logic.entity.Project
import org.example.logic.entity.ProjectState
import java.text.SimpleDateFormat
import java.util.*


fun Project.toCsvRow(): List<String> {
    return listOf(
        projectId.toString(),
        name,
        description,
        "\"${state.toCsvCell()}\""
    )
}

fun ModificationLog.toCsvCell() = listOf(projectID, taskID, authorID, changeDescription, changeDate).toString()

fun ProjectState.toCsvCell() = listOf(projectId, stateName).toString()
val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)


fun List<String>.fromCsvRowToProject(): Project {
    return Project(
        projectId = UUID.fromString(this[0].trim()),
        name = this[1],
        description = this[2],
        state = this[3].parseState()


    )
}


fun String.parseState(): ProjectState {
    val parser = ParserImpl()
    val state = parser.parseStringList(this)
    return ProjectState(
        projectId = UUID.fromString(state[0].trim()),
        stateName = state[1].trim()
    )
}

fun CreateProjectRequest.toProject(): Project {
    val id = UUID.randomUUID()
    return Project(
        projectId = id,
        name = name,
        description = description,
        state = ProjectState(
            projectId = id,
            stateName = "InProgress"
        )
    )
}