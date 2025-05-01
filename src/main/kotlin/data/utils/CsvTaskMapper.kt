package org.example.data.utils

import org.example.logic.entity.Task

fun Task.toCsvRow(): List<String> {
    return listOf(
        id,
        title,
        description,
        "\"${state.toCsvCell()}\"",
        projectId,
        createdBy,
        createdAt.toString(),
        updatedAt.toString()
    )
}

fun List<String>.fromCsvRowToTask(): Task {
    return Task(
        id = this[0],
        title = this[1],
        description = this[2],
        state = this[3].parseState(),
        projectId = this[4],
        createdBy = this[5],
        createdAt = this[6].toLong(),
        updatedAt = this[7].toLong()
    )
}
