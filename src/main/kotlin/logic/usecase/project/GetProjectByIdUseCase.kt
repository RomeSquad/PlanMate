package org.example.logic.usecase.project

import org.example.logic.entity.Project
import org.example.logic.repository.ProjectRepository

class GetProjectByIdUseCase(
    private val projectRepository: ProjectRepository
) {
    fun getProjectById(id: Int): Result<Project> {
        return if(id >0) projectRepository.getProjectById(id)  else  Result.failure(Exception("Project name cannot be blank"))
    }
}