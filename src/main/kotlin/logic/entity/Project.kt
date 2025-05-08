package org.example.logic.entity

import java.util.Date

data class Project(
    val id: Int,
    val name: String,
    val description: String,

)

data class CreateProjectRequest(
    val name: String,
    val userId: Int,
    val userName: String,
    val description: String
)

data class CreateProjectResponse(
    val id: Int,
)

fun CreateProjectRequest.toProject(lastId: Int) =
    Project(
        id = lastId + 1,
        name = name,
        description = description,

            stateName = "InProgress"
        )
    )