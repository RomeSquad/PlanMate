package org.example.logic.usecase.project

import org.example.logic.entity.Project
import org.example.logic.repository.ProjectRepository
import java.util.UUID

class GetProjectByIdUseCase(
    private val projectRepository: ProjectRepository
) {
    suspend fun getProjectById(id: UUID): Project {
        return projectRepository.getProjectById(id)
    }
}