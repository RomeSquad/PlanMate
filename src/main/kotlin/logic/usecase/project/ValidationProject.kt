package org.example.logic.usecase.project

import org.example.logic.entity.Project
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.exception.EmptyTitleException
import org.example.logic.exception.InvalidUserException

class ValidationProject {
    fun validateCreateProject(project: Project, user: User) {
        if (user.userRole != UserRole.ADMIN) throw InvalidUserException()
        if (project.name.isBlank()) throw EmptyTitleException()
    }



}