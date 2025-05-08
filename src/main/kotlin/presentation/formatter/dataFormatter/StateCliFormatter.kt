package org.example.presentation.formatter.dataFormatter

import org.example.logic.entity.ProjectState

fun ProjectState.format(): String {
    return String.format("%-20s | %-20s", "Project ID: $projectId", "State: $stateName")
}

