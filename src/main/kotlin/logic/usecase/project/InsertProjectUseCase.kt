package org.example.logic.usecase.project

import org.example.logic.entity.CreateProjectRequest
import org.example.logic.entity.CreateProjectResponse
import org.example.logic.repository.ProjectRepository

class InsertProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    fun insertProject(projectRequest: CreateProjectRequest): Result<CreateProjectResponse> {
        return projectRequest.takeIf{it.name.isNotBlank()}?.let { projectRepository.insertProject(it) }?: Result.failure(Exception("Project name cannot be blank"))
    }
}
