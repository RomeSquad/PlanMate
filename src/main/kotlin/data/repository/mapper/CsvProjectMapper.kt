package org.example.data.repository.mapper

import org.example.data.utils.ParserImpl
import org.example.logic.entity.ChangeHistory
import org.example.logic.entity.Project
import org.example.logic.entity.State
import java.text.SimpleDateFormat
import java.util.*



fun Project.toCsvRow(): List<String> {
    return listOf(
        id.toString(),
        name,
        description,
        "\"${state.toCsvCell()}\""
    )
}

fun ChangeHistory.toCsvCell() = listOf(projectID, taskID, authorID, changeDescription, changeDate).toString()

fun State.toCsvCell() = listOf(projectId, stateName).toString()
val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)




fun List<String>.fromCsvRowToProject(): Project {
    return Project(
        id = this[0].toInt(),
        name = this[1],
        description = this[2],
        state = this[4].parseState()


    )
}



fun String.parseState(): State {
    val parser = ParserImpl()
    val state = parser.parseStringList(this)
    return State(
        projectId = state[0].trim(),
        stateName = state[1].trim()
    )
}