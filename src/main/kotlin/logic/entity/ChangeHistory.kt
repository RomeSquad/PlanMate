package org.example.logic.entity

import java.util.Date

data class ChangeHistory(
    val projectID: String,
    val taskID: String,
    val authorID: String,
    val changeDate: String,
    val changeDescription: Date?
)