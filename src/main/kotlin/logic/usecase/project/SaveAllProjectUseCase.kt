package org.example.logic.usecase.project

import org.example.logic.repository.ProjectRepository

class SaveAllProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    fun saveProjects()= projectRepository.saveAllProjects()
}
