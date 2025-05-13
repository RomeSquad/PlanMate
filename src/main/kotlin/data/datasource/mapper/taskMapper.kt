package org.example.data.datasource.mapper

import org.example.logic.entity.Task
import java.util.*

fun Task.toCsvRow(): List<String> {
    return listOf(
        taskId.toString(),
        title,
        description,
        "\"${state.toCsvCell()}\"",
        projectId.toString(),
        createdBy.toString(),
        createdAt.toString(),
        updatedAt.toString()
    )
}

fun List<String>.fromCsvRowToTask(): Task {
    return Task(
        taskId = UUID.fromString(this[0]),
        title = this[1],
        description = this[2],
        state = this[3].parseState(),
        projectId = UUID.fromString(this[4]),
        createdBy = UUID.fromString(this[5]),
        createdAt = this[6].toLong(),
        updatedAt = this[7].toLong()
    )
}
