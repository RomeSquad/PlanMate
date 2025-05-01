package org.example.logic.entity

import java.util.Date

data class ChangeHistory(
    val projectID: String,
    val taskID: String,
    val authorID: String,
    val changeDate: Date,
    val changeDescription: String
){
    override fun toString(): String {
        return String.format(
            "%-12s | %-10s | %-10s | %-20s | %-30s",
            "ProjectID: $projectID",
            "TaskID: $taskID",
            "AuthorID: $authorID",
            "Date: $changeDate",
            "Change: $changeDescription"
        )
    }
}