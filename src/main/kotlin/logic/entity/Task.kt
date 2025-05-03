package org.example.logic.entity

data class Task(
    val id: String,
    var title: String,
    var description: String,
    var state: State,
    val projectId: Int,
    val createdBy: String,
    val createdAt: Long,
    var updatedAt: Long
)