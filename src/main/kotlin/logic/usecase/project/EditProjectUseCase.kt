package logic.usecase.project

import org.example.logic.entity.Project
import org.example.logic.repository.ProjectRepository

class EditProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    suspend fun execute(project: Project) {
        projectRepository.editProject(project)
        return projectRepository.editProject(project)
    }
}

