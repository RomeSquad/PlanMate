package org.example.logic.usecase.project

import org.example.logic.entity.Project
import org.example.logic.repository.ProjectRepository

class GetAllProjectsUseCase(
    private val projectRepository: ProjectRepository,
) {
    suspend fun getAllProjects(): List<Project> {
        return projectRepository.getAllProjects()
    }
}