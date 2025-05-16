package org.example.logic.request

import data.datasource.authentication.dto.UserDto
import org.example.logic.entity.Project
import org.example.logic.entity.auth.User

data class ProjectCreationRequest(
   val project: Project,
)
