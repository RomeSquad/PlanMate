package org.example.data.repository

import org.example.data.datasource.project.ProjectDataSource
import org.example.logic.entity.CreateProjectRequest
import org.example.logic.entity.CreateProjectResponse
import org.example.logic.entity.Project
import org.example.logic.entity.toProject
import org.example.logic.entity.Project
import org.example.logic.repository.ProjectRepository

class ProjectRepositoryImpl(
    private val projectDataSource: ProjectDataSource
) : ProjectRepository {




    private var projects = mutableListOf<Project>()

    init {
        projects += getAllProjects().getOrThrow()
    }

    override fun insertProject(projectRequest: CreateProjectRequest): Result<CreateProjectResponse> {
        return projectRequest.toProject(getLatestProjectId()).run {
            projects.add(this)
            Result.success(CreateProjectResponse(id))
        }
    }

    override fun getAllProjects(): Result<List<Project>> {
        return projectDataSource.getAllProjects()
    }
    private fun getLatestProjectId() = projects.lastOrNull()?.id ?: 0
}
    fun editProject(project: Project) {
        if (project.id.isEmpty()) return

        val existingProject = projectDataSource.getProjectById(project.id)

        if (existingProject == null) return

        if (existingProject.name == project.name &&
            existingProject.description == project.description
        ) return

        projectDataSource.editProject(project)
    }


     fun deleteProject(id: String) {

    }

     fun getProjectById(id: String): Project? {
        return null
    }
}