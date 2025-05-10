package org.example.logic.usecase.project

import org.example.logic.entity.CreateProjectRequest
import org.example.logic.entity.CreateProjectResponse
import org.example.logic.repository.ProjectRepository
class InsertProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    suspend fun insertProject(projectRequest: CreateProjectRequest): CreateProjectResponse {
        require(projectRequest.name.isNotBlank()) { "Project name cannot be blank" }
        return projectRepository.insertProject(projectRequest)
    }
}