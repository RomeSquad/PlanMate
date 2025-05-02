package org.example.presentation.formatter.dataFormatter

import org.example.logic.entity.ChangeHistory

fun ChangeHistory.format(): String {
    return String.format(
        "%-12s | %-10s | %-10s | %-20s | %-30s",
        "ProjectID: $projectID",
        "TaskID: $taskID",
        "AuthorID: $authorID",
        "Date: $changeDate",
        "Change: $changeDescription"
    )
}
