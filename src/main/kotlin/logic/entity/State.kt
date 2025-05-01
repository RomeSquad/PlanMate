package org.example.logic.entity

data class State(
    val projectId: String,
    val name: String
){
    override fun toString(): String {
        return String.format(
            "%-20s | %-20s",
            "Project ID: $projectId",
            "State: $name"
        )
    }

}
