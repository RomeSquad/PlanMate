package org.example.presentation.utils.formatter.dataFormatter

import org.example.logic.entity.ModificationLog

fun ModificationLog.format(): String {
    return String.format(
        "%-12s %-10s %-10s %-20s %-30s",
        "Change: $changeDescription",
        "in ProjectID: $projectID",
        "TaskID: $taskID",
        "by Author: $authorID",
        "on $changeDate",
    )
}
