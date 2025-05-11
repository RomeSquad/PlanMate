package org.example.logic.usecase.project

import org.example.logic.repository.ProjectRepository
import java.util.UUID

class DeleteProjectByIdUseCase(
    private val projectRepository: ProjectRepository
) {
    suspend fun deleteProjectById(id: UUID) {
        return projectRepository.deleteProject(id)
    }
}