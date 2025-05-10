package org.example.presentation.utils.formatter.dataFormatter

import org.example.logic.entity.Task

fun Task.format(): String {
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