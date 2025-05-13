package org.example.logic.entity

import java.util.*

data class ChangeHistory(
    val projectID: UUID,
    val taskID: UUID,
    val authorID: UUID,
    val changeDate: Date,
    val changeDescription: String
)