package org.example.logic.usecase.project

import org.example.logic.repository.ProjectRepository

class DeleteProjectByIdUseCase(
    private val projectRepository: ProjectRepository
) {
    suspend fun deleteProjectById(id: Int) {
        return projectRepository.deleteProject(id)
    }
}