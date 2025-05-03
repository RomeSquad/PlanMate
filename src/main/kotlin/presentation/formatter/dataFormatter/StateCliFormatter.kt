package org.example.presentation.formatter.dataFormatter

import org.example.logic.entity.State

fun State.format(): String {
    return String.format("%-20s | %-20s", "Project ID: $projectId", "State: $name")
}

