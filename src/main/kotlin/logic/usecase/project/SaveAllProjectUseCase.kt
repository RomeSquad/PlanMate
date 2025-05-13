package org.example.logic.usecase.project

import org.example.logic.repository.ProjectRepository


class SaveAllProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    suspend fun saveProjects() {
        projectRepository.saveAllProjects()
    }
}