package org.example.logic.usecase.project

import org.example.logic.repository.ProjectRepository

class DeleteProjectByIdUseCase(
    private val projectRepository: ProjectRepository
) {
    fun deleteProjectById(id: Int): Result<Unit> {
        return projectRepository.deleteProject(id)
    }
}