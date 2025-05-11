package org.example.logic.entity

import org.bson.codecs.pojo.annotations.BsonId
import java.util.UUID

data class Task(
    @BsonId val id: UUID,
    var title: String,
    var description: String,
    var state: ProjectState,
    val projectId: UUID,
    val createdBy: UUID,
    val createdAt: Long,
    var updatedAt: Long
)