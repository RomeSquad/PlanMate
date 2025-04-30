package org.example.logic.entity

data class Project(
    val id: Int,
    val name: String,
    val description: String,
    val changeHistory: List<ChangeHistory>,
    val state: State
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
        changeHistory = listOf(
            ChangeHistory(
                /*TODO*/
            )
        ),
        state = State(
            /*TODO*/
        )
    )