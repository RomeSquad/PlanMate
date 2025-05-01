package org.example.data.utils

import data.utils.ParserImpl
import org.example.logic.entity.ChangeHistory
import org.example.logic.entity.Project
import org.example.logic.entity.State
import java.util.Locale

fun Project.toCsvRow(): List<String> {
    return listOf(id.toString(), name, description, "\"${changeHistory.map { it.toCsvCell() }}\"" ,"\"${state.toCsvCell()}\"")
}
fun ChangeHistory.toCsvCell() = listOf(projectID, taskID, authorID, changeDescription, changeDate).toString()

fun State.toCsvCell() = listOf(projectId, name).toString()
val dateFormat = java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)


fun List<String>.fromCsvRowToProject(): Project {
    return Project(
        id = this[0].toInt(),
        name = this[1],
        description = this[2],
        changeHistory = this[3].parseChangeHistory(),
        state = this[4].parseState()


    )
}

fun String.parseChangeHistory(): List<ChangeHistory> {
    val parser = ParserImpl()
    val change = split("],[")
    return change.map {
        val changeHistory = parser.parseStringList(it)
        ChangeHistory(
            projectID = changeHistory[0].trim().removeSurrounding("[", "]" ),
            taskID = changeHistory[1].trim(),
            authorID = changeHistory[2].trim(),
            changeDescription = changeHistory[3].trim(),
            changeDate = dateFormat.parse(changeHistory[4].trim().removeSurrounding("[", "]" ))
        )
    }
}

fun String.parseState(): State {
    val parser = ParserImpl()
    val state = parser.parseStringList(this)
    return State(
        projectId = state[0].trim(),
        name = state[1].trim()
    )
}