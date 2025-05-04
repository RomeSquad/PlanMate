package logic.usecases

import org.example.logic.entity.Project
import org.example.logic.repository.ProjectRepository

class EditProjectUseCase(
    private val projectRepository: ProjectRepository
) {
    fun execute(project: Project) {
        projectRepository.editProject(project)
    }
}


/*

*/