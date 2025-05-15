package org.example.logic.request.auth

import org.example.logic.entity.Project
import org.example.logic.entity.auth.User

data class ProjectCreationRequest(
   val project: Project,
   val user: User
)
