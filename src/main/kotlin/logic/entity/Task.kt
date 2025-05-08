package org.example.logic.entity

import org.bson.codecs.pojo.annotations.BsonId

data class Task(
    @BsonId val id: String,
    var title: String,
    var description: String,
    var state: State,
    val projectId: Int,
    val createdBy: String,
    val createdAt: Long,
    var updatedAt: Long
)