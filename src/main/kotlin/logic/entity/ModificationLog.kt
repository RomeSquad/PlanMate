package org.example.logic.entity

import java.util.*

data class ModificationLog(
    val projectID: UUID,
    val taskID: UUID?,
    val authorID: UUID,
    val changeDate: Date,
    val changeDescription: String
)