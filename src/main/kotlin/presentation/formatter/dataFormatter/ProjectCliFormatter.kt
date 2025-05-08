package org.example.presentation.formatter.dataFormatter

import org.example.logic.entity.Project
import org.example.presentation.formatter.CliFormatter


fun Project.format(): String {
    return String.format(
        "%-10s | %-20s | %-20s | %-30s | %-15s | %-15s | %-10s | %-10s",
        "ID: $id",
        "Name: $name",
        "Description: $description",
        "State: $state",
        "Change History: $changeHistory"
    )
}

fun Project.changeHistoryFormat(): String {
    val cliFormatter = CliFormatter()
    return String.format(
        "%-10s | %-20s | %-20s \n%-30s \n",
        "ID: $id",
        "Name: $name",
        "State: $state",
        "Description: $description"
    ) + cliFormatter.verticalLayout(changeHistory.map { it.format() }.toList())
}

fun Project.swimlaneFormat(): String {
    return String.format(
        "%-10s | %-20s | %-20s \n%-30s \n%-15s",
        "ID: $id",
        "Name: $name",
        "State: $state",
        "Description: $description"
    )
}

