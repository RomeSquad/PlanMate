package org.example.logic.entity

data class Task(
    val id: String,
    var title: String,
    var description: String,
    var state: State,
    val projectId: String,
    val createdBy: String,
    val createdAt: Long,
    var updatedAt: Long
){
    override fun toString(): String {
        return String.format(
            "%-10s | %-20s | %-20s | %-30s | %-15s | %-15s | %-10s | %-10s",
            "ID: $id",
            "Title: $title",
            "Description: $description",
            "State: $state",
            "ProjectID: $projectId",
            "Created by: $createdBy",
            "Created: $createdAt",
            "Updated: $updatedAt"
        )
    }
}