package org.example.logic.entity

import java.util.*

data class ChangeHistory(
    val projectID: Int,
    val taskID: Int,
    val authorID: Int,
    val changeDate: Date,
    val changeDescription: String
)