package org.example.logic.entity

import java.util.Date

data class ChangeHistory(
    val projectID: Int,
    val taskID: String,
    val authorID: String,
    val changeDate: Date,
    val changeDescription: String
)