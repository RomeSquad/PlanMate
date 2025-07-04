package org.example.presentation.utils.formatter.dataFormatter

import org.example.logic.entity.ModificationLog
import org.example.logic.entity.Project
import org.example.presentation.utils.formatter.CliFormatter


fun Project.format(): String {
    return String.format(
        "%-10s | %-20s | %-20s | %-30s | %-15s | %-15s | %-10s | %-10s",
        "ID: $projectId",
        "Name: $name",
        "Description: $description",
        "State: $state"
    )
}

fun changeHistoryFormat(project: Project, changeHistory: List<ModificationLog>): String {
    val cliFormatter = CliFormatter()
    return String.format(
        "%-10s | %-20s | %-20s \n%-30s \n",
        "ID: ${project.projectId}",
        "Name: ${project.name}",
        "State: ${project.state}",
        "Description: ${project.description}"
    ) + cliFormatter.verticalLayout(changeHistory.map { it.format() }.toList())
}

fun Project.swimlaneFormat(): String {
    return String.format(
        "%-10s | %-20s | %-20s \n%-30s \n%-15s",
        "ID: $projectId",
        "Name: $name",
        "State: $state",
        "Description: $description"
    )
}

