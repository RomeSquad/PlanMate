package org.example.logic.usecase.project

import org.example.logic.entity.Project
import org.example.logic.repository.ProjectRepository

class GetProjectByIdUseCase(
    private val projectRepository: ProjectRepository
) {
    suspend fun getProjectById(id: Int): Project {
        require(id > 0) { "Project id must be greater than zero" }
        return projectRepository.getProjectById(id)
    }
}