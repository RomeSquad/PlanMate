package org.example.logic.usecase.project

import org.example.logic.entity.Project
import org.example.logic.entity.User
import org.example.logic.repository.ProjectRepository
import java.util.*

class InsertProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val validationProject: ValidationProject
) {
    suspend fun insertProject(project: Project, user: User): UUID {
        validationProject.validateCreateProject(project, user)
        return projectRepository.createProject(project, user)
    }
}